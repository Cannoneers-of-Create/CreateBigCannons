package rbasamoyai.createbigcannons.munitions.config.components;

import static rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record EntityDamagePropertiesComponent(float entityDamage, boolean rendersInvulnerable, boolean ignoresInvulnerability,
											  boolean ignoresEntityArmor, float knockback) {

	public static final EntityDamagePropertiesComponent DEFAULT = new EntityDamagePropertiesComponent(0, false, false, false, 0);

	public static EntityDamagePropertiesComponent fromJson(String id, JsonObject obj) {
		float entityDamage = Math.max(0, getOrWarn(obj, "entity_damage", id, 1f, JsonElement::getAsFloat));
		boolean rendersInvulnerable = GsonHelper.getAsBoolean(obj, "renders_invulnerable", true);
		boolean ignoresInvulnerability = GsonHelper.getAsBoolean(obj, "ignores_invulnerability", false);
		boolean ignoresEntityArmor = GsonHelper.getAsBoolean(obj, "ignores_entity_armor", false);
		float knockback = Math.max(0, GsonHelper.getAsFloat(obj, "knockback", 2.0f));
		return new EntityDamagePropertiesComponent(entityDamage, rendersInvulnerable, ignoresInvulnerability, ignoresEntityArmor, knockback);
	}

	public static EntityDamagePropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new EntityDamagePropertiesComponent(buf.readFloat(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.entityDamage)
			.writeBoolean(this.rendersInvulnerable)
			.writeBoolean(this.ignoresInvulnerability)
			.writeBoolean(this.ignoresEntityArmor)
			.writeFloat(this.knockback);
	}

}
