package rbasamoyai.createbigcannons.cannonmount;

import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionType;
import com.simibubi.create.content.contraptions.components.structureMovement.StructureTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;
import rbasamoyai.createbigcannons.CBCContraptionTypes;
import rbasamoyai.createbigcannons.cannons.autocannon.*;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.network.CBCNetwork;
import rbasamoyai.createbigcannons.network.ClientboundAnimateCannonContraptionPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MountedAutocannonContraption extends AbstractMountedCannonContraption {

	private AutocannonMaterial cannonMaterial;
	private BlockPos recoilSpringPos;



	public MountedAutocannonContraption() { super (45, 90); }

	@Override
	public LazyOptional<IItemHandler> getItemOptional() {
		return this.presentTileEntities.get(this.startPos) instanceof AutocannonBreechBlockEntity breech ? LazyOptional.of(breech::createItemHandler) : LazyOptional.empty();
	}

	@Override
	public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
		if (!this.collectCannonBlocks(level, pos)) return false;
		this.bounds = new AABB(BlockPos.ZERO);
		this.bounds = this.bounds.inflate(Math.ceil(Math.sqrt(getRadius(this.getBlocks().keySet(), Direction.Axis.Y))));
		return !this.blocks.isEmpty();
	}

	private boolean collectCannonBlocks(Level level, BlockPos pos) throws AssemblyException {
		BlockState startState = level.getBlockState(pos);

		if (!(startState.getBlock() instanceof AutocannonBlock startCannon)) {
			return false;
		}
		if (!startCannon.isComplete(startState)) {
			throw hasIncompleteCannonBlocks(pos);
		}

		AutocannonMaterial material = startCannon.getAutocannonMaterial();

		List<StructureBlockInfo> cannonBlocks = new ArrayList<>();
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getTileEntityNBT(level, pos)));

		int cannonLength = 1;

		Direction cannonFacing = startCannon.getFacing(startState);

		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, cannonFacing.getAxis());
		Direction negative = positive.getOpposite();

		BlockPos start = pos;
		BlockState nextState = level.getBlockState(pos.relative(positive));
		boolean positiveBreech = false;

		while (nextState.getBlock() instanceof AutocannonBlock cBlock && this.isConnectedToCannon(level, nextState, start.relative(positive), positive, material)) {
			start = start.relative(positive);
			if (!cBlock.isComplete(nextState)) throw hasIncompleteCannonBlocks(start);
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getTileEntityNBT(level, start)));
			cannonLength++;
			positiveBreech = cBlock.isBreechMechanism(nextState);
			nextState = level.getBlockState(start.relative(positive));
			if (cannonLength > getMaxCannonLength()) throw cannonTooLarge();
			if (positiveBreech) break;
		}
		BlockPos positiveEndPos = positiveBreech ? start : start.relative(negative);

		start = pos;
		nextState = level.getBlockState(pos.relative(negative));

		boolean negativeBreech = false;
		while (nextState.getBlock() instanceof AutocannonBlock cBlock && this.isConnectedToCannon(level, nextState, start.relative(negative), negative, material)) {
			start = start.relative(negative);
			if (!cBlock.isComplete(nextState)) throw hasIncompleteCannonBlocks(start);
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getTileEntityNBT(level, start)));
			cannonLength++;
			negativeBreech = cBlock.isBreechMechanism(nextState);
			nextState = level.getBlockState(start.relative(negative));
			if (cannonLength > getMaxCannonLength()) throw cannonTooLarge();
			if (negativeBreech) break;
		}
		BlockPos negativeEndPos = negativeBreech ? start : start.relative(positive);

		if (positiveBreech == negativeBreech) throw invalidCannon();

		this.initialOrientation = negativeBreech ? positive : negative;
		this.startPos = negativeBreech ? negativeEndPos : positiveEndPos;

		this.anchor = pos;

		this.startPos = this.startPos.subtract(pos);
		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos.subtract(pos);
			StructureBlockInfo localBlockInfo = new StructureBlockInfo(localPos, blockInfo.state, blockInfo.nbt);
			this.blocks.put(localPos, localBlockInfo);

			if (blockInfo.nbt == null) continue;
			BlockEntity be = BlockEntity.loadStatic(localPos, blockInfo.state, blockInfo.nbt);
			this.presentTileEntities.put(localPos, be);
		}

		StructureBlockInfo possibleSpring = this.blocks.get(this.startPos.relative(this.initialOrientation));
		if (possibleSpring != null
				&& possibleSpring.state.getBlock() instanceof AutocannonRecoilSpringBlock springBlock
				&& springBlock.getFacing(possibleSpring.state) == this.initialOrientation) {
			this.recoilSpringPos = this.startPos.relative(this.initialOrientation).immutable();
			if (this.presentTileEntities.get(this.recoilSpringPos) instanceof AutocannonRecoilSpringBlockEntity springBE) {
				for (int i = 2; i < cannonLength; ++i) {
					BlockPos pos1 = this.startPos.relative(this.initialOrientation, i);
					StructureBlockInfo blockInfo = this.blocks.get(pos1);
					if (blockInfo == null) continue;
					springBE.toAnimate.put(pos1.subtract(this.recoilSpringPos), blockInfo.state);
					if (blockInfo.state.hasProperty(AutocannonBarrelBlock.ASSEMBLED)) {
						this.blocks.put(pos1, new StructureBlockInfo(pos1, blockInfo.state.setValue(AutocannonBarrelBlock.ASSEMBLED, true), blockInfo.nbt));
					}
				}
				CompoundTag newTag = springBE.saveWithFullMetadata();
				newTag.remove("x");
				newTag.remove("y");
				newTag.remove("z");
				this.blocks.put(this.recoilSpringPos, new StructureBlockInfo(this.recoilSpringPos, possibleSpring.state, newTag));
			}
		}

		this.cannonMaterial = material;

		return true;
	}

	private boolean isConnectedToCannon(LevelAccessor level, BlockState state, BlockPos pos, Direction connection, AutocannonMaterial material) {
		AutocannonBlock cBlock = (AutocannonBlock) state.getBlock();
		if (cBlock.getAutocannonMaterialInLevel(level, state, pos) != material) return false;
		return ((IAutocannonBlockEntity) level.getBlockEntity(pos)).cannonBehavior().isConnectedTo(connection.getOpposite());
	}

	@Override
	public void addBlocksToWorld(Level world, StructureTransform transform) {
		Map<BlockPos, StructureBlockInfo> modifiedBlocks = new HashMap<>();
		for (Map.Entry<BlockPos, StructureBlockInfo> entry : this.blocks.entrySet()) {
			StructureBlockInfo info = entry.getValue();
			BlockState newState = info.state;
			boolean modified = true;

			if (newState.hasProperty(AutocannonBarrelBlock.ASSEMBLED) && newState.getValue(AutocannonBarrelBlock.ASSEMBLED)) {
				newState = newState.setValue(AutocannonBarrelBlock.ASSEMBLED, false);
				modified = true;
			}

			if (info.nbt != null) {
				if (info.nbt.contains("AnimateTicks")) {
					info.nbt.remove("AnimateTicks");
					modified = true;
				}
				if (info.nbt.contains("RenderedBlocks")) {
					info.nbt.remove("RenderedBlocks");
					modified = true;
				}
			}

			if (modified) modifiedBlocks.put(info.pos, new StructureBlockInfo(info.pos, newState, info.nbt));
		}
		this.blocks.putAll(modifiedBlocks);
		super.addBlocksToWorld(world, transform);
	}

	@Override
	public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
		if (this.startPos == null
			|| this.cannonMaterial == null
			|| !(this.presentTileEntities.get(this.startPos) instanceof AutocannonBreechBlockEntity breech)
			|| !breech.canFire()) return;

		LazyOptional<IItemHandler> lzop = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack foundProjectile = lzop.map(h -> h.extractItem(0, 1, false)).orElse(ItemStack.EMPTY);
		breech.cannonBehavior().removeItem();
		if (!(foundProjectile.getItem() instanceof AutocannonCartridgeItem round) || !AutocannonCartridgeItem.hasProjectile(foundProjectile)) return;

		Vec3 ejectPos = entity.toGlobalVector(Vec3.atCenterOf(this.startPos.relative(this.initialOrientation.getOpposite())), 1.0f);
		Vec3 centerPos = entity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 1.0f);
		ItemStack ejectStack = round.getEmptyCartridge(foundProjectile);
		if (!ejectStack.isEmpty()) {
			ItemStack output = breech.insertOutput(ejectStack);
			if (!output.isEmpty()) {
				ItemEntity ejectEntity = new ItemEntity(level, ejectPos.x, ejectPos.y, ejectPos.z, ejectStack);
				ejectEntity.setDeltaMovement(ejectPos.subtract(centerPos).normalize().scale(0.5));
				level.addFreshEntity(ejectEntity);
			}
		}

		boolean canFail = !CBCConfigs.SERVER.failure.disableAllFailure.get();
		BlockPos currentPos = this.startPos.relative(this.initialOrientation);
		int barrelTravelled = 0;

		while (this.presentTileEntities.get(currentPos) instanceof IAutocannonBlockEntity autocannon) {
			ItemCannonBehavior behavior = autocannon.cannonBehavior();
			ItemStack stack = behavior.storedItem();

			if (stack.isEmpty()) {
				++barrelTravelled;
				if (barrelTravelled > this.cannonMaterial.maxLength()) {
					StructureBlockInfo oldInfo = this.blocks.get(currentPos);
					behavior.tryLoadingItem(stack);
					CompoundTag tag = this.presentTileEntities.get(currentPos).saveWithFullMetadata();
					tag.remove("x");
					tag.remove("y");
					tag.remove("z");
					StructureBlockInfo squibInfo = new StructureBlockInfo(currentPos, oldInfo.state, tag);
					this.blocks.put(currentPos, squibInfo);
					Vec3 squibPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos), 1.0f);
					level.playSound(null, squibPos.x, squibPos.y, squibPos.z, oldInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 10.0f, 0.0f);
					return;
				}
				currentPos = currentPos.relative(this.initialOrientation);
			} else {
				behavior.removeItem();
				if (canFail) {
					// TODO: fail
				}
				return;
			}
		}

		Vec3 spawnPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
		Vec3 vec1 = spawnPos.subtract(centerPos).normalize();
		Vec3 particlePos = spawnPos.subtract(vec1.scale(1.5));

		AbstractAutocannonProjectile projectile = AutocannonCartridgeItem.getAutocannonProjectile(foundProjectile, level);
		if (projectile != null) {
			projectile.setPos(spawnPos);
			projectile.setChargePower(barrelTravelled);
			projectile.setTracer(true);
			projectile.shoot(vec1.x, vec1.y, vec1.z, barrelTravelled, 0.05f);
			projectile.xRotO = projectile.getXRot();
			projectile.yRotO = projectile.getYRot();
			level.addFreshEntity(projectile);
		}

		breech.handleFiring();
		if (this.presentTileEntities.get(this.recoilSpringPos) instanceof AutocannonRecoilSpringBlockEntity spring) spring.handleFiring();
		CBCNetwork.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new ClientboundAnimateCannonContraptionPacket(entity));

		for (ServerPlayer player : level.players()) {
			level.sendParticles(player, new CannonPlumeParticleData(0.1f), true, particlePos.x, particlePos.y, particlePos.z, 0, vec1.x, vec1.y, vec1.z, 1.0f);
		}
		level.playSound(null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0f, 2.0f);
	}

	@Override
	public void animate() {
		super.animate();
		if (this.presentTileEntities.get(this.startPos) instanceof AutocannonBreechBlockEntity breech) breech.handleFiring();
		if (this.presentTileEntities.get(this.recoilSpringPos) instanceof AutocannonRecoilSpringBlockEntity spring) spring.handleFiring();
	}

	@Override
	public void tick(Level level, PitchOrientedContraptionEntity entity) {
		super.tick(level, entity);

		if (level instanceof ServerLevel slevel) this.fireShot(slevel, entity);

		for (Map.Entry<BlockPos, BlockEntity> entry : this.presentTileEntities.entrySet()) {
			if (entry.getValue() instanceof IAutocannonBlockEntity autocannon) autocannon.tickFromContraption(level, entity, entry.getKey());
		}
	}

	@Override
	public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower) {
		if (this.presentTileEntities.get(this.startPos) instanceof AutocannonBreechBlockEntity breech) breech.setFireRate(firePower);
	}

	@Override public float getWeightForStress() { return this.cannonMaterial == null ? this.blocks.size() : this.blocks.size() * this.cannonMaterial.weight(); }

	@Override
	public CompoundTag writeNBT(boolean clientData) {
		CompoundTag tag = super.writeNBT(clientData);
		tag.putString("AutocannonMaterial", this.cannonMaterial == null ? AutocannonMaterial.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
		if (this.startPos != null) tag.put("StartPos", NbtUtils.writeBlockPos(this.startPos));
		if (this.recoilSpringPos != null) tag.put("RecoilSpringPos", NbtUtils.writeBlockPos(this.recoilSpringPos));
		return tag;
	}

	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = AutocannonMaterial.fromName(new ResourceLocation(tag.getString("AutocannonMaterial")));
		this.startPos = tag.contains("StartPos") ? NbtUtils.readBlockPos(tag.getCompound("StartPos")) : null;
		this.recoilSpringPos = tag.contains("RecoilSpringPos") ? NbtUtils.readBlockPos(tag.getCompound("RecoilSpringPos")) : null;
	}

	@Override protected ContraptionType getType() { return CBCContraptionTypes.MOUNTED_AUTOCANNON; }

}
