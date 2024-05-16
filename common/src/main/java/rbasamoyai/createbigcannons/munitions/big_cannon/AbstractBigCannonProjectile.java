package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.mojang.math.Constants;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.TrailSmokeParticleData;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;

public abstract class AbstractBigCannonProjectile<T extends BigCannonProjectileProperties> extends AbstractCannonProjectile<T> {

	protected AbstractBigCannonProjectile(EntityType<? extends AbstractBigCannonProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		ChunkPos cpos = new ChunkPos(this.blockPosition());
		if (this.level.isClientSide || this.level.hasChunk(cpos.x, cpos.z)) {
			super.tick();
			if (!this.isInGround()) {
				TrailType trailType = CBCConfigs.SERVER.munitions.bigCannonTrailType.get();
				if (trailType != TrailType.NONE) {
					int lifetime = trailType == TrailType.SHORT ? 100 : 280 + this.level.random.nextInt(50);
					for (int i = 0; i < 10; ++i) {
						double partial = i * 0.1f;
						double dx = Mth.lerp(partial, this.xOld, this.getX());
						double dy = Mth.lerp(partial, this.yOld, this.getY());
						double dz = Mth.lerp(partial, this.zOld, this.getZ());
						double sx = this.level.random.nextDouble(-0.002d, 0.002d);
						double sy = this.level.random.nextDouble(-0.002d, 0.002d);
						double sz = this.level.random.nextDouble(-0.002d, 0.002d);
						this.level.addAlwaysVisibleParticle(new TrailSmokeParticleData(lifetime), true, dx, dy, dz, sx, sy, sz);
					}
				}
			}
		}
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
	protected void onDestroyBlock(BlockState state, BlockHitResult result) {
		double mass = this.getProjectileMass();
		Vec3 curVel = this.getDeltaMovement();
		double mag = curVel.length();
		double bonus = 1 + Math.max(0, (mag - CBCConfigs.SERVER.munitions.minVelocityForPenetrationBonus.getF())
			* CBCConfigs.SERVER.munitions.penetrationBonusScale.getF());

		double hardness = BlockArmorPropertiesHandler.getProperties(state).hardness(this.level, state, result.getBlockPos(), true) / bonus;
		this.setProjectileMass((float) Math.max(mass - hardness, 0));

		if (!level.isClientSide()) this.level.destroyBlock(result.getBlockPos(), false);
	}

	@Override
	protected boolean canDeflect(BlockHitResult result) {
		return super.canDeflect(result) && this.random.nextFloat() < CBCConfigs.SERVER.munitions.bigCannonDeflectChance.getF();
	}

	@Override
	protected boolean canBounceOffOf(BlockState state) {
		return super.canBounceOffOf(state) && this.random.nextFloat() < CBCConfigs.SERVER.munitions.bigCannonDeflectChance.getF();
	}

	@Override
	protected DamageSource getEntityDamage() {
		return new CannonDamageSource(CreateBigCannons.MOD_ID + ".big_cannon_projectile", this, null);
	}

	public float addedChargePower() {
		T properties = this.getProperties();
		return properties == null ? 0 : properties.addedChargePower();
	}

	public float minimumChargePower() {
		T properties = this.getProperties();
		return properties == null ? 1 : properties.minimumChargePower();
	}

	public boolean canSquib() {
		T properties = this.getProperties();
		return properties == null || properties.canSquib();
	}

	public float addedRecoil() {
		T properties = this.getProperties();
		return properties == null ? 1 : properties.addedRecoil();
	}

	public enum TrailType {
		NONE,
		LONG,
		SHORT
	}

}
