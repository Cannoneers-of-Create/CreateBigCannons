package rbasamoyai.createbigcannons.munitions;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class Shrapnel extends AbstractHurtingProjectile {

	public static final DamageSource SHRAPNEL = new DamageSource(CreateBigCannons.MOD_ID + ".shrapnel");
	
	private int age;
	
	public Shrapnel(EntityType<? extends Shrapnel> type, Level level) {
		super(type, level);
	}
	
	@Override
	public void tick() {
		super.tick();
		++this.age;
		if (!this.level.isClientSide && this.age > 160) {
			this.discard();
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Age", this.age);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.age = tag.getInt("Age");
	}
	
	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		BlockPos pos = result.getBlockPos();
		this.level.playSound(null, pos, this.level.getBlockState(pos).getSoundType().getBreakSound(), SoundSource.NEUTRAL, 1.0f, 2.0f);
	}
	
	@Override
	protected void onHitEntity(EntityHitResult result) {
		result.getEntity().hurt(SHRAPNEL, 10.0f);
		result.getEntity().invulnerableTime = 0;
	}
	
	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		this.discard();
	}
	
	public static void build(EntityType.Builder<? extends Shrapnel> builder) {
		builder.setTrackingRange(3)
				.setUpdateInterval(20)
				.setShouldReceiveVelocityUpdates(true)
				.fireImmune()
				.sized(0.25f, 0.25f);
	}
	
	@Override
	protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
		return 0.125f;
	}
	
	@Override protected float getInertia() { return 0.99f; }

}
