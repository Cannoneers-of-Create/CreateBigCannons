package rbasamoyai.createbigcannons.munitions.big_cannon.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class InertBigCannonProjectilePropertiesHandler extends EntityPropertiesTypeHandler<InertBigCannonProjectileProperties> {

	private static final InertBigCannonProjectileProperties DEFAULT = new InertBigCannonProjectileProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, BigCannonProjectilePropertiesComponent.DEFAULT);

	@Override
	protected InertBigCannonProjectileProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromJson(id, obj);
		return new InertBigCannonProjectileProperties(ballistics, damage, bigCannonProperties);
	}

	@Override
	protected InertBigCannonProjectileProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromNetwork(buf);
		return new InertBigCannonProjectileProperties(ballistics, damage, bigCannonProperties);
	}

	@Override
	protected void writePropertiesToNetwork(InertBigCannonProjectileProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.bigCannonProperties().toNetwork(buf);
	}

	@Override protected InertBigCannonProjectileProperties getNoPropertiesValue() { return DEFAULT; }

}
