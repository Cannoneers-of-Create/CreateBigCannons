package rbasamoyai.createbigcannons.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;

public abstract class EntityTypeConfigurator {

	@ExpectPlatform
	public static EntityTypeConfigurator of(Object builder) {
		throw new AssertionError();
	}

	public abstract EntityTypeConfigurator size(float width, float height);
	public abstract EntityTypeConfigurator fireImmune();
	public abstract EntityTypeConfigurator trackingRange(int range);
	public abstract EntityTypeConfigurator updateInterval(int interval);
	public abstract EntityTypeConfigurator updateVelocity(boolean update);

}
