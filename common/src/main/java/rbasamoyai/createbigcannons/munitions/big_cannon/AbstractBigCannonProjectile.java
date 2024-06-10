package rbasamoyai.createbigcannons.munitions.big_cannon;

import javax.annotation.Nonnull;

import com.mojang.math.Constants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.TrailSmokeParticleData;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;

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
