package rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.DropMortarProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class DropMortarShellPropertiesHandler extends EntityPropertiesTypeHandler<DropMortarShellProperties> {

	private static final DropMortarShellProperties DEFAULT = new DropMortarShellProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, BigCannonProjectilePropertiesComponent.DEFAULT, BigCannonFuzePropertiesComponent.DEFAULT,
		DropMortarProjectilePropertiesComponent.DEFAULT, 0, 0);

	@Override
	protected DropMortarShellProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromJson(id, obj);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromJson(id, obj);
		DropMortarProjectilePropertiesComponent dropMortarProperties = DropMortarProjectilePropertiesComponent.fromJson(id, obj);
		float entityDamagingExplosionPower = Math.max(0, getOrWarn(obj, "entity_damaging_explosive_power", id, 4f, JsonElement::getAsFloat));
		float blockDamagingExplosionPower = Math.max(0, getOrWarn(obj, "block_damaging_explosive_power", id, 2f, JsonElement::getAsFloat));
		return new DropMortarShellProperties(ballistics, damage, bigCannonProperties, fuze, dropMortarProperties, entityDamagingExplosionPower, blockDamagingExplosionPower);
	}

	@Override
	protected DropMortarShellProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromNetwork(buf);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromNetwork(buf);
		DropMortarProjectilePropertiesComponent dropMortarProperties = DropMortarProjectilePropertiesComponent.fromNetwork(buf);
		float entityDamagingExplosionPower = buf.readFloat();
		float blockDamagingExplosionPower = buf.readFloat();
		return new DropMortarShellProperties(ballistics, damage, bigCannonProperties, fuze, dropMortarProperties, entityDamagingExplosionPower,
			blockDamagingExplosionPower);
	}

	@Override
	protected void writePropertiesToNetwork(DropMortarShellProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.bigCannonProperties().toNetwork(buf);
		properties.fuze().toNetwork(buf);
		properties.dropMortarProperties().toNetwork(buf);
		buf.writeFloat(properties.entityDamagingExplosivePower())
			.writeFloat(properties.blockDamagingExplosivePower());
	}

	@Override protected DropMortarShellProperties getNoPropertiesValue() { return DEFAULT; }

}
