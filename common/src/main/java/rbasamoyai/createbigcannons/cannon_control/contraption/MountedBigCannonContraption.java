package rbasamoyai.createbigcannons.cannon_control.contraption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.effects.CannonPlumeParticleData;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.BigCannonBreechStrengthHandler;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialProperties;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBigCannonMaterials;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCContraptionTypes;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.DropMortarMunition;
import rbasamoyai.createbigcannons.munitions.big_cannon.DropMortarProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.IntegratedPropellantProjectile;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilities;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilityHandler;
import rbasamoyai.createbigcannons.network.ClientboundAddShakeEffectPacket;

public class MountedBigCannonContraption extends AbstractMountedCannonContraption {

	private BigCannonMaterial cannonMaterial;
	public boolean hasFired = false;
	public boolean hasWeldedPenalty = false;

	protected int mortarDelay = 0;
	protected ItemStack cachedMortarRound = ItemStack.EMPTY;

	@Override
	public float maximumDepression(@Nonnull ControlPitchContraption controller) {
		if (this.isDropMortar()) return -15;
		BlockState state = controller.getControllerState();
		if (CBCBlocks.CANNON_MOUNT.has(state)) return 30;
		if (CBCBlocks.CANNON_CARRIAGE.has(state)) return 15;
		return 0;
	}

	@Override
	public float maximumElevation(@Nonnull ControlPitchContraption controller) {
		if (this.isDropMortar()) return 85;
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
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getBlockEntityNBT(level, pos)));

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

			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
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

		start = pos;
		nextState = level.getBlockState(pos.relative(negative));

		BigCannonEnd negativeEnd = startEnd;
		while (this.isValidCannonBlock(level, nextState, start.relative(negative)) && this.isConnectedToCannon(level, nextState, start.relative(negative), negative, material)) {
			start = start.relative(negative);

			if (!((BigCannonBlock) nextState.getBlock()).isComplete(nextState)) {
				throw hasIncompleteCannonBlocks(start);
			}

			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
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

		if (positiveEnd == negativeEnd) {
			throw invalidCannon();
		}

		boolean openEndFlag = positiveEnd == BigCannonEnd.OPEN;
		this.initialOrientation = openEndFlag ? positive : negative;
		this.startPos = openEndFlag ? negativeEndPos : positiveEndPos;
		this.anchor = pos;

		this.startPos = this.startPos.subtract(pos);
		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos.subtract(pos);
			StructureBlockInfo localBlockInfo = new StructureBlockInfo(localPos, blockInfo.state, blockInfo.nbt);
			this.getBlocks().put(localPos, localBlockInfo);

			if (blockInfo.nbt == null) continue;
			BlockEntity be = BlockEntity.loadStatic(localPos, blockInfo.state, blockInfo.nbt);
			this.presentBlockEntities.put(localPos, be);
			if (be instanceof IBigCannonBlockEntity cbe && cbe.cannonBehavior().isWelded()) this.hasWeldedPenalty = true;
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
		return this.blocks.size() * this.cannonMaterial.properties().weight();
	}

	@Override
	public void tick(Level level, PitchOrientedContraptionEntity entity) {
		super.tick(level, entity);

		BlockPos endPos = this.startPos.relative(this.initialOrientation.getOpposite());
		if (this.presentBlockEntities.get(endPos) instanceof QuickfiringBreechBlockEntity qfbreech)
			qfbreech.tickAnimation();
		if (!level.isClientSide && this.isDropMortar() && this.mortarDelay > 0) {
			--this.mortarDelay;
			if (this.mortarDelay == 0) this.actuallyFireDropMortar();
		}
	}

	@Override
	public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower, ControlPitchContraption controller) {
		if (!togglePower || firePower <= 0) return;
		this.fireShot(level, entity);
	}

	public int getMaxSafeCharges() {
		BigCannonMaterialProperties properties = this.cannonMaterial.properties();
		StructureBlockInfo breech = this.blocks.get(this.startPos.relative(this.initialOrientation.getOpposite()));
		if (breech == null) return 0;
		int materialStrength = properties.maxSafePropellantStress();
		int maxSafeCharges = Math.min(materialStrength, BigCannonBreechStrengthHandler.getStrength(breech.state.getBlock(), materialStrength));
		if (this.hasWeldedPenalty) maxSafeCharges -= properties.weldStressPenalty();
		return Math.max(maxSafeCharges, 0);
	}

	@Override
	public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
		BlockPos endPos = this.startPos.relative(this.initialOrientation.getOpposite());
		if (this.presentBlockEntities.get(endPos) instanceof QuickfiringBreechBlockEntity qfbreech && qfbreech.getOpenProgress() > 0)
			return;
		if (this.isDropMortar()) return;

		ControlPitchContraption controller = entity.getController();

		RandomSource rand = level.getRandom();
		BlockPos currentPos = this.startPos.immutable();
		int count = 0;
		int maxSafeCharges = this.getMaxSafeCharges();
		boolean canFail = !CBCConfigs.SERVER.failure.disableAllFailure.get();
		float spreadSub = CBCConfigs.SERVER.cannons.barrelSpreadReduction.getF();
		boolean emptyNoProjectile = false;

		PropellantContext propelCtx = new PropellantContext();

		List<StructureBlockInfo> projectileBlocks = new ArrayList<>();
		AbstractBigCannonProjectile<?> projectile = null;
		BlockPos assemblyPos = null;

		float minimumSpread = CBCConfigs.SERVER.cannons.minimumBigCannonSpread.getF();

		while (this.presentBlockEntities.get(currentPos) instanceof IBigCannonBlockEntity cbe) {
			BigCannonBehavior behavior = cbe.cannonBehavior();
			StructureBlockInfo containedBlockInfo = behavior.block();
			StructureBlockInfo cannonInfo = this.blocks.get(currentPos);
			if (cannonInfo == null) break;

			Block block = containedBlockInfo.state.getBlock();

			if (containedBlockInfo.state.isAir()) {
				if (count == 0) return;
				if (projectile == null) {
					if (projectileBlocks.isEmpty()) {
						emptyNoProjectile = true;
						propelCtx.chargesUsed = Math.max(propelCtx.chargesUsed - 1, 0);
					} else if (canFail) {
						this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
						return;
					}
				} else {
					++propelCtx.barrelTravelled;
					if (cannonInfo.state.is(CBCTags.CBCBlockTags.REDUCES_SPREAD)) {
						propelCtx.spread = Math.max(propelCtx.spread - spreadSub, minimumSpread);
					}
					if (projectile.canSquib() && this.cannonMaterial.properties().mayGetStuck(propelCtx.chargesUsed, propelCtx.barrelTravelled) && rollSquib(rand)) {
						this.squibBlocks(currentPos, projectileBlocks);
						Vec3 squibPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
						level.playSound(null, squibPos.x, squibPos.y, squibPos.z, cannonInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 10.0f, 0.0f);
						return;
					}
				}
			} else if (block instanceof BigCannonPropellantBlock<?> cpropel && !(block instanceof ProjectileBlock)) {
				if (!cpropel.canBeIgnited(containedBlockInfo, this.initialOrientation)) return;
				if (!propelCtx.addPropellant(cpropel, containedBlockInfo, this.initialOrientation) && canFail) {
					this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				}
				this.consumeBlock(behavior, currentPos, cpropel::consumePropellant);
				if (canFail && (!cbe.blockCanHandle(cannonInfo) && rollBarrelBurst(rand)
					|| propelCtx.stress > maxSafeCharges && rollOverloadBurst(rand))) {
					this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				}
				if (emptyNoProjectile && canFail && rollFailToIgnite(rand)) {
					Vec3 failIgnitePos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
					level.playSound(null, failIgnitePos.x, failIgnitePos.y, failIgnitePos.z, cannonInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 5.0f, 0.0f);
					return;
				}
				emptyNoProjectile = false;
			} else if (block instanceof ProjectileBlock<?> projBlock && projectile == null) {
				if (canFail && emptyNoProjectile && rollFailToIgnite(rand)) {
					Vec3 failIgnitePos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
					level.playSound(null, failIgnitePos.x, failIgnitePos.y, failIgnitePos.z, cannonInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 5.0f, 0.0f);
					return;
				}
				projectileBlocks.add(containedBlockInfo);
				if (assemblyPos == null) assemblyPos = currentPos.immutable();

				List<StructureBlockInfo> copy = ImmutableList.copyOf(projectileBlocks);
				for (ListIterator<StructureBlockInfo> projIter = projectileBlocks.listIterator(); projIter.hasNext(); ) {
					int i = projIter.nextIndex();
					StructureBlockInfo projInfo = projIter.next();
					if (projInfo.state.getBlock() instanceof ProjectileBlock<?> cproj1 && cproj1.isValidAddition(copy, projInfo, i, this.initialOrientation)) continue;
					if (canFail) this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				}
				this.consumeBlock(behavior, currentPos);
				if (projBlock.isComplete(projectileBlocks, this.initialOrientation)) {
					projectile = projBlock.getProjectile(level, projectileBlocks);
					propelCtx.chargesUsed += projectile.addedChargePower();
					if (propelCtx.chargesUsed < projectile.minimumChargePower()) {
						if (assemblyPos != null) this.squibBlocks(assemblyPos, projectileBlocks);
						return;
					}
				}
				emptyNoProjectile = false;
			} else {
				if (canFail) {
					this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				} else {
					this.consumeBlock(behavior, currentPos);
				}
			}
			currentPos = currentPos.relative(this.initialOrientation);
			BlockState cannonState = cannonInfo.state;
			if (cannonState.getBlock() instanceof BigCannonBlock cannon && cannon.getOpeningType(level, cannonState, currentPos) == BigCannonEnd.OPEN) {
				++count;
			}
		}
		if (projectile == null && !projectileBlocks.isEmpty()) {
			StructureBlockInfo info = projectileBlocks.get(0);
			if (!(info.state.getBlock() instanceof ProjectileBlock<?> projBlock)) {
				if (canFail) this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
				return;
			}
			int remaining = projBlock.getExpectedSize() - projectileBlocks.size();
			if (remaining < 1) {
				if (canFail) this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
				return;
			}
			for (int i = 0; i < remaining; ++i) {
				StructureBlockInfo additionalInfo = this.blocks.remove(currentPos);
				if (additionalInfo == null) {
					if (canFail) this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
					return;
				}
				projectileBlocks.add(additionalInfo);
				if (assemblyPos == null) assemblyPos = currentPos.immutable();

				List<StructureBlockInfo> copy = ImmutableList.copyOf(projectileBlocks);
				for (ListIterator<StructureBlockInfo> projIter = projectileBlocks.listIterator(); projIter.hasNext(); ) {
					int j = projIter.nextIndex();
					StructureBlockInfo projInfo = projIter.next();
					if (projInfo.state.getBlock() instanceof ProjectileBlock<?> cproj1 && cproj1.isValidAddition(copy, projInfo, j, this.initialOrientation)) continue;
					if (canFail) this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
					return;
				}
				currentPos = currentPos.relative(this.initialOrientation);
			}
			if (projBlock.isComplete(projectileBlocks, this.initialOrientation)) {
				projectile = projBlock.getProjectile(level, projectileBlocks);
				propelCtx.chargesUsed += projectile.addedChargePower();
				if (propelCtx.chargesUsed < projectile.minimumChargePower()) {
					if (assemblyPos != null) this.squibBlocks(assemblyPos, projectileBlocks);
					return;
				}
			} else if (canFail) {
				this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
				return;
			}
		}

		Vec3 spawnPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
		Vec3 vec = spawnPos.subtract(entity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 1.0f)).normalize();

		if (propelCtx.chargesUsed < minimumSpread) propelCtx.chargesUsed = minimumSpread;

		float recoilMagnitude = 0;

		if (projectile != null) {
			if (projectile instanceof IntegratedPropellantProjectile integPropel && !projectileBlocks.isEmpty()) {
				if (!propelCtx.addIntegratedPropellant(integPropel, projectileBlocks.get(0), this.initialOrientation) && canFail) {
					this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
					return;
				}
			}
			StructureBlockInfo muzzleInfo = this.blocks.get(currentPos);
			if (canFail && muzzleInfo != null && !muzzleInfo.state.isAir()) {
				this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
				return;
			}
			projectile.setPos(spawnPos);
			projectile.setChargePower(propelCtx.chargesUsed);
			projectile.shoot(vec.x, vec.y, vec.z, propelCtx.chargesUsed, propelCtx.spread);
			projectile.xRotO = projectile.getXRot();
			projectile.yRotO = projectile.getYRot();
			level.addFreshEntity(projectile);
			recoilMagnitude += projectile.addedRecoil();
		}

		recoilMagnitude += propelCtx.recoil;
		recoilMagnitude *= CBCConfigs.SERVER.cannons.bigCannonRecoilScale.getF();
		if (controller != null) controller.onRecoil(vec.scale(-recoilMagnitude), entity);

		this.hasFired = true;

		float shakeDistance = propelCtx.chargesUsed * 6;
		Vec3 plumePos = spawnPos.subtract(vec.scale(0.5));
		for (ServerPlayer player : level.players()) {
			level.sendParticles(player, new CannonPlumeParticleData(propelCtx.smokeScale * 0.25f), true, plumePos.x, plumePos.y, plumePos.z, 0, vec.x, vec.y, vec.z, 1.0f);
			float dist = player.distanceTo(entity);
			if (dist < shakeDistance && shakeDistance > 0.1f) {
				float f = 1 - dist / shakeDistance;
				float f2 = f * f;
				float shake = Math.min(45, shakeDistance * f2);
				NetworkPlatform.sendToClientPlayer(new ClientboundAddShakeEffectPacket(level.random.nextInt(10000), 50, shake), player);
			}
		}
		CBCSoundEvents.FIRE_BIG_CANNON.playOnServer(level, new BlockPos(spawnPos));
	}

	private void consumeBlock(BigCannonBehavior behavior, BlockPos pos) {
		this.consumeBlock(behavior, pos, BigCannonBehavior::removeBlock);
	}

	private void consumeBlock(BigCannonBehavior behavior, BlockPos pos, Consumer<BigCannonBehavior> action) {
		action.accept(behavior);
		CompoundTag tag = behavior.blockEntity.saveWithFullMetadata();
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

	private void squibBlocks(BlockPos currentPos, List<StructureBlockInfo> projectileBlocks) {
		for (int i = 0; i < projectileBlocks.size(); ++i) {
			BlockPos pos = currentPos.relative(this.initialOrientation, i);
			StructureBlockInfo cannonInfo1 = this.blocks.get(pos);
			BlockEntity be1 = this.presentBlockEntities.get(pos);
			StructureBlockInfo projBlock = projectileBlocks.get(i);

			if (cannonInfo1 != null && be1 instanceof IBigCannonBlockEntity cbe1) {
				BigCannonBehavior behavior1 = cbe1.cannonBehavior();
				behavior1.loadBlock(projBlock);
				CompoundTag tag = behavior1.blockEntity.saveWithFullMetadata();
				tag.remove("x");
				tag.remove("y");
				tag.remove("z");
				StructureBlockInfo squibInfo = new StructureBlockInfo(cannonInfo1.pos, cannonInfo1.state, tag);
				this.blocks.put(cannonInfo1.pos, squibInfo);
			} else {
				CompoundTag tag = projBlock.nbt;
				if (tag != null) {
					tag.remove("x");
					tag.remove("y");
					tag.remove("z");
				}
				this.blocks.put(pos, new StructureBlockInfo(pos, projBlock.state, tag));
			}
		}
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

	public void fail(BlockPos localPos, Level level, PitchOrientedContraptionEntity entity, @Nullable BlockEntity failed, int charges) {
		Vec3 failurePoint = entity.toGlobalVector(Vec3.atCenterOf(localPos), 1.0f);
		float failScale = CBCConfigs.SERVER.failure.failureExplosionPower.getF();
		if (this.cannonMaterial.properties().failureMode() == BigCannonMaterialProperties.FailureMode.RUPTURE) {
			level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, 2 * failScale + 1, Explosion.BlockInteraction.NONE);
			int failInt = Mth.ceil(failScale);
			BlockPos startPos = localPos.relative(this.initialOrientation.getOpposite(), failInt);
			for (int i = 0; i < failInt * 2 + 1; ++i) {
				BlockPos pos = startPos.relative(this.initialOrientation, i);
				this.blocks.remove(pos);
				this.presentBlockEntities.remove(pos);
			}

			ControlPitchContraption controller = entity.getController();
			if (controller != null) controller.disassemble();
		} else {
			for (Iterator<Map.Entry<BlockPos, StructureBlockInfo>> iter = this.blocks.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry<BlockPos, StructureBlockInfo> entry = iter.next();
				this.presentBlockEntities.remove(entry.getKey());
				iter.remove();
			}

			float power = (float) charges * failScale;
			level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, power, Explosion.BlockInteraction.DESTROY);
			entity.discard();
		}
	}

	@Override
	public Vec3 getInteractionVec(PitchOrientedContraptionEntity poce) {
		return poce.toGlobalVector(Vec3.atCenterOf(this.startPos.relative(this.initialOrientation.getOpposite())), 1);
	}

	@Override
	public CompoundTag writeNBT(boolean clientData) {
		CompoundTag tag = super.writeNBT(clientData);
		tag.putString("CannonMaterial", this.cannonMaterial == null ? CBCBigCannonMaterials.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
		if (this.hasWeldedPenalty) tag.putBoolean("WeldedCannon", true);
		if (this.mortarDelay > 0) tag.putInt("MortarDelay", this.mortarDelay);
		if (this.cachedMortarRound != null && !this.cachedMortarRound.isEmpty()) tag.put("CachedMortarRound", this.cachedMortarRound.save(new CompoundTag()));
		return tag;
	}

	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = BigCannonMaterial.fromNameOrNull(new ResourceLocation(tag.getString("CannonMaterial")));
		this.hasWeldedPenalty = tag.contains("WeldedCannon");
		if (this.cannonMaterial == null) this.cannonMaterial = CBCBigCannonMaterials.CAST_IRON;
		this.mortarDelay = Math.max(0, tag.getInt("MortarDelay"));
		this.cachedMortarRound = tag.contains("CachedMortarRound", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("CachedMortarRound")) : ItemStack.EMPTY;
	}

	@Override
	public ContraptionType getType() {
		return CBCContraptionTypes.MOUNTED_CANNON;
	}

	public boolean isDropMortar() {
		StructureBlockInfo breech = this.blocks.get(this.startPos.relative(this.initialOrientation.getOpposite()));
		return breech != null && breech.state.getBlock() instanceof BigCannonBlock cblock && cblock.getCannonShape() == CannonCastShape.DROP_MORTAR_END;
	}

	public boolean tryDroppingMortarRound(ItemStack stack) {
		if (!this.isDropMortar() || this.cachedMortarRound != null && !this.cachedMortarRound.isEmpty()
			|| !(Block.byItem(stack.getItem()) instanceof DropMortarMunition)) return false;
		BlockPos currentPos = this.startPos.immutable();
		while (this.presentBlockEntities.get(currentPos) instanceof IBigCannonBlockEntity cbe) {
			BigCannonBehavior behavior = cbe.cannonBehavior();
			if (!behavior.block().state.isAir()) return false;
			currentPos = currentPos.relative(this.initialOrientation);
		}
		this.cachedMortarRound = stack.copy();
		this.mortarDelay = CBCConfigs.SERVER.cannons.dropMortarDelay.get();
		if (this.mortarDelay <= 0) this.actuallyFireDropMortar();
		return true;
	}

	public void actuallyFireDropMortar() {
		if (!(this.entity instanceof PitchOrientedContraptionEntity poce) || !(poce.level instanceof ServerLevel slevel)) return;
		ItemStack stack = this.cachedMortarRound;
		this.cachedMortarRound = ItemStack.EMPTY;
		if (!(Block.byItem(stack.getItem()) instanceof DropMortarMunition munition)) return;

		ControlPitchContraption controller = poce.getController();
		AbstractBigCannonProjectile<? extends DropMortarProjectileProperties> projectile = munition.getProjectile(slevel, stack);

		BlockPos currentPos = this.startPos.immutable();
		while (this.presentBlockEntities.get(currentPos) instanceof IBigCannonBlockEntity cbe) {
			BigCannonBehavior behavior = cbe.cannonBehavior();
			if (!behavior.block().state.isAir()) return;
			currentPos = currentPos.relative(this.initialOrientation);
		}

		DropMortarProjectileProperties properties = projectile.getProperties();
		float recoilMagnitude = properties == null ? 1 : properties.mortarRecoil();
		float power = properties == null ? 3 : properties.mortarPower();
		float spread = properties == null ? 0.1f : properties.mortarSpread();

		Vec3 spawnPos = this.entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
		Vec3 vec = spawnPos.subtract(this.entity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 1.0f)).normalize();

		projectile.setPos(spawnPos);
		projectile.setChargePower(power);
		projectile.shoot(vec.x, vec.y, vec.z, power, spread);
		projectile.xRotO = projectile.getXRot();
		projectile.yRotO = projectile.getYRot();
		slevel.addFreshEntity(projectile);

		recoilMagnitude *= CBCConfigs.SERVER.cannons.bigCannonRecoilScale.getF();
		if (controller != null) controller.onRecoil(vec.scale(-recoilMagnitude), entity);

		Vec3 plumePos = spawnPos.subtract(vec.scale(0.5));
		for (ServerPlayer player : slevel.players()) {
			slevel.sendParticles(player, new CannonPlumeParticleData(0.2f), true, plumePos.x, plumePos.y, plumePos.z, 0, vec.x, vec.y, vec.z, 1.0f);
		}
		CBCSoundEvents.FIRE_BIG_CANNON.playOnServer(slevel, new BlockPos(spawnPos));
		this.hasFired = true;
	}

	protected static class PropellantContext {
		public float chargesUsed = 0;
		public float recoil = 0;
		public float stress = 0;
		public float smokeScale = 0;
		public int barrelTravelled = 0;
		public float spread = 0.0f;
		public List<StructureBlockInfo> propellantBlocks = new ArrayList<>();

		public boolean addPropellant(BigCannonPropellantBlock<?> propellant, StructureBlockInfo info, Direction initialOrientation) {
			this.propellantBlocks.add(info);
			if (!safeLoad(ImmutableList.copyOf(this.propellantBlocks), initialOrientation)) return false;
			float power = Math.max(0, propellant.getChargePower(info));
			this.chargesUsed += power;
			this.smokeScale += power;
			this.recoil = Math.max(0, propellant.getRecoil(info));
			this.stress += propellant.getStressOnCannon(info);
			this.spread += propellant.getSpread(info);
			return true;
		}

		public boolean addIntegratedPropellant(IntegratedPropellantProjectile propellant, StructureBlockInfo firstInfo, Direction initialOrientation) {
			List<StructureBlockInfo> copy = ImmutableList.<StructureBlockInfo>builder().addAll(this.propellantBlocks).add(firstInfo).build();
			if (!safeLoad(copy, initialOrientation)) return false;
			float power = Math.max(0, propellant.getChargePower());
			this.chargesUsed += power;
			this.smokeScale += power;
			this.stress += propellant.getStressOnCannon();
			this.spread += propellant.getSpread();
			return true;
		}

		public static boolean safeLoad(List<StructureBlockInfo> propellant, Direction orientation) {
			Map<Block, Integer> allowedCounts = new HashMap<>();
			Map<Block, Integer> actualCounts = new HashMap<>();
			for (ListIterator<StructureBlockInfo> iter = propellant.listIterator(); iter.hasNext(); ) {
				int index = iter.nextIndex();
				StructureBlockInfo info = iter.next();

				Block block = info.state.getBlock();
				if (!(block instanceof BigCannonPropellantBlock<?> cpropel) || !(cpropel.isValidAddition(info, index, orientation))) return false;
				if (actualCounts.containsKey(block)) {
					actualCounts.put(block, actualCounts.get(block) + 1);
				} else {
					actualCounts.put(block, 1);
				}
				BigCannonPropellantCompatibilities compatibilities = BigCannonPropellantCompatibilityHandler.getCompatibilities(block);
				for (Map.Entry<Block, Integer> entry : compatibilities.validPropellantCounts().entrySet()) {
					Block block1 = entry.getKey();
					int oldCount = allowedCounts.getOrDefault(block1, -1);
					int newCount = entry.getValue();
					if (newCount >= 0 && (oldCount < 0 || newCount < oldCount)) allowedCounts.put(block1, newCount);
				}
			}
			for (Map.Entry<Block, Integer> entry : actualCounts.entrySet()) {
				Block block = entry.getKey();
				if (allowedCounts.containsKey(block) && allowedCounts.get(block) < entry.getValue()) return false;
			}
			return true;
		}
	}

}
