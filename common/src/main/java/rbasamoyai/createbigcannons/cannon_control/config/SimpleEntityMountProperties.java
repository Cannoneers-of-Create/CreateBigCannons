package rbasamoyai.createbigcannons.cannon_control.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;

public record SimpleEntityMountProperties(GeneralMountProperties properties) implements CannonMountEntityPropertiesProvider {

	@Override public float maximumElevation(Entity entity) { return this.properties.maximumElevation(); }
	@Override public float maximumDepression(Entity entity) { return this.properties.maximumDepression(); }

	public static class Serializer implements CannonMountEntityPropertiesSerializer<SimpleEntityMountProperties> {
		@Override
		public SimpleEntityMountProperties fromJson(EntityType<?> beType, ICannonContraptionType contraptionType, JsonObject obj) {
			return new SimpleEntityMountProperties(GeneralMountProperties.fromJson(obj));
		}

		@Override
		public void toNetwork(SimpleEntityMountProperties properties, FriendlyByteBuf buf) {
			properties.properties.toNetwork(buf);
		}

		@Override
		public SimpleEntityMountProperties fromNetwork(FriendlyByteBuf buf) {
			GeneralMountProperties properties = GeneralMountProperties.fromNetwork(buf);
			return new SimpleEntityMountProperties(properties);
		}
	}

}
