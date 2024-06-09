package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import rbasamoyai.createbigcannons.munitions.BaseProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class BigCannonProjectileProperties extends BaseProjectileProperties {

	private final float addedChargePower;
	private final float minimumChargePower;
	private final boolean canSquib;
	private final float addedRecoil;

	public BigCannonProjectileProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable,
										 boolean ignoresEntityArmor, double gravity, double drag, boolean isQuadraticDrag,
										 float knockback, int addedChargePower, float minimumChargePower, boolean canSquib,
										 float addedRecoil) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, isQuadraticDrag, knockback);
		this.addedChargePower = addedChargePower;
		this.minimumChargePower = minimumChargePower;
		this.canSquib = canSquib;
		this.addedRecoil = addedRecoil;
	}

	public BigCannonProjectileProperties(String id, JsonObject obj) {
		super(id, obj);
		this.addedChargePower = Math.max(0, GsonHelper.getAsFloat(obj, "added_charge_power", 0));
		this.minimumChargePower = Math.max(0, GsonHelper.getAsFloat(obj, "minimum_charge_power", 1));
		this.canSquib = GsonHelper.getAsBoolean(obj, "can_squib", true);
		this.addedRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "added_recoil", 1));
	}

	public BigCannonProjectileProperties(FriendlyByteBuf buf) {
		super(buf);
		this.addedChargePower = buf.readFloat();
		this.minimumChargePower = buf.readFloat();
		this.canSquib = buf.readBoolean();
		this.addedRecoil = buf.readFloat();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeFloat(this.addedChargePower)
			.writeFloat(this.minimumChargePower)
			.writeBoolean(this.canSquib)
			.writeFloat(this.addedRecoil);
	}

	public float addedChargePower() { return this.addedChargePower; }
	public float minimumChargePower() { return this.minimumChargePower; }
	public boolean canSquib() { return this.canSquib; }
	public float addedRecoil() { return this.addedRecoil; }

	public static class Serializer implements MunitionPropertiesSerializer<BigCannonProjectileProperties> {
		@Override
		public BigCannonProjectileProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new BigCannonProjectileProperties(loc.toString(), obj);
		}

		@Override
		public BigCannonProjectileProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new BigCannonProjectileProperties(buf);
		}
	}

}
