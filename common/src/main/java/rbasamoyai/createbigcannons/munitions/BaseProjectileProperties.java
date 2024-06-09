package rbasamoyai.createbigcannons.munitions;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import rbasamoyai.createbigcannons.munitions.config.MunitionProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class BaseProjectileProperties implements MunitionProperties {

	private final float entityDamage;
	private final float durabilityMass;
	private final boolean rendersInvulnerable;
	private final boolean ignoresEntityArmor;
	private final double gravity;
	private final double drag;
	private final boolean isQuadraticDrag;
	private final float knockback;

	public BaseProjectileProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable,
                                    boolean ignoresEntityArmor, double gravity, double drag, boolean isQuadraticDrag,
									float knockback) {
		this.entityDamage = entityDamage;
		this.durabilityMass = durabilityMass;
		this.rendersInvulnerable = rendersInvulnerable;
		this.ignoresEntityArmor = ignoresEntityArmor;
		this.gravity = gravity;
		this.drag = drag;
        this.isQuadraticDrag = isQuadraticDrag;
        this.knockback = knockback;
	}

	public BaseProjectileProperties(String id, JsonObject obj) {
		this.entityDamage = Math.max(0, getOrWarn(obj, "entity_damage", id, 1f, JsonElement::getAsFloat));
		this.durabilityMass = Math.max(0, getOrWarn(obj, "durability_mass", id, 1f, JsonElement::getAsFloat));
		this.rendersInvulnerable = !obj.has("renders_invulnerable") || obj.get("renders_invulnerable").getAsBoolean();
		this.ignoresEntityArmor = obj.has("ignores_entity_armor") && obj.get("ignores_entity_armor").getAsBoolean();
		this.gravity = Math.min(0, GsonHelper.getAsDouble(obj, "gravity", -0.05d));
		this.drag = Math.max(0, GsonHelper.getAsDouble(obj, "drag", 0.0d));
		this.isQuadraticDrag = GsonHelper.getAsBoolean(obj, "quadratic_drag", false);
		this.knockback = Math.max(0, GsonHelper.getAsFloat(obj, "knockback", 2.0f));
	}

	public BaseProjectileProperties(FriendlyByteBuf buf) {
		this.entityDamage = buf.readFloat();
		this.durabilityMass = buf.readFloat();
		this.rendersInvulnerable = buf.readBoolean();
		this.ignoresEntityArmor = buf.readBoolean();
		this.gravity = buf.readDouble();
		this.drag = buf.readDouble();
		this.isQuadraticDrag = buf.readBoolean();
		this.knockback = buf.readFloat();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.entityDamage)
			.writeFloat(this.durabilityMass)
			.writeBoolean(this.rendersInvulnerable)
			.writeBoolean(this.ignoresEntityArmor)
			.writeDouble(this.gravity)
			.writeDouble(this.drag)
			.writeBoolean(this.isQuadraticDrag)
			.writeFloat(this.knockback);
	}

	public float entityDamage() { return this.entityDamage; }
	public float durabilityMass() { return this.durabilityMass; }
	public boolean rendersInvulnerable() { return this.rendersInvulnerable; }
	public boolean ignoresEntityArmor() { return this.ignoresEntityArmor; }
	public double gravity() { return this.gravity; }
	public double drag() { return this.drag; }
	public boolean isQuadraticDrag() { return this.isQuadraticDrag; }
	public float knockback() { return this.knockback; }

	public static class Serializer implements MunitionPropertiesSerializer<BaseProjectileProperties> {
		@Override
		public BaseProjectileProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new BaseProjectileProperties(loc.toString(), obj);
		}

		@Override
		public BaseProjectileProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new BaseProjectileProperties(buf);
		}
	}

}
