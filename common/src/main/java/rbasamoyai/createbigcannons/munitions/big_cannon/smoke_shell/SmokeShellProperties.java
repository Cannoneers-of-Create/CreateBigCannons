package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

public class SmokeShellProperties extends FuzedBigCannonProjectileProperties {

	private final float smokeScale;
	private final int smokeDuration;

	public SmokeShellProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable, boolean ignoresEntityArmor,
								double gravity, double drag, float knockback, int addedChargePower, float minimumChargePower,
								boolean canSquib, float addedRecoil, boolean baseFuze, float smokeScale, int smokeDuration) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, knockback, addedChargePower, minimumChargePower, canSquib, addedRecoil, baseFuze);
		this.smokeScale = smokeScale;
		this.smokeDuration = smokeDuration;
	}

	public SmokeShellProperties(String id, JsonObject obj) {
		super(id, obj);
		this.smokeScale = Mth.clamp(getOrWarn(obj, "smoke_scale", id, 10f, JsonElement::getAsFloat), 1f, 20f);
		this.smokeDuration = Math.max(1, getOrWarn(obj, "smoke_duration", id, 300, JsonElement::getAsInt));
	}

	public SmokeShellProperties(FriendlyByteBuf buf) {
		super(buf);
		this.smokeScale = buf.readFloat();
		this.smokeDuration = buf.readVarInt();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeFloat(this.smokeScale);
		buf.writeVarInt(this.smokeDuration);
	}

	public float smokeScale() { return this.smokeScale; }
	public int smokeDuration() { return this.smokeDuration; }

	public static class Serializer implements MunitionPropertiesSerializer<SmokeShellProperties> {
		@Override
		public SmokeShellProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new SmokeShellProperties(loc.toString(), obj);
		}

		@Override
		public SmokeShellProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new SmokeShellProperties(buf);
		}
	}

}
