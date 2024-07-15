package rbasamoyai.createbigcannons.munitions.big_cannon;

import javax.annotation.Nonnull;

import com.mojang.math.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.TrailSmokeParticleData;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.ImpactExplosion;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.network.ClientboundPlayBlockHitEffectPacket;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public abstract class AbstractBigCannonProjectile extends AbstractCannonProjectile {

	private static final EntityDataAccessor<ItemStack> TRACER = SynchedEntityData.defineId(AbstractBigCannonProjectile.class, EntityDataSerializers.ITEM_STACK);

	protected AbstractBigCannonProjectile(EntityType<? extends AbstractBigCannonProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(TRACER, ItemStack.EMPTY);
	}

	@Override
	public void tick() {
		ChunkPos cpos = new ChunkPos(this.blockPosition());
		if (this.level.isClientSide || this.level.hasChunk(cpos.x, cpos.z)) {
			Vec3 oldPos = this.position();
			super.tick();
			if (!this.isInGround()) {
				TrailType trailType = CBCConfigs.SERVER.munitions.bigCannonTrailType.get();
				if (trailType != TrailType.NONE) {
					int lifetime = trailType == TrailType.SHORT ? 100 : 280 + this.level.random.nextInt(50);
					ParticleOptions options = new TrailSmokeParticleData(lifetime);
					for (int i = 0; i < 10; ++i) {
						double partial = i * 0.1f;
						double dx = Mth.lerp(partial, this.xOld, this.getX());
						double dy = Mth.lerp(partial, this.yOld, this.getY());
						double dz = Mth.lerp(partial, this.zOld, this.getZ());
						double sx = this.level.random.nextDouble() * 0.004d - 0.002d;
						double sy = this.level.random.nextDouble() * 0.004d - 0.002d;
						double sz = this.level.random.nextDouble() * 0.004d - 0.002d;
						this.level.addAlwaysVisibleParticle(options, true, dx, dy, dz, sx, sy, sz);
					}
				}
				Vec3 newPos = this.position();
				if (this.level.isClientSide && this.localSoundCooldown == 0) {
					Vec3 displacement = newPos.subtract(oldPos);
					double dispLen = displacement.length();
					Vec3 originPos = newPos.subtract(displacement.scale(0.5));
					double radius = Math.min(200, dispLen * 30);
					EnvExecute.executeOnClient(() -> () -> CBCClientCommon.playShellFlyingSoundOnClient(this,
						CBCSoundEvents.SHELL_FLYING.getMainEvent(), player -> {
							if (!CBCConfigs.CLIENT.enableBigCannonFlybySounds.get())
								return false;
							if (player.distanceToSqr(originPos) > radius * radius)
								return false;
							Vec3 diff = player.position().subtract(originPos);
							return displacement.normalize().dot(diff.normalize()) >= 0;
						}, radius));
				}
			}
		}
	}


	public boolean hasTracer() {
		return (!this.getTracer().isEmpty() || CBCConfigs.SERVER.munitions.allBigCannonProjectilesAreTracers.get()) && !this.isInGround();
	}

	public void setTracer(ItemStack stack) {
		this.entityData.set(TRACER, stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack);
	}

	public ItemStack getTracer() { return this.entityData.get(TRACER); }

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (!this.getTracer().isEmpty())
			tag.put("Tracer", this.getTracer().save(new CompoundTag()));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setTracer(tag.contains("Tracer", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Tracer")) : ItemStack.EMPTY);
	}

	@Override
	protected void onTickRotate() {
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();

		if (!this.isInGround()) {
			Vec3 vel = this.getDeltaMovement();
			if (vel.lengthSqr() > 0.005d) {
				this.setYRot((float) (Mth.atan2(vel.x, vel.z) * (double) Constants.RAD_TO_DEG));
				this.setXRot((float) (Mth.atan2(vel.y, vel.horizontalDistance()) * (double) Constants.RAD_TO_DEG));
			}

			this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
			this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
		}
	}

	public abstract BlockState getRenderedBlockState();

	@Override
	protected ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state, BlockHitResult blockHitResult) {
		BlockPos pos = blockHitResult.getBlockPos();
		Vec3 hitLoc = blockHitResult.getLocation();

		BallisticPropertiesComponent ballistics = this.getBallisticProperties();
		BlockArmorPropertiesProvider blockArmor = BlockArmorPropertiesHandler.getProperties(state);
		boolean unbreakable = projectileContext.griefState() == CBCCfgMunitions.GriefState.NO_DAMAGE || state.getDestroySpeed(this.level, pos) == -1;

		Vec3 accel = this.getForces(this.position(), this.getDeltaMovement());
		Vec3 curVel = this.getDeltaMovement().add(accel);

		Vec3 normal = CBCUtils.getSurfaceNormalVector(this.level, blockHitResult);
		double incidence = Math.max(0, curVel.normalize().dot(normal.reverse()));
		double velMag = curVel.length();
		double mass = this.getProjectileMass();
		double bonusMomentum = 1 + Math.max(0, (velMag - CBCConfigs.SERVER.munitions.minVelocityForPenetrationBonus.getF())
			* CBCConfigs.SERVER.munitions.penetrationBonusScale.getF());
		double incidentVel = velMag * incidence;
		double momentum = mass * incidentVel * bonusMomentum;

		double toughness = blockArmor.toughness(this.level, state, pos, true);
		double toughnessPenalty = toughness - momentum;
		double hardnessPenalty = blockArmor.hardness(this.level, state, pos, true) - ballistics.penetration();
		double bounceBonus = Math.max(1 - hardnessPenalty, 0);

		double projectileDeflection = ballistics.deflection();
		double baseChance = CBCConfigs.SERVER.munitions.baseProjectileBounceChance.getF();
		double bounceChance = projectileDeflection < 1e-2d || incidence > projectileDeflection ? 0 : Math.max(baseChance, 1 - incidence / projectileDeflection) * bounceBonus;

		boolean surfaceImpact = this.canHitSurface();
		boolean canBounce = CBCConfigs.SERVER.munitions.projectilesCanBounce.get();
		boolean blockBroken = toughnessPenalty < 1e-2d && !unbreakable;
		ImpactResult.KinematicOutcome outcome;
		if (surfaceImpact && canBounce && this.level.getRandom().nextDouble() < bounceChance) {
			outcome = ImpactResult.KinematicOutcome.BOUNCE;
		} else if (blockBroken && !this.level.isClientSide) {
			outcome = ImpactResult.KinematicOutcome.PENETRATE;
		} else {
			outcome = ImpactResult.KinematicOutcome.STOP;
		}
		boolean shatter = surfaceImpact && outcome != ImpactResult.KinematicOutcome.BOUNCE && hardnessPenalty > ballistics.toughness();
		float durabilityPenalty = ((float) Math.max(0, hardnessPenalty) + 1) * (float) toughness / (float) incidentVel;

		state.onProjectileHit(this.level, state, blockHitResult, this);
		if (!this.level.isClientSide) {
			boolean bounced = outcome == ImpactResult.KinematicOutcome.BOUNCE;
			Vec3 effectNormal;
			if (bounced) {
				double elasticity = 1.7f;
				effectNormal = curVel.subtract(normal.scale(normal.dot(curVel) * elasticity));
			} else {
				effectNormal = curVel.reverse();
			}
			for (BlockState state1 : blockArmor.containedBlockStates(this.level, state, pos.immutable(), true)) {
				projectileContext.addPlayedEffect(new ClientboundPlayBlockHitEffectPacket(state1, this.getType(), bounced, true,
					hitLoc.x, hitLoc.y, hitLoc.z, (float) effectNormal.x, (float) effectNormal.y, (float) effectNormal.z));
			}
		}
		if (blockBroken) {
			this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty, 0));
			this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), ProjectileBlock.UPDATE_ALL_IMMEDIATE);
			if (surfaceImpact) {
				float f = (float) toughness / (float) momentum;
				float overPenetrationPower = f < 0.15f ? 2 - 2 * f : 0;
				if (overPenetrationPower > 0 && outcome == ImpactResult.KinematicOutcome.PENETRATE)
					projectileContext.queueExplosion(pos, overPenetrationPower);
			}
		} else {
			if (outcome == ImpactResult.KinematicOutcome.STOP) {
				this.setProjectileMass(0);
			} else {
				this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty / 2f, 0));
			}
			Vec3 spallLoc = hitLoc.add(curVel.normalize().scale(2));
			if (!this.level.isClientSide) {
				ImpactExplosion explosion = new ImpactExplosion(this.level, this, this.indirectArtilleryFire(), spallLoc.x, spallLoc.y, spallLoc.z, 2, Explosion.BlockInteraction.NONE);
				CreateBigCannons.handleCustomExplosion(this.level, explosion);
			}
			SoundType sound = state.getSoundType();
			if (!this.level.isClientSide)
				this.level.playSound(null, spallLoc.x, spallLoc.y, spallLoc.z, sound.getBreakSound(), SoundSource.BLOCKS,
					sound.getVolume(), sound.getPitch());
		}
		shatter |= this.onImpact(blockHitResult, new ImpactResult(outcome, shatter), projectileContext);
		return new ImpactResult(outcome, shatter);
	}

	protected boolean onImpact(HitResult hitResult, ImpactResult impactResult, ProjectileContext projectileContext) {
		return false;
	}

	@Override
	protected void onHitEntity(Entity entity, ProjectileContext projectileContext) {
		super.onHitEntity(entity, projectileContext);
		this.onImpact(new EntityHitResult(entity), new ImpactResult(ImpactResult.KinematicOutcome.PENETRATE, this.getProjectileMass() <= 0),
			projectileContext);
	}

	@Override
	protected DamageSource getEntityDamage() {
		return new CannonDamageSource(CreateBigCannons.MOD_ID + ".big_cannon_projectile", this, null, this.getDamageProperties().ignoresEntityArmor());
	}

	public float addedChargePower() { return this.getBigCannonProjectileProperties().addedChargePower(); }
	public float minimumChargePower() { return this.getBigCannonProjectileProperties().minimumChargePower(); }
	public boolean canSquib() { return this.getBigCannonProjectileProperties().canSquib(); }
	public float addedRecoil() { return this.getBigCannonProjectileProperties().addedRecoil(); }

	@Nonnull protected abstract BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties();

	public enum TrailType {
		NONE,
		LONG,
		SHORT
	}

}
