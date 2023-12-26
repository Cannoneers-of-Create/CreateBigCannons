package rbasamoyai.createbigcannons.munitions.autocannon.flak;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class FlakAutocannonProjectileProperties extends AutocannonProjectileProperties {

	private final float shrapnelDamage;
	private final double shrapnelSpread;
	private final int shrapnelCount;

	public FlakAutocannonProjectileProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable,
											  boolean ignoresEntityArmor, double gravity, double drag, float knockback,
											  double addedRecoil, boolean canSquib, float shrapnelDamage, double shrapnelSpread,
											  int shrapnelCount) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, knockback, addedRecoil, canSquib);
		this.shrapnelDamage = shrapnelDamage;
		this.shrapnelSpread = shrapnelSpread;
		this.shrapnelCount = shrapnelCount;
	}

	public FlakAutocannonProjectileProperties(String id, JsonObject obj) {
		super(id, obj);
		this.shrapnelDamage = Math.max(0, getOrWarn(obj, "shrapnel_entity_damage", id, 1f, JsonElement::getAsFloat));
		this.shrapnelSpread = Math.max(0, getOrWarn(obj, "shrapnel_spread", id, 1d, JsonElement::getAsDouble));
		this.shrapnelCount = Math.max(0, getOrWarn(obj, "shrapnel_count", id, 1, JsonElement::getAsInt));
	}

	public FlakAutocannonProjectileProperties(FriendlyByteBuf buf) {
		super(buf);
		this.shrapnelDamage = buf.readFloat();
		this.shrapnelSpread = buf.readDouble();
		this.shrapnelCount = buf.readVarInt();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeFloat(this.shrapnelDamage)
			.writeDouble(this.shrapnelSpread);
		buf.writeVarInt(this.shrapnelCount);
	}

	public float shrapnelDamage() { return this.shrapnelDamage; }
	public double shrapnelSpread() { return this.shrapnelSpread; }
	public int shrapnelCount() { return this.shrapnelCount; }

	public static class Serializer implements MunitionPropertiesSerializer<FlakAutocannonProjectileProperties> {
		@Override
		public FlakAutocannonProjectileProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new FlakAutocannonProjectileProperties(loc.toString(), obj);
		}

		@Override
		public FlakAutocannonProjectileProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new FlakAutocannonProjectileProperties(buf);
		}
	}

}
