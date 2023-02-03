package rbasamoyai.createbigcannons.munitions.autocannon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public abstract class AbstractAutocannonProjectile extends AbstractCannonProjectile {

	protected int ageRemaining;

	protected AbstractAutocannonProjectile(EntityType<? extends AbstractAutocannonProjectile> type, Level level) {
		super(type, level);
		this.ageRemaining = 60;
		this.damage = 15;
	}

	@Override protected float getKnockback(Entity target) { return 0.5f; }

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide && this.level.hasChunkAt(this.blockPosition())) {
			this.ageRemaining--;
			if (this.ageRemaining <= 0) this.expireProjectile();
		}
	}

	protected void expireProjectile() {
		this.discard();
	}

	public void setTracer(boolean tracer) {
		if (tracer) {
			this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) | 2));
		} else {
			this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) & 0b11111101));
		}
	}

	@Override
	protected void onFinalImpact(HitResult result) {
		super.onFinalImpact(result);
		if (!this.isRemoved()) this.discard();
	}

	public boolean isTracer() { return (this.entityData.get(ID_FLAGS) & 2) != 0; }

	@Override protected float getGravity() { return 0; }
	@Override protected float getDrag() { return 1; }

    @Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("Tracer", this.isTracer());
		tag.putInt("Age", this.ageRemaining);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setTracer(tag.getBoolean("Tracer"));
		this.ageRemaining = tag.getInt("Age");
	}

	public static void buildAutocannon(EntityType.Builder<? extends AbstractAutocannonProjectile> builder) {
		builder.setTrackingRange(16)
				.setUpdateInterval(1)
				.setShouldReceiveVelocityUpdates(true)
				.fireImmune()
				.sized(0.2f, 0.2f);
	}
	
}
