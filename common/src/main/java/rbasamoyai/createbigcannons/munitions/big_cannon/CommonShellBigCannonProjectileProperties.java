package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class CommonShellBigCannonProjectileProperties extends FuzedBigCannonProjectileProperties {

	private final float explosivePower;

	public CommonShellBigCannonProjectileProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable,
													boolean ignoresEntityArmor, double gravity, double drag, boolean isQuadraticDrag, float knockback,
													int addedChargePower, float minimumChargePower, boolean canSquib,
													float addedRecoil, boolean baseFuze, float explosivePower) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, isQuadraticDrag, knockback, addedChargePower, minimumChargePower, canSquib, addedRecoil, baseFuze);
		this.explosivePower = explosivePower;
	}

	public CommonShellBigCannonProjectileProperties(String id, JsonObject obj) {
		super(id, obj);
		this.explosivePower = Math.max(0, GsonHelper.getAsFloat(obj, "explosive_power", 0));
	}

	public CommonShellBigCannonProjectileProperties(FriendlyByteBuf buf) {
		super(buf);
		this.explosivePower = buf.readFloat();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeFloat(this.explosivePower);
	}

	public float explosivePower() { return this.explosivePower; }

	public static class Serializer implements MunitionPropertiesSerializer<CommonShellBigCannonProjectileProperties> {
		@Override
		public CommonShellBigCannonProjectileProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new CommonShellBigCannonProjectileProperties(loc.toString(), obj);
		}

		@Override
		public CommonShellBigCannonProjectileProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new CommonShellBigCannonProjectileProperties(buf);
		}
	}

}
