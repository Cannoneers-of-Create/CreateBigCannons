package rbasamoyai.createbigcannons.fabric.multiloader.fabric;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.EntityDimensions;
import rbasamoyai.createbigcannons.multiloader.EntityTypeConfigurator;

public class EntityTypeConfiguratorImpl extends EntityTypeConfigurator {

	private final FabricEntityTypeBuilder<?> builder;

	public static EntityTypeConfigurator of(Object builder) {
		if (!(builder instanceof FabricEntityTypeBuilder<?> builderc))
			throw new IllegalStateException("'builder' must be a FabricEntityTypeBuilder");
		return new EntityTypeConfiguratorImpl(builderc);
	}

	protected EntityTypeConfiguratorImpl(FabricEntityTypeBuilder<?> builder) {
		this.builder = builder;
	}

	@Override
	public EntityTypeConfigurator size(float width, float height) {
		this.builder.dimensions(EntityDimensions.scalable(width, height));
		return this;
	}

	@Override
	public EntityTypeConfigurator fireImmune() {
		this.builder.fireImmune();
		return this;
	}

	@Override
	public EntityTypeConfigurator trackingRange(int range) {
		this.builder.trackRangeChunks(range);
		return this;
	}

	@Override
	public EntityTypeConfigurator updateInterval(int interval) {
		this.builder.trackedUpdateRate(interval);
		return this;
	}

	@Override
	public EntityTypeConfigurator updateVelocity(boolean update) {
		this.builder.forceTrackedVelocityUpdates(update);
		return this;
	}

}
