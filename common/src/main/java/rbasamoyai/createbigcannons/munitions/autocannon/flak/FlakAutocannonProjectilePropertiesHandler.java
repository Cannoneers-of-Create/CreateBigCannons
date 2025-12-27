package rbasamoyai.createbigcannons.munitions.autocannon.flak;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.ExplosionPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.ProjectileBurstParentPropertiesComponent;

public class FlakAutocannonProjectilePropertiesHandler extends EntityPropertiesTypeHandler<FlakAutocannonProjectileProperties> {

	private static final FlakAutocannonProjectileProperties DEFAULT = new FlakAutocannonProjectileProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, AutocannonProjectilePropertiesComponent.DEFAULT, ExplosionPropertiesComponent.DEFAULT,
		ProjectileBurstParentPropertiesComponent.DEFAULT);

	@Override
	protected FlakAutocannonProjectileProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		AutocannonProjectilePropertiesComponent autocannonProperties = AutocannonProjectilePropertiesComponent.fromJson(id, obj);
		ExplosionPropertiesComponent explosion = ExplosionPropertiesComponent.fromJson(id, obj);
		ProjectileBurstParentPropertiesComponent flakBurst = ProjectileBurstParentPropertiesComponent.fromJson(id, "shrapnel_", obj);
		return new FlakAutocannonProjectileProperties(ballistics, damage, autocannonProperties, explosion, flakBurst);
	}

	@Override
	protected FlakAutocannonProjectileProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		AutocannonProjectilePropertiesComponent autocannonProperties = AutocannonProjectilePropertiesComponent.fromNetwork(buf);
		ExplosionPropertiesComponent explosion = ExplosionPropertiesComponent.fromNetwork(buf);
		ProjectileBurstParentPropertiesComponent flakBurst = ProjectileBurstParentPropertiesComponent.fromNetwork(buf);
		return new FlakAutocannonProjectileProperties(ballistics, damage, autocannonProperties, explosion, flakBurst);
	}

	@Override
	protected void writePropertiesToNetwork(FlakAutocannonProjectileProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.autocannonProperties().toNetwork(buf);
		properties.explosion().toNetwork(buf);
		properties.flakBurst().toNetwork(buf);
	}

	@Override protected FlakAutocannonProjectileProperties getNoPropertiesValue() { return DEFAULT; }

}
