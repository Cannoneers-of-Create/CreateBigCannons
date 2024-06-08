package rbasamoyai.createbigcannons.munitions.autocannon.flak;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class FlakAutocannonProjectileProperties extends AutocannonProjectileProperties {

	private final double shrapnelSpread;
	private final int shrapnelCount;
	private final float explosionPower;

	public FlakAutocannonProjectileProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable,
											  boolean ignoresEntityArmor, double gravity, double drag, float knockback,
											  double addedRecoil, boolean canSquib, double shrapnelSpread, int shrapnelCount,
											  float explosionPower) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, knockback, addedRecoil, canSquib);
		this.shrapnelSpread = shrapnelSpread;
		this.shrapnelCount = shrapnelCount;
		this.explosionPower = explosionPower;
	}

	public FlakAutocannonProjectileProperties(String id, JsonObject obj) {
		super(id, obj);
		this.shrapnelSpread = Math.max(0, getOrWarn(obj, "shrapnel_spread", id, 1d, JsonElement::getAsDouble));
		this.shrapnelCount = Math.max(0, getOrWarn(obj, "shrapnel_count", id, 1, JsonElement::getAsInt));
		this.explosionPower = Math.max(0, getOrWarn(obj, "explosion_power", id, 2f, JsonElement::getAsFloat));
	}

	public FlakAutocannonProjectileProperties(FriendlyByteBuf buf) {
		super(buf);
		this.shrapnelSpread = buf.readDouble();
		this.shrapnelCount = buf.readVarInt();
		this.explosionPower = buf.readFloat();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeDouble(this.shrapnelSpread);
		buf.writeVarInt(this.shrapnelCount)
			.writeFloat(this.explosionPower);
	}

	public double shrapnelSpread() { return this.shrapnelSpread; }
	public int shrapnelCount() { return this.shrapnelCount; }
	public float explosionPower() { return this.explosionPower; }

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
