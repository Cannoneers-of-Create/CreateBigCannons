package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class SmokeShellPropertiesHandler extends EntityPropertiesTypeHandler<SmokeShellProperties> {

	private static final SmokeShellProperties DEFAULT = new SmokeShellProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, BigCannonProjectilePropertiesComponent.DEFAULT, BigCannonFuzePropertiesComponent.DEFAULT,
		0, 0);

	@Override
	protected SmokeShellProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromJson(id, obj);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromJson(id, obj);
		float smokeScale = Mth.clamp(getOrWarn(obj, "smoke_scale", id, 10f, JsonElement::getAsFloat), 1f, 20f);
		int smokeDuration = Math.max(1, getOrWarn(obj, "smoke_duration", id, 300, JsonElement::getAsInt));
		return new SmokeShellProperties(ballistics, damage, bigCannonProperties, fuze, smokeScale, smokeDuration);
	}

	@Override
	protected SmokeShellProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromNetwork(buf);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromNetwork(buf);
		float smokeScale = buf.readFloat();
		int smokeDuration = buf.readVarInt();
		return new SmokeShellProperties(ballistics, damage, bigCannonProperties, fuze, smokeScale, smokeDuration);
	}

	@Override
	protected void writePropertiesToNetwork(SmokeShellProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.bigCannonProperties().toNetwork(buf);
		properties.fuze().toNetwork(buf);
		buf.writeFloat(properties.smokeScale());
		buf.writeVarInt(properties.smokeDuration());
	}

	@Override protected SmokeShellProperties getNoPropertiesValue() { return DEFAULT; }

}
