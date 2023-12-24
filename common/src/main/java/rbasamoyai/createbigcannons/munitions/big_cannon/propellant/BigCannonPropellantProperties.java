package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import rbasamoyai.createbigcannons.munitions.config.MunitionProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class BigCannonPropellantProperties implements MunitionProperties {

	private final float strength;
	private final float addedStress;
	private final float addedRecoil;
	private final float addedSpread;

	public BigCannonPropellantProperties(float strength, float addedStress, float addedRecoil, float addedSpread) {
		this.strength = strength;
		this.addedStress = addedStress;
		this.addedRecoil = addedRecoil;
		this.addedSpread = addedSpread;
	}

	public BigCannonPropellantProperties(String id, JsonObject obj) {
		this.strength = Math.max(0, GsonHelper.getAsFloat(obj, "strength", 2));
		this.addedStress = Math.max(0, GsonHelper.getAsFloat(obj, "added_stress", 1));
		this.addedRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "added_recoil", 2));
		this.addedSpread = Math.max(0, GsonHelper.getAsFloat(obj, "added_spread", 2));
	}

	public BigCannonPropellantProperties(FriendlyByteBuf buf) {
		this.strength = buf.readFloat();
		this.addedStress = buf.readFloat();
		this.addedRecoil = buf.readFloat();
		this.addedSpread = buf.readFloat();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.strength)
			.writeFloat(this.addedStress)
			.writeFloat(this.addedRecoil)
			.writeFloat(this.addedSpread);
	}

	public float strength() { return this.strength; }
	public float addedStress() { return this.addedStress; }
	public float addedRecoil() { return this.addedRecoil; }
	public float addedSpread() { return this.addedSpread; }

	public static class Serializer implements MunitionPropertiesSerializer<BigCannonPropellantProperties> {
		@Override
		public BigCannonPropellantProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new BigCannonPropellantProperties(loc.toString(), obj);
		}

		@Override
		public BigCannonPropellantProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new BigCannonPropellantProperties(buf);
		}
	}

}
