package rbasamoyai.createbigcannons.cannon_control.config;

import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;

public class DefaultCannonMountPropertiesSerializers {

	public static void init() {
		CannonMountPropertiesHandler.registerBlockMountSerializer(CBCBlockEntities.CANNON_MOUNT.get(), new SimpleBlockMountProperties.Serializer());
		CannonMountPropertiesHandler.registerEntityMountSerializer(CBCEntityTypes.CANNON_CARRIAGE.get(), new SimpleEntityMountProperties.Serializer());
	}

}
