package rbasamoyai.createbigcannons.munitions.autocannon;

import com.simibubi.create.content.contraptions.particle.AirParticleData;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public abstract class AbstractAutocannonProjectile extends AbstractCannonProjectile {

    protected int ageRemaining;

    protected AbstractAutocannonProjectile(EntityType<? extends AbstractAutocannonProjectile> type, Level level) {
        super(type, level);
        this.ageRemaining = 60;
        this.damage = 15;
    }

    @Override protected float getKnockback(Entity target) { return 1.0f; }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {
            this.ageRemaining--;
            if (this.ageRemaining <= 0) this.discard();
        }
    }

    public void setTracer(boolean tracer) {
        if (tracer) {
            this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) | 2));
        } else {
            this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) & 0b11111101));
        }
    }

    public boolean isTracer() { return (this.entityData.get(ID_FLAGS) & 2) != 0; }

    @Override public final BlockState getRenderedBlockState() { return Blocks.AIR.defaultBlockState(); }

    @Override protected ParticleOptions getTrailParticle() { return new AirParticleData(1, 10); }

    @Override protected float getGravity() { return 0; }
    @Override protected float getInertia() { return 1; }

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

    @Override protected float getPenetratingExplosionPower() { return 1; }
}
