package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.ExplosionPropertiesComponent;

public class MortarStonePropertiesHandler extends EntityPropertiesTypeHandler<MortarStoneProperties> {

	private static final MortarStoneProperties DEFAULT = new MortarStoneProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, BigCannonProjectilePropertiesComponent.DEFAULT, ExplosionPropertiesComponent.DEFAULT, 0);

	@Override
	protected MortarStoneProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromJson(id, obj);
		ExplosionPropertiesComponent explosion = ExplosionPropertiesComponent.fromJson(id, obj);
		float maxCharges = Math.max(-1, getOrWarn(obj, "max_charges", id, 2f, JsonElement::getAsFloat));
		return new MortarStoneProperties(ballistics, damage, bigCannonProperties, explosion, maxCharges);
	}

	@Override
	protected MortarStoneProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromNetwork(buf);
		ExplosionPropertiesComponent explosion = ExplosionPropertiesComponent.fromNetwork(buf);
		float maxCharges = buf.readFloat();
		return new MortarStoneProperties(ballistics, damage, bigCannonProperties, explosion, maxCharges);
	}

	@Override
	protected void writePropertiesToNetwork(MortarStoneProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.bigCannonProperties().toNetwork(buf);
		properties.explosion().toNetwork(buf);
		buf.writeFloat(properties.maxCharges());
	}

	@Override protected MortarStoneProperties getNoPropertiesValue() { return DEFAULT; }

}
