package rbasamoyai.createbigcannons.munitions.autocannon.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class InertAutocannonProjectilePropertiesHandler extends EntityPropertiesTypeHandler<InertAutocannonProjectileProperties> {

	private static final InertAutocannonProjectileProperties DEFAULT = new InertAutocannonProjectileProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, AutocannonProjectilePropertiesComponent.DEFAULT);

	@Override
	protected InertAutocannonProjectileProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		AutocannonProjectilePropertiesComponent autocannonProperties = AutocannonProjectilePropertiesComponent.fromJson(id, obj);
		return new InertAutocannonProjectileProperties(ballistics, damage, autocannonProperties);
	}

	@Override
	protected InertAutocannonProjectileProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		AutocannonProjectilePropertiesComponent autocannonProperties = AutocannonProjectilePropertiesComponent.fromNetwork(buf);
		return new InertAutocannonProjectileProperties(ballistics, damage, autocannonProperties);
	}

	@Override
	protected void writePropertiesToNetwork(InertAutocannonProjectileProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.autocannonProperties().toNetwork(buf);
	}

	@Override protected InertAutocannonProjectileProperties getNoPropertiesValue() { return DEFAULT; }

}
