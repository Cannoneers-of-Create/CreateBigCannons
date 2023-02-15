package rbasamoyai.createbigcannons.cannon_control.contraption;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCContraptionTypes;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.effects.CannonPlumeParticleData;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial.FailureMode;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonEnd;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

import java.util.*;

public class MountedBigCannonContraption extends AbstractMountedCannonContraption {
	
	private BigCannonMaterial cannonMaterial;
	private boolean isWeakBreech = false;

	@Override
	public float maximumDepression(ControlPitchContraption controller) {
		BlockState state = controller.getControllerState();
		if (CBCBlocks.CANNON_MOUNT.has(state)) return 30;
		if (CBCBlocks.CANNON_CARRIAGE.has(state)) return 15;
		return 0;
	}

	@Override
	public float maximumElevation(ControlPitchContraption controller) {
		BlockState state = controller.getControllerState();
		if (CBCBlocks.CANNON_MOUNT.has(state)) return 60;
		if (CBCBlocks.CANNON_CARRIAGE.has(state)) return 30;
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
		
		if (!(startState.getBlock() instanceof BigCannonBlock startCannon)) {
			return false;
		}
		if (!startCannon.isComplete(startState)) {
			throw hasIncompleteCannonBlocks(pos);
		}
		if (this.hasCannonLoaderInside(level, startState, pos)) {
			throw cannonLoaderInsideDuringAssembly(pos);
		}
		BigCannonMaterial material = startCannon.getCannonMaterial();
		BigCannonEnd startEnd = startCannon.getOpeningType(level, startState, pos);
		
		List<StructureBlockInfo> cannonBlocks = new ArrayList<>();
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getTileEntityNBT(level, pos)));
		
		int cannonLength = 1;
		
		Direction cannonFacing = startCannon.getFacing(startState);
		
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, cannonFacing.getAxis());
		Direction negative = positive.getOpposite();
		
		BlockPos start = pos;
		BlockState nextState = level.getBlockState(pos.relative(positive));
		
		BigCannonEnd positiveEnd = startEnd;
		while (this.isValidCannonBlock(level, nextState, start.relative(positive)) && this.isConnectedToCannon(level, nextState, start.relative(positive), positive, material)) {
			start = start.relative(positive);
			
			if (!((BigCannonBlock) nextState.getBlock()).isComplete(nextState)) {
				throw hasIncompleteCannonBlocks(start);
			}
			
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getTileEntityNBT(level, start)));
			cannonLength++;
			
			positiveEnd = ((BigCannonBlock) nextState.getBlock()).getOpeningType(level, nextState, start);
			
			if (this.hasCannonLoaderInside(level, nextState, start)) {
				throw cannonLoaderInsideDuringAssembly(start);
			}
			
			nextState = level.getBlockState(start.relative(positive));
			
			if (cannonLength > getMaxCannonLength()) {
				throw cannonTooLarge();
			}
			if (positiveEnd != BigCannonEnd.OPEN) break;
		}
		BlockPos positiveEndPos = positiveEnd == BigCannonEnd.OPEN ? start : start.relative(negative);
		BlockState positiveEndState = level.getBlockState(start);
		
		start = pos;
		nextState = level.getBlockState(pos.relative(negative));
		
		BigCannonEnd negativeEnd = startEnd;
		while (this.isValidCannonBlock(level, nextState, start.relative(negative)) && this.isConnectedToCannon(level, nextState, start.relative(negative), negative, material)) {
			start = start.relative(negative);
			
			if (!((BigCannonBlock) nextState.getBlock()).isComplete(nextState)) {
				throw hasIncompleteCannonBlocks(start);
			}
			
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getTileEntityNBT(level, start)));
			cannonLength++;
			
			negativeEnd = ((BigCannonBlock) nextState.getBlock()).getOpeningType(level, nextState, start);
			
			if (this.hasCannonLoaderInside(level, nextState, start)) {
				throw cannonLoaderInsideDuringAssembly(start);
			}
			
			nextState = level.getBlockState(start.relative(negative));
			
			if (cannonLength > getMaxCannonLength()) {
				throw cannonTooLarge();
			}
			if (negativeEnd != BigCannonEnd.OPEN) break;
		}
		BlockPos negativeEndPos = negativeEnd == BigCannonEnd.OPEN ? start : start.relative(positive);
		BlockState negativeEndState = level.getBlockState(start);
		
		if (positiveEnd == negativeEnd) {
			throw invalidCannon();
		}
		
		boolean openEndFlag = positiveEnd == BigCannonEnd.OPEN;
		this.initialOrientation = openEndFlag ? positive : negative;
		this.startPos = openEndFlag ? negativeEndPos : positiveEndPos;
		
		this.isWeakBreech = openEndFlag ? negativeEndState.is(CBCTags.BlockCBC.WEAK_CANNON_END) : positiveEndState.is(CBCTags.BlockCBC.WEAK_CANNON_END);
		
		this.isWeakBreech &= CBCConfigs.SERVER.cannons.weakBreechStrength.get() != -1;

		this.anchor = pos;

		this.startPos = this.startPos.subtract(pos);
		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos.subtract(pos);
			StructureBlockInfo localBlockInfo = new StructureBlockInfo(localPos, blockInfo.state, blockInfo.nbt);
			this.getBlocks().put(localPos, localBlockInfo);
			
			if (blockInfo.nbt == null) continue;
			BlockEntity be = BlockEntity.loadStatic(localPos, blockInfo.state, blockInfo.nbt);
			this.presentTileEntities.put(localPos, be);
		}
		this.cannonMaterial = material;
		
		return true;
	}
	
	private boolean isValidCannonBlock(LevelAccessor level, BlockState state, BlockPos pos) {
		return state.getBlock() instanceof BigCannonBlock;
	}
	
	private boolean hasCannonLoaderInside(LevelAccessor level, BlockState state, BlockPos pos) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof IBigCannonBlockEntity cannon)) return false;
		BlockState containedState = cannon.cannonBehavior().block().state;
		return CBCBlocks.RAM_HEAD.has(containedState) || CBCBlocks.WORM_HEAD.has(containedState) || AllBlocks.PISTON_EXTENSION_POLE.has(containedState);
	}
	
	private boolean isConnectedToCannon(LevelAccessor level, BlockState state, BlockPos pos, Direction connection, BigCannonMaterial material) {
		BigCannonBlock cBlock = (BigCannonBlock) state.getBlock();
		if (cBlock.getCannonMaterialInLevel(level, state, pos) != material) return false;
		return level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe
			&& level.getBlockEntity(pos.relative(connection.getOpposite())) instanceof IBigCannonBlockEntity cbe1
			&& cbe.cannonBehavior().isConnectedTo(connection.getOpposite())
			&& cbe1.cannonBehavior().isConnectedTo(connection);
	}
	
	public float getWeightForStress() {
		if (this.cannonMaterial == null) {
			return this.blocks.size();
		}
		return this.blocks.size() * this.cannonMaterial.weight();
	}

	@Override
	public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower) {
		if (!togglePower || firePower <= 0) return;
		this.fireShot(level, entity);
	}

	@Override
	public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
		StructureBlockInfo foundProjectile = null;
		float chargesUsed = 0;
		float smokeScale = 0;
		int barrelTravelled = 0;
		RandomSource rand = level.getRandom();

		boolean failed = false;
		boolean canFail = !CBCConfigs.SERVER.failure.disableAllFailure.get();
		BlockPos currentPos = this.startPos.immutable();
		int count = 0;
		BlockEntity failedEntity = null;

		float spread = 0.0f;
		float spreadAdd = CBCConfigs.SERVER.cannons.powderChargeSpread.getF();
		float spreadSub = CBCConfigs.SERVER.cannons.barrelSpreadReduction.getF();

		boolean emptyNoProjectile = false;

		int weakBreechStrength = CBCConfigs.SERVER.cannons.weakBreechStrength.get();
		int maxSafeCharges = this.isWeakBreech && weakBreechStrength > -1
				? Math.min(this.cannonMaterial.maxSafeCharges(), weakBreechStrength)
				: this.cannonMaterial.maxSafeCharges();

		while (this.presentTileEntities.get(currentPos) instanceof IBigCannonBlockEntity cbe) {
			BigCannonBehavior behavior = cbe.cannonBehavior();
			StructureBlockInfo containedBlockInfo = behavior.block();
			StructureBlockInfo cannonInfo = this.blocks.get(currentPos);

			if (foundProjectile == null && containedBlockInfo.state.isAir()) {
				if (count == 0) return;
				emptyNoProjectile = true;
				chargesUsed = Math.max(chargesUsed - 1, 0);
			} else if (CBCBlocks.POWDER_CHARGE.has(containedBlockInfo.state) && foundProjectile == null) {
				this.consumeBlock(behavior, currentPos);
				++chargesUsed;
				++smokeScale;
				spread += spreadAdd;

				if (canFail && (!cannonInfo.state.is(CBCTags.BlockCBC.THICK_TUBING) && rollBarrelBurst(rand)
						|| chargesUsed > maxSafeCharges && rollOverloadBurst(rand))) {
					failed = true;
					failedEntity = behavior.tileEntity;
					break;
				}
				if (emptyNoProjectile && rollFailToIgnite(rand) && canFail) {
					Vec3 failIgnitePos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
					level.playSound(null, failIgnitePos.x, failIgnitePos.y, failIgnitePos.z, cannonInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 5.0f, 0.0f);
					return;
				}
				emptyNoProjectile = false;
			} else if (containedBlockInfo.state.getBlock() instanceof ProjectileBlock && foundProjectile == null) {
				if (chargesUsed == 0) return;
				foundProjectile = containedBlockInfo;
				if (emptyNoProjectile && rollFailToIgnite(rand) && canFail) {
					Vec3 failIgnitePos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
					level.playSound(null, failIgnitePos.x, failIgnitePos.y, failIgnitePos.z, cannonInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 5.0f, 0.0f);
					return;
				}
				this.consumeBlock(behavior, currentPos);
				emptyNoProjectile = false;
			} else if (!containedBlockInfo.state.isAir() && foundProjectile != null) {
				if (canFail) {
					failed = true;
					failedEntity = behavior.tileEntity;
					break;
				} else {
					this.consumeBlock(behavior, currentPos);
				}
			}

			currentPos = currentPos.relative(this.initialOrientation);

			BlockState cannonState = cannonInfo.state;
			if (cannonState.getBlock() instanceof BigCannonBlock cannon && cannon.getOpeningType(level, cannonState, currentPos) == BigCannonEnd.OPEN) {
				++count;
			}

			if (foundProjectile != null) {
				++barrelTravelled;
				if (cannonInfo.state.is(CBCTags.BlockCBC.REDUCES_SPREAD)) {
					spread = Math.max(spread - spreadSub, 0.0f);
				}

				if (chargesUsed > 0 && (double) barrelTravelled / (double) chargesUsed > this.cannonMaterial.squibRatio() && rollSquib(rand)) {
					behavior.loadBlock(foundProjectile);
					CompoundTag tag = behavior.tileEntity.saveWithFullMetadata();
					tag.remove("x");
					tag.remove("y");
					tag.remove("z");
					StructureBlockInfo squibInfo = new StructureBlockInfo(cannonInfo.pos, cannonInfo.state, tag);
					this.blocks.put(cannonInfo.pos, squibInfo);

					Vec3 squibPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
					level.playSound(null, squibPos.x, squibPos.y, squibPos.z, cannonInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 10.0f, 0.0f);
					return;
				}
			}
		}
		if (canFail && failed && failedEntity != null) {
			this.fail(currentPos, level, entity, failedEntity, (int) chargesUsed);
			return;
		}

		if (chargesUsed <= 0) {
			chargesUsed = 0.5f;
		}

		Vec3 spawnPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
		Vec3 vec = spawnPos.subtract(entity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 1.0f)).normalize();

		if (foundProjectile != null && foundProjectile.state.getBlock() instanceof ProjectileBlock projectileBlock) {
			BlockEntity projectileBE = foundProjectile.nbt == null ? null : BlockEntity.loadStatic(foundProjectile.pos, foundProjectile.state, foundProjectile.nbt);
			AbstractCannonProjectile projectile = projectileBlock.getProjectile(level, foundProjectile.state, foundProjectile.pos, projectileBE);
			projectile.setPos(spawnPos);
			projectile.setChargePower(chargesUsed);
			projectile.shoot(vec.x, vec.y, vec.z, chargesUsed, spread);
			projectile.xRotO = projectile.getXRot();
			projectile.yRotO = projectile.getYRot();
			level.addFreshEntity(projectile);
		}

		for (ServerPlayer player : level.players()) {
			level.sendParticles(player, new CannonPlumeParticleData(smokeScale * 0.5f), true, spawnPos.x, spawnPos.y, spawnPos.z, 0, vec.x, vec.y, vec.z, 1.0f);
		}
		level.playSound(null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 20.0f, 0.0f);
	}

	private void consumeBlock(BigCannonBehavior behavior, BlockPos pos) {
		behavior.removeBlock();
		CompoundTag tag = behavior.tileEntity.saveWithFullMetadata();
		tag.remove("x");
		tag.remove("y");
		tag.remove("z");

		StructureBlockInfo oldInfo = this.blocks.get(pos);
		if (oldInfo == null) return;
		StructureBlockInfo consumedInfo = new StructureBlockInfo(oldInfo.pos, oldInfo.state, tag);
		this.blocks.put(oldInfo.pos, consumedInfo);
	}
	
	private static boolean rollSquib(RandomSource random) {
		float f = CBCConfigs.SERVER.failure.squibChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}
	
	private static boolean rollBarrelBurst(RandomSource random) {
		float f = CBCConfigs.SERVER.failure.barrelChargeBurstChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}
	
	private static boolean rollOverloadBurst(RandomSource random) {
		float f = CBCConfigs.SERVER.failure.overloadBurstChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}
	
	private static boolean rollFailToIgnite(RandomSource random) {
		float f = CBCConfigs.SERVER.failure.interruptedIgnitionChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}
	
	public void fail(BlockPos localPos, Level level, PitchOrientedContraptionEntity entity, BlockEntity failed, int charges) {
		Vec3 failurePoint = entity.toGlobalVector(Vec3.atCenterOf(failed.getBlockPos()), 1.0f);
		float failScale = CBCConfigs.SERVER.failure.failureExplosionPower.getF();
		if (this.cannonMaterial.failureMode() == FailureMode.RUPTURE) {
			level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, 2 * failScale + 1, Explosion.BlockInteraction.NONE);
			int failInt = Mth.ceil(failScale);
			BlockPos startPos = localPos.relative(this.initialOrientation.getOpposite(), failInt);
			for (int i = 0; i < failInt * 2 + 1; ++i) {
				BlockPos pos = startPos.relative(this.initialOrientation, i);
				this.blocks.remove(pos);
				this.presentTileEntities.remove(pos);
			}

			ControlPitchContraption controller = entity.getController();
			if (controller != null) controller.disassemble();
		} else {
			for (Iterator<Map.Entry<BlockPos, StructureBlockInfo>> iter = this.blocks.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry<BlockPos, StructureBlockInfo> entry = iter.next();
				this.presentTileEntities.remove(entry.getKey());
				iter.remove();
			}
			
			float power = (float) charges * failScale;
			level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, power, Explosion.BlockInteraction.DESTROY);
			entity.discard();
		}
	}
	
	@Override
	public CompoundTag writeNBT(boolean clientData) {
		CompoundTag tag = super.writeNBT(clientData);
		tag.putString("CannonMaterial", this.cannonMaterial == null ? BigCannonMaterial.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
		tag.putBoolean("WeakBreech", this.isWeakBreech);
		return tag;
	}
	
	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = BigCannonMaterial.fromName(new ResourceLocation(tag.getString("CannonMaterial")));
		this.isWeakBreech = tag.getBoolean("WeakBreech");
	}
	
	@Override protected ContraptionType getType() { return CBCContraptionTypes.MOUNTED_CANNON; }
	
}
