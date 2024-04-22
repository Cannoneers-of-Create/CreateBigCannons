package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class ShrapnelShellProperties extends FuzedBigCannonProjectileProperties {

	private final float shrapnelDamage;
	private final double shrapnelSpread;
	private final int shrapnelCount;
	private final float explosionPower;

	public ShrapnelShellProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable, boolean ignoresEntityArmor,
								   double gravity, double drag, float knockback, int addedChargePower, float minimumChargePower,
								   boolean canSquib, float addedRecoil, boolean baseFuze, float shrapnelDamage, double shrapnelSpread,
								   int shrapnelCount, float explosionPower) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, knockback, addedChargePower, minimumChargePower, canSquib, addedRecoil, baseFuze);
		this.shrapnelDamage = shrapnelDamage;
		this.shrapnelSpread = shrapnelSpread;
		this.shrapnelCount = shrapnelCount;
		this.explosionPower = explosionPower;
	}

	public ShrapnelShellProperties(String id, JsonObject obj) {
		super(id, obj);
		this.shrapnelDamage = Math.max(0, getOrWarn(obj, "shrapnel_entity_damage", id, 1f, JsonElement::getAsFloat));
		this.shrapnelSpread = Math.max(0, getOrWarn(obj, "shrapnel_spread", id, 1d, JsonElement::getAsDouble));
		this.shrapnelCount = Math.max(0, getOrWarn(obj, "shrapnel_count", id, 1, JsonElement::getAsInt));
		this.explosionPower = Math.max(0, getOrWarn(obj, "explosion_power", id, 2f, JsonElement::getAsFloat));
	}

	public ShrapnelShellProperties(FriendlyByteBuf buf) {
		super(buf);
		this.shrapnelDamage = buf.readFloat();
		this.shrapnelSpread = buf.readDouble();
		this.shrapnelCount = buf.readVarInt();
		this.explosionPower = buf.readFloat();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeFloat(this.shrapnelDamage)
			.writeDouble(this.shrapnelSpread);
		buf.writeVarInt(this.shrapnelCount)
			.writeFloat(this.explosionPower);
	}

	public float shrapnelDamage() { return this.shrapnelDamage; }
	public double shrapnelSpread() { return this.shrapnelSpread; }
	public int shrapnelCount() { return this.shrapnelCount; }
	public float explosionPower() { return this.explosionPower; }

	public static class Serializer implements MunitionPropertiesSerializer<ShrapnelShellProperties> {
		@Override
		public ShrapnelShellProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new ShrapnelShellProperties(loc.toString(), obj);
		}

		@Override
		public ShrapnelShellProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new ShrapnelShellProperties(buf);
		}
	}

}
