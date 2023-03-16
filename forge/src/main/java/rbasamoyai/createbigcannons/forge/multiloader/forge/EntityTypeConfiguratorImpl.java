package rbasamoyai.createbigcannons.forge.multiloader.forge;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.multiloader.EntityTypeConfigurator;

public class EntityTypeConfiguratorImpl extends EntityTypeConfigurator {

	private final EntityType.Builder<?> builder;

	public static EntityTypeConfigurator of(Object builder) {
		if (!(builder instanceof EntityType.Builder<?> builderc))
			throw new IllegalStateException("'builder' EntityType.Builder");
		return new EntityTypeConfiguratorImpl(builderc);
	}

	protected EntityTypeConfiguratorImpl(EntityType.Builder<?> builder) {
		this.builder = builder;
	}

	@Override
	public EntityTypeConfigurator size(float width, float height) {
		this.builder.sized(width, height);
		return this;
	}

	@Override
	public EntityTypeConfigurator fireImmune() {
		this.builder.fireImmune();
		return this;
	}

	@Override
	public EntityTypeConfigurator trackingRange(int range) {
		this.builder.setTrackingRange(range);
		return this;
	}

	@Override
	public EntityTypeConfigurator updateInterval(int interval) {
		this.builder.updateInterval(interval);
		return this;
	}

	@Override
	public EntityTypeConfigurator updateVelocity(boolean update) {
		this.builder.setShouldReceiveVelocityUpdates(update);
		return this;
	}

}
