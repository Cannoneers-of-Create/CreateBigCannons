package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class GrapeshotBagProperties extends BigCannonProjectileProperties {

	private final double grapeshotSpread;
	private final int grapeshotCount;

	public GrapeshotBagProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable, boolean ignoresEntityArmor,
								  double gravity, double drag, boolean isQuadraticDrag, float knockback, int addedChargePower, float minimumChargePower,
								  boolean canSquib, float addedRecoil, double grapeshotSpread, int grapeshotCount) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, isQuadraticDrag, knockback, addedChargePower, minimumChargePower, canSquib, addedRecoil);
		this.grapeshotSpread = grapeshotSpread;
		this.grapeshotCount = grapeshotCount;
	}

	public GrapeshotBagProperties(String id, JsonObject obj) {
		super(id, obj);
		this.grapeshotSpread = Math.max(0, getOrWarn(obj, "grapeshot_spread", id, 1d, JsonElement::getAsDouble));
		this.grapeshotCount = Math.max(0, getOrWarn(obj, "grapeshot_count", id, 1, JsonElement::getAsInt));
	}

	public GrapeshotBagProperties(FriendlyByteBuf buf) {
		super(buf);
		this.grapeshotSpread = buf.readDouble();
		this.grapeshotCount = buf.readVarInt();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeDouble(this.grapeshotSpread);
		buf.writeVarInt(this.grapeshotCount);
	}

	public double grapeshotSpread() { return this.grapeshotSpread; }
	public int grapeshotCount() { return this.grapeshotCount; }

	public static class Serializer implements MunitionPropertiesSerializer<GrapeshotBagProperties> {
		@Override
		public GrapeshotBagProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new GrapeshotBagProperties(loc.toString(), obj);
		}

		@Override
		public GrapeshotBagProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new GrapeshotBagProperties(buf);
		}
	}

}
