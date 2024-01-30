package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class MortarStoneProperties extends BigCannonProjectileProperties {

	private final float explosivePower;
	private final float maxCharges;

	public MortarStoneProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable, boolean ignoresEntityArmor,
								 double gravity, double drag, float knockback, int addedChargePower, float minimumChargePower,
								 boolean canSquib, float addedRecoil, float explosivePower, float maxCharges) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, knockback, addedChargePower, minimumChargePower, canSquib, addedRecoil);
		this.explosivePower = explosivePower;
		this.maxCharges = maxCharges;
	}

	public MortarStoneProperties(String id, JsonObject obj) {
		super(id, obj);
		this.explosivePower = Math.max(0, getOrWarn(obj, "explosive_power", id, 4f, JsonElement::getAsFloat));
		this.maxCharges = Math.max(-1, getOrWarn(obj, "max_charges", id, 2f, JsonElement::getAsFloat));
	}

	public MortarStoneProperties(FriendlyByteBuf buf) {
		super(buf);
		this.explosivePower = buf.readFloat();
		this.maxCharges = buf.readFloat();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeFloat(this.explosivePower)
			.writeFloat(this.maxCharges);
	}

	public float explosionPower() { return this.explosivePower; }
	public float maxCharges() { return this.maxCharges; }

	public static class Serializer implements MunitionPropertiesSerializer<MortarStoneProperties> {
		@Override
		public MortarStoneProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new MortarStoneProperties(loc.toString(), obj);
		}

		@Override
		public MortarStoneProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new MortarStoneProperties(buf);
		}
	}

}
