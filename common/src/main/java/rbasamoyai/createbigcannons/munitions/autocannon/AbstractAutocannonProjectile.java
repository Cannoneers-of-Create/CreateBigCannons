package rbasamoyai.createbigcannons.munitions.autocannon;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mojang.math.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.TrailSmokeParticleData;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.network.ClientboundPlayBlockHitEffectPacket;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public abstract class AbstractAutocannonProjectile extends AbstractCannonProjectile {

	protected double displacement = 0;
	protected int ageRemaining;
	protected final Map<Player, Integer> whooshedPlayers = new HashMap<>();

	protected AbstractAutocannonProjectile(EntityType<? extends AbstractAutocannonProjectile> type, Level level) {
		super(type, level);
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

			this.setYRot(this.getYRot());
			this.setXRot(this.getXRot());
		}
	}

	@Override
	public void tick() {
		Vec3 prevPos = this.position();
		ChunkPos cpos = new ChunkPos(this.blockPosition());
		Vec3 nextVelocity = this.nextVelocity;
		if (this.level.isClientSide || this.level.hasChunk(cpos.x, cpos.z)) {
			super.tick();
			this.displacement += this.position().distanceTo(prevPos);
			if (!this.level.isClientSide) {
				this.ageRemaining--;
				if (this.ageRemaining <= 0)
					this.expireProjectile();
			}
			if (!this.isInGround()) {
				TrailType trailType = CBCConfigs.SERVER.munitions.autocannonTrailType.get();
				if (trailType != TrailType.NONE) {
					int lifetime = trailType == TrailType.SHORT ? 50 : 100 + this.level.random.nextInt(50);
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
					if (nextVelocity != null) {
						ParticleOptions options1 = new TrailSmokeParticleData(lifetime - 1);
						Vec3 nextPos = this.position().add(nextVelocity);
						for (int i = 0; i < 20; ++i) {
							double partial = i * 0.1f;
							double dx = Mth.lerp(partial, this.getX(), nextPos.x);
							double dy = Mth.lerp(partial, this.getY(), nextPos.y);
							double dz = Mth.lerp(partial, this.getZ(), nextPos.z);
							double sx = this.level.random.nextDouble() * 0.004d - 0.002d;
							double sy = this.level.random.nextDouble() * 0.004d - 0.002d;
							double sz = this.level.random.nextDouble() * 0.004d - 0.002d;
							this.level.addAlwaysVisibleParticle(options1, true, dx, dy, dz, sx, sy, sz);
						}
					}
				}
				if (this.level.isClientSide && CBCConfigs.CLIENT.enableAutocannonFlybySounds.get()) {
					for (Iterator<Map.Entry<Player, Integer>> iter = this.whooshedPlayers.entrySet().iterator(); iter.hasNext(); ) {
						Map.Entry<Player, Integer> entry = iter.next();
						if (entry.getKey().isRemoved() || !entry.getKey().isAlive()) {
							iter.remove();
							continue;
						}
						int v = entry.getValue();
						if (v <= 0) {
							iter.remove();
						} else {
							entry.setValue(v - 1);
						}
					}
					SoundEvent soundEvent = this.getAutocannonRoundType() == AutocannonAmmoType.MACHINE_GUN
						? CBCSoundEvents.MACHINE_GUN_ROUND_FLYBY.getMainEvent()
						: CBCSoundEvents.AUTOCANNON_ROUND_FLYBY.getMainEvent();
					Vec3 curPos = this.position();
					Vec3 displacementVec = curPos.subtract(prevPos);
					AABB path = this.getBoundingBox().expandTowards(displacementVec.reverse()).inflate(3);
					for (Player player : this.level.getNearbyPlayers(TargetingConditions.forNonCombat(), null, path)) {
						Vec3 diff = player.position().subtract(prevPos);
						boolean flag = this.whooshedPlayers.containsKey(player);
						this.whooshedPlayers.put(player, 3);
						if (flag)
							continue;
						float volume = displacementVec.dot(diff) < 0 ? 0.25f : 1f;
						float pitch = 0.95f + this.random.nextFloat() * 0.2f;
						this.level.playSound(player,this.getX(), this.getY(), this.getZ(), soundEvent, SoundSource.NEUTRAL, volume, pitch);
					}
				}
			}
		}
	}

	protected void expireProjectile() {
		this.discard();
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		this.whooshedPlayers.clear();
	}

	public void setTracer(boolean tracer) {
		if (tracer) {
			this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) | 2));
		} else {
			this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) & 0b11111101));
		}
	}

	public void setLifetime(int lifetime) { this.ageRemaining = lifetime; }

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
		double momentum = mass * velMag * incidence * bonusMomentum;

		double hardnessPenalty = Math.max(blockArmor.hardness(this.level, state, pos, true) - ballistics.penetration(), 0);

		double projectileDeflection = ballistics.deflection();
		double baseChance = CBCConfigs.SERVER.munitions.baseProjectileBounceChance.getF();
		double bounceChance = projectileDeflection < 1e-2d || incidence > projectileDeflection ? 0 : Math.max(baseChance, 1 - incidence / projectileDeflection);

		boolean surfaceImpact = this.lastPenetratedBlock.isAir();
		boolean canBounce = CBCConfigs.SERVER.munitions.projectilesCanBounce.get();
		ImpactResult.KinematicOutcome outcome;
		if (surfaceImpact && canBounce && this.level.getRandom().nextDouble() < bounceChance) {
			outcome = ImpactResult.KinematicOutcome.BOUNCE;
		} else {
			outcome = ImpactResult.KinematicOutcome.STOP;
		}
		boolean shatter = surfaceImpact && outcome != ImpactResult.KinematicOutcome.BOUNCE && hardnessPenalty > ballistics.toughness();

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
			if (hardnessPenalty > 1e-2d) {
				if (ballistics.toughness() < 1e-2d){
					momentum = 0;
				} else{
					momentum *= Math.max(0.25, 1 - hardnessPenalty / ballistics.toughness());
				}
			}
			if (!unbreakable)
				CreateBigCannons.BLOCK_DAMAGE.damageBlock(pos.immutable(), Math.max(Mth.ceil(momentum), 0), state, this.level);
		}
		this.onImpact(blockHitResult, new ImpactResult(outcome, shatter), projectileContext);
		return new ImpactResult(outcome, !this.level.isClientSide && (shatter || outcome != ImpactResult.KinematicOutcome.BOUNCE));
	}

	public boolean isTracer() { return (this.entityData.get(ID_FLAGS) & 2) != 0; }

	public double getTotalDisplacement() { return this.displacement; }
	public void setTotalDisplacement(double value) { this.displacement = value; }

    @Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("Tracer", this.isTracer());
		tag.putInt("Age", this.ageRemaining);
		tag.putDouble("Displacement", this.displacement);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setTracer(tag.getBoolean("Tracer"));
		this.ageRemaining = tag.getInt("Age");
		this.displacement = tag.getFloat("Displacement");
	}

	@Override
	public CompoundTag addExtraSyncData() {
		CompoundTag tag = super.addExtraSyncData();
		tag.putDouble("Displacement", this.displacement);
		return tag;
	}

	@Override
	public void readExtraSyncData(CompoundTag tag) {
		super.readExtraSyncData(tag);
		this.displacement = tag.getDouble("Displacement");
	}

	public AutocannonAmmoType getAutocannonRoundType() { return AutocannonAmmoType.AUTOCANNON; }

	public enum TrailType {
		NONE,
		LONG,
		SHORT
	}

}
