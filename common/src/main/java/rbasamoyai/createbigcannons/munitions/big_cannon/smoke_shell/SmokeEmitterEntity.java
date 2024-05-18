package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.effects.SmokeShellSmokeParticleData;

public class SmokeEmitterEntity extends Entity {

	private static final EntityDataAccessor<Float> SMOKE_SIZE = SynchedEntityData.defineId(SmokeEmitterEntity.class, EntityDataSerializers.FLOAT);

	private int duration;

	public SmokeEmitterEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(SMOKE_SIZE, 0f);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("Duration", this.duration);
		tag.putFloat("Size", this.getSize());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.duration = tag.getInt("Duration");
		this.setSize(tag.getFloat("Size"));
	}

	public void setDuration(int duration) { this.duration = duration; }
	public void setSize(float size) { this.entityData.set(SMOKE_SIZE, size); }
	public float getSize() { return this.entityData.get(SMOKE_SIZE); }

	@Override
	public void tick() {
		super.tick();
		if (this.level.isClientSide) {
			if (this.firstTick || (this.level.getGameTime() + this.getId()) % 5 == 0) {
				float size = this.getSize();
				ParticleOptions particle = this.getParticle();
				for (int i = 0; i < Math.ceil(size * 4); ++i) {
					double rx = this.random.nextDouble() * size - size * 0.5f + this.getX();
					double ry = this.random.nextDouble() * size - size * 0.5f + this.getY();
					double rz = this.random.nextDouble() * size - size * 0.5f + this.getZ();
					double dx = this.random.nextGaussian() * 0.05;
					double dy = this.random.nextGaussian() * 0.02;
					double dz = this.random.nextGaussian() * 0.05;
					this.level.addParticle(particle, true, rx, ry, rz, dx, dy, dz);
				}
			}
		} else {
			--this.duration;
			if (this.duration <= 0) this.discard();
		}
	}

	protected ParticleOptions getParticle() {
		return new SmokeShellSmokeParticleData(4);
	}

}
