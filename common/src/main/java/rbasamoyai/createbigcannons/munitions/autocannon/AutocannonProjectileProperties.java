package rbasamoyai.createbigcannons.munitions.autocannon;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import rbasamoyai.createbigcannons.munitions.BaseProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class AutocannonProjectileProperties extends BaseProjectileProperties {

	private final double addedRecoil;
	private final boolean canSquib;

	public AutocannonProjectileProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable,
										  boolean ignoresEntityArmor, double gravity, double drag, boolean isQuadraticDrag,
										  float knockback, double addedRecoil, boolean canSquib) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, isQuadraticDrag, knockback);
		this.addedRecoil = addedRecoil;
		this.canSquib = canSquib;
	}

	public AutocannonProjectileProperties(String id, JsonObject obj) {
		super(id, obj);
		this.addedRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "added_recoil", 1));
		this.canSquib = GsonHelper.getAsBoolean(obj, "can_squib", true);
	}

	public AutocannonProjectileProperties(FriendlyByteBuf buf) {
		super(buf);
		this.addedRecoil = buf.readDouble();
		this.canSquib = buf.readBoolean();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeDouble(this.addedRecoil)
			.writeBoolean(this.canSquib);
	}

	public double addedRecoil() { return this.addedRecoil; }
	public boolean canSquib() { return this.canSquib; }

	public static class Serializer implements MunitionPropertiesSerializer<AutocannonProjectileProperties> {
		@Override
		public AutocannonProjectileProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new AutocannonProjectileProperties(loc.toString(), obj);
		}

		@Override
		public AutocannonProjectileProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new AutocannonProjectileProperties(buf);
		}
	}

}
