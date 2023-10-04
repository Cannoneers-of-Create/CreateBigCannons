package rbasamoyai.createbigcannons.cannon_control.contraption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.StructureTransform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.effects.CannonPlumeParticleData;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBarrelBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.cannons.autocannon.IAutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterialProperties;
import rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring.AutocannonRecoilSpringBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring.AutocannonRecoilSpringBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCAutocannonMaterials;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCContraptionTypes;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.network.ClientboundAnimateCannonContraptionPacket;

public class MountedAutocannonContraption extends AbstractMountedCannonContraption implements ItemCannon {

	private AutocannonMaterial cannonMaterial;
	private BlockPos recoilSpringPos;
	private boolean isHandle = false;

	@Override
	public float maximumDepression(@Nonnull ControlPitchContraption controller) {
		BlockState state = controller.getControllerState();
		if (CBCBlocks.CANNON_MOUNT.has(state)) return 45;
		if (CBCBlocks.CANNON_CARRIAGE.has(state)) return 15;
		return 0;
	}

	@Override
	public float maximumElevation(@Nonnull ControlPitchContraption controller) {
		BlockState state = controller.getControllerState();
		if (CBCBlocks.CANNON_MOUNT.has(state)) return 90;
		if (CBCBlocks.CANNON_CARRIAGE.has(state)) return this.isHandle ? 45 : 90;
		return 0;
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
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getBlockEntityNBT(level, pos)));

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
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
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
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
			cannonLength++;
			negativeBreech = cBlock.isBreechMechanism(nextState);
			nextState = level.getBlockState(start.relative(negative));
			if (cannonLength > getMaxCannonLength()) throw cannonTooLarge();
			if (negativeBreech) break;
		}
		BlockPos negativeEndPos = negativeBreech ? start : start.relative(positive);

		if (cannonLength < 2 || positiveBreech && negativeBreech) throw invalidCannon();

		this.startPos = !positiveBreech && !negativeBreech ? pos : negativeBreech ? negativeEndPos : positiveEndPos;
		BlockState breechState = level.getBlockState(this.startPos);
		if (!(breechState.getBlock() instanceof AutocannonBreechBlock)) throw invalidCannon();
		this.initialOrientation = breechState.getValue(BlockStateProperties.FACING);

		this.anchor = pos;

		this.startPos = this.startPos.subtract(pos);

		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos.subtract(pos);
			StructureBlockInfo localBlockInfo = new StructureBlockInfo(localPos, blockInfo.state, blockInfo.nbt);
			this.blocks.put(localPos, localBlockInfo);

			if (blockInfo.nbt == null) continue;
			BlockEntity be = BlockEntity.loadStatic(localPos, blockInfo.state, blockInfo.nbt);
			this.presentBlockEntities.put(localPos, be);
		}

		StructureBlockInfo startInfo = this.blocks.get(this.startPos);
		if (startInfo == null || !(startInfo.state.getBlock() instanceof AutocannonBreechBlock))
			throw noAutocannonBreech();
		this.isHandle = startInfo.state.hasProperty(AutocannonBreechBlock.HANDLE) && startInfo.state.getValue(AutocannonBreechBlock.HANDLE);
		if (this.isHandle) {
			this.getSeats().add(this.startPos.immutable());
		}

		StructureBlockInfo possibleSpring = this.blocks.get(this.startPos.relative(this.initialOrientation));
		if (possibleSpring != null
			&& possibleSpring.state.getBlock() instanceof AutocannonRecoilSpringBlock springBlock
			&& springBlock.getFacing(possibleSpring.state) == this.initialOrientation) {
			this.recoilSpringPos = this.startPos.relative(this.initialOrientation).immutable();
			if (this.presentBlockEntities.get(this.recoilSpringPos) instanceof AutocannonRecoilSpringBlockEntity springBE) {
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
		return level.getBlockEntity(pos) instanceof IAutocannonBlockEntity cbe
			&& level.getBlockEntity(pos.relative(connection.getOpposite())) instanceof IAutocannonBlockEntity cbe1
			&& cbe.cannonBehavior().isConnectedTo(connection.getOpposite())
			&& cbe1.cannonBehavior().isConnectedTo(connection);
	}

	public static AssemblyException noAutocannonBreech() {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.noAutocannonBreech"));
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
	public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity, @Nullable ControlPitchContraption controller) {
		if (this.startPos == null
			|| this.cannonMaterial == null
			|| !(this.presentBlockEntities.get(this.startPos) instanceof AbstractAutocannonBreechBlockEntity breech)
			|| !breech.canFire()) return;

		ItemStack foundProjectile = breech.extractNextInput();
		if (!(foundProjectile.getItem() instanceof AutocannonAmmoItem round)) return;

		Vec3 ejectPos = entity.toGlobalVector(Vec3.atCenterOf(this.startPos.relative(this.isHandle ? Direction.DOWN : this.initialOrientation.getOpposite())), 1.0f);
		Vec3 centerPos = entity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 1.0f);
		ItemStack ejectStack = round.getSpentItem(foundProjectile);
		if (!ejectStack.isEmpty()) {
			ItemStack output = breech.insertOutput(ejectStack);
			if (!output.isEmpty()) {
				ItemEntity ejectEntity = new ItemEntity(level, ejectPos.x, ejectPos.y, ejectPos.z, ejectStack);
				Vec3 eject = ejectPos.subtract(centerPos).normalize();
				ejectEntity.setDeltaMovement(eject.scale(this.isHandle ? 0.1 : 0.5));
				ejectEntity.setPickUpDelay(20);
				level.addFreshEntity(ejectEntity);
			}
		}

		AutocannonMaterialProperties properties = this.cannonMaterial.properties();

		float speed = properties.baseSpeed();
		float spread = properties.baseSpread();

		boolean canFail = !CBCConfigs.SERVER.failure.disableAllFailure.get();
		BlockPos currentPos = this.startPos.relative(this.initialOrientation);
		int barrelTravelled = 0;

		while (this.presentBlockEntities.get(currentPos) instanceof IAutocannonBlockEntity autocannon) {
			ItemCannonBehavior behavior = autocannon.cannonBehavior();

			if (behavior.canLoadItem(foundProjectile)) {
				++barrelTravelled;
				if (barrelTravelled <= properties.maxSpeedIncreases())
					speed += properties.speedIncreasePerBarrel();
				spread -= properties.spreadReductionPerBarrel();
				spread = Math.max(spread, 0);
				if (barrelTravelled > properties.maxBarrelLength()) {
					StructureBlockInfo oldInfo = this.blocks.get(currentPos);
					behavior.tryLoadingItem(foundProjectile);
					CompoundTag tag = this.presentBlockEntities.get(currentPos).saveWithFullMetadata();
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
					Vec3 failurePoint = entity.toGlobalVector(Vec3.atCenterOf(currentPos), 1.0f);
					level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, 2, Explosion.BlockInteraction.NONE);
					for (int i = 0; i < 10; ++i) {
						BlockPos pos = currentPos.relative(this.initialOrientation, i);
						this.blocks.remove(pos);
					}
					if (controller != null) controller.disassemble();
				}
				return;
			}
		}

		Vec3 spawnPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
		Vec3 vec1 = spawnPos.subtract(centerPos).normalize();
		Vec3 particlePos = spawnPos.subtract(vec1.scale(1.5));

		float recoilMagnitude = properties.baseRecoil();

		boolean isTracer = CBCConfigs.SERVER.munitions.allProjectilesAreTracers.get() || round.isTracer(foundProjectile);

		AbstractAutocannonProjectile projectile = round.getAutocannonProjectile(foundProjectile, level);
		if (projectile != null) {
			projectile.setPos(spawnPos);
			projectile.setChargePower(barrelTravelled);
			projectile.setTracer(isTracer);
			projectile.setLifetime(properties.projectileLifetime());
			projectile.shoot(vec1.x, vec1.y, vec1.z, speed, spread);
			projectile.xRotO = projectile.getXRot();
			projectile.yRotO = projectile.getYRot();
			level.addFreshEntity(projectile);
			recoilMagnitude += 0.5;
		}

		recoilMagnitude *= CBCConfigs.SERVER.cannons.autocannonRecoilScale.getF();
		if (controller != null) controller.onRecoil(vec1.scale(-recoilMagnitude), entity);

		breech.handleFiring();
		if (this.presentBlockEntities.get(this.recoilSpringPos) instanceof AutocannonRecoilSpringBlockEntity spring)
			spring.handleFiring();
		NetworkPlatform.sendToClientTracking(new ClientboundAnimateCannonContraptionPacket(entity), entity);

		Vec3 particleVel = vec1.scale(1.25);
		for (ServerPlayer player : level.players()) {
			if (entity.getControllingPassenger() == player) continue;
			level.sendParticles(player, new CannonPlumeParticleData(0.1f), true, particlePos.x, particlePos.y, particlePos.z, 0, particleVel.x, particleVel.y, particleVel.z, 1.0f);
		}
		CBCSoundEvents.FIRE_AUTOCANNON.playOnServer(level, new BlockPos(spawnPos));
	}

	@Override
	public void animate() {
		super.animate();
		if (this.presentBlockEntities.get(this.startPos) instanceof AbstractAutocannonBreechBlockEntity breech)
			breech.handleFiring();
		if (this.presentBlockEntities.get(this.recoilSpringPos) instanceof AutocannonRecoilSpringBlockEntity spring)
			spring.handleFiring();
	}

	@Override
	public void tick(Level level, PitchOrientedContraptionEntity entity) {
		super.tick(level, entity);

		if (level instanceof ServerLevel slevel && this.canBeFiredOnController(entity.getController()))
			this.fireShot(slevel, entity, entity.getController());

		for (Map.Entry<BlockPos, BlockEntity> entry : this.presentBlockEntities.entrySet()) {
			if (entry.getValue() instanceof IAutocannonBlockEntity autocannon)
				autocannon.tickFromContraption(level, entity, entry.getKey());
		}
	}

	@Override
	public BlockPos getSeatPos(Entity entity) {
		return entity == this.entity.getControllingPassenger() ? this.startPos.relative(this.initialOrientation.getOpposite()) : super.getSeatPos(entity);
	}

	@Override
	public boolean canBeTurnedByController(ControlPitchContraption control) {
		return !this.isHandle;
	}

	@Override
	public boolean canBeTurnedByPassenger(Entity entity) {
		return this.isHandle && entity instanceof Player;
	}

	@Override
	public boolean canBeFiredOnController(ControlPitchContraption control) {
		return !this.isHandle && this.entity.getVehicle() != control;
	}

	@Override
	public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower, ControlPitchContraption controller) {
		if (this.presentBlockEntities.get(this.startPos) instanceof AbstractAutocannonBreechBlockEntity breech)
			breech.setFireRate(firePower);
	}

	public void trySettingFireRateCarriage(int fireRateAdjustment) {
		if (this.presentBlockEntities.get(this.startPos) instanceof AbstractAutocannonBreechBlockEntity breech
			&& (fireRateAdjustment > 0 || breech.getFireRate() > 1)) // Can't turn off carriage autocannon
			breech.setFireRate(breech.getFireRate() + fireRateAdjustment);
	}

	public int getReferencedFireRate() {
		return this.presentBlockEntities.get(this.startPos) instanceof AbstractAutocannonBreechBlockEntity breech ? breech.getActualFireRate() : 0;
	}

	@Override
	public float getWeightForStress() {
		return this.cannonMaterial == null ? this.blocks.size() : this.blocks.size() * this.cannonMaterial.properties().weight();
	}

	@Override
	public Vec3 getInteractionVec(PitchOrientedContraptionEntity poce) {
		return poce.toGlobalVector(Vec3.atCenterOf(this.startPos), 1);
	}

	@Override
	public CompoundTag writeNBT(boolean clientData) {
		CompoundTag tag = super.writeNBT(clientData);
		tag.putString("AutocannonMaterial", this.cannonMaterial == null ? CBCAutocannonMaterials.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
		if (this.startPos != null) tag.put("StartPos", NbtUtils.writeBlockPos(this.startPos));
		if (this.recoilSpringPos != null) tag.put("RecoilSpringPos", NbtUtils.writeBlockPos(this.recoilSpringPos));
		tag.putBoolean("IsHandle", this.isHandle);
		return tag;
	}

	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = AutocannonMaterial.fromNameOrNull(new ResourceLocation(tag.getString("AutocannonMaterial")));
		if (this.cannonMaterial == null) this.cannonMaterial = CBCAutocannonMaterials.CAST_IRON;
		this.startPos = tag.contains("StartPos") ? NbtUtils.readBlockPos(tag.getCompound("StartPos")) : null;
		this.recoilSpringPos = tag.contains("RecoilSpringPos") ? NbtUtils.readBlockPos(tag.getCompound("RecoilSpringPos")) : null;
		this.isHandle = tag.getBoolean("IsHandle");
	}

	@Override
	public ContraptionType getType() {
		return CBCContraptionTypes.MOUNTED_AUTOCANNON;
	}

	@Override
	public ItemStack insertItemIntoCannon(ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public ItemStack extractItemFromCannon(boolean simulate) {
		return ItemStack.EMPTY;
	}

}
