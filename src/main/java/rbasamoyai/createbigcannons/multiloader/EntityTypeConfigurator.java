package rbasamoyai.createbigcannons.multiloader;

import net.minecraft.world.entity.EntityType;

public class EntityTypeConfigurator {

    public static EntityTypeConfigurator of(Object builder) {
        if (!(builder instanceof EntityType.Builder<?> builderc))
            throw new IllegalStateException("'builder' EntityType.Builder");
        return new EntityTypeConfigurator(builderc);
    }

    private final EntityType.Builder<?> builder;

    protected EntityTypeConfigurator(EntityType.Builder<?> builder) {
        this.builder = builder;
    }

    public EntityTypeConfigurator size(float width, float height) {
        this.builder.sized(width, height);
        return this;
    }

    public EntityTypeConfigurator eyeHeight(float eyeHeight) {
        this.builder.eyeHeight(eyeHeight);
        return this;
    }

    public EntityTypeConfigurator fireImmune() {
        this.builder.fireImmune();
        return this;
    }

    public EntityTypeConfigurator trackingRange(int range) {
        this.builder.setTrackingRange(range);
        return this;
    }

    public EntityTypeConfigurator updateInterval(int interval) {
        this.builder.updateInterval(interval);
        return this;
    }

    public EntityTypeConfigurator updateVelocity(boolean update) {
        this.builder.setShouldReceiveVelocityUpdates(update);
        return this;
    }

}
