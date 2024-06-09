package rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import rbasamoyai.createbigcannons.munitions.big_cannon.DropMortarProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class DropMortarShellProperties extends FuzedBigCannonProjectileProperties implements DropMortarProjectileProperties {

	private final float entityDamagingExplosionPower;
	private final float blockDamagingExplosionPower;

	private final float mortarPower;
	private final float mortarRecoil;
	private final float mortarSpread;

	public DropMortarShellProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable, boolean ignoresEntityArmor,
									 double gravity, double drag, boolean isQuadraticDrag, float knockback, int addedChargePower, float minimumChargePower,
									 boolean canSquib, float addedRecoil, boolean baseFuze, float entityDamagingExplosionPower,
									 float blockDamagingExplosionPower, float mortarPower, float mortarRecoil, float mortarSpread) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, isQuadraticDrag, knockback, addedChargePower, minimumChargePower, canSquib, addedRecoil, baseFuze);
		this.entityDamagingExplosionPower = entityDamagingExplosionPower;
		this.blockDamagingExplosionPower = blockDamagingExplosionPower;
		this.mortarPower = mortarPower;
		this.mortarRecoil = mortarRecoil;
		this.mortarSpread = mortarSpread;
	}

	public DropMortarShellProperties(String id, JsonObject obj) {
		super(id, obj);
		this.entityDamagingExplosionPower = Math.max(0, getOrWarn(obj, "entity_damaging_explosion_power", id, 4f, JsonElement::getAsFloat));
		this.blockDamagingExplosionPower = Math.max(0, getOrWarn(obj, "block_damaging_explosion_power", id, 2f, JsonElement::getAsFloat));
		this.mortarPower = Math.max(0, GsonHelper.getAsInt(obj, "mortar_power", 3));
		this.mortarRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "mortar_recoil", 1));
		this.mortarSpread = Math.max(0, GsonHelper.getAsFloat(obj, "mortar_spread", 0.1f));
	}

	public DropMortarShellProperties(FriendlyByteBuf buf) {
		super(buf);
		this.entityDamagingExplosionPower = buf.readFloat();
		this.blockDamagingExplosionPower = buf.readFloat();
		this.mortarPower = buf.readFloat();
		this.mortarRecoil = buf.readFloat();
		this.mortarSpread = buf.readFloat();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeFloat(this.entityDamagingExplosionPower)
			.writeFloat(this.blockDamagingExplosionPower)
			.writeFloat(this.mortarPower)
			.writeFloat(this.mortarRecoil)
			.writeFloat(this.mortarSpread);
	}

	public float entityDamagingExplosionPower() { return this.entityDamagingExplosionPower; }
	public float blockDamagingExplosionPower() { return this.blockDamagingExplosionPower; }

	@Override public float mortarPower() { return this.mortarPower; }
	@Override public float mortarRecoil() { return this.mortarRecoil; }
	@Override public float mortarSpread() { return this.mortarSpread; }

	public static class Serializer implements MunitionPropertiesSerializer<DropMortarShellProperties> {
		@Override
		public DropMortarShellProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new DropMortarShellProperties(loc.toString(), obj);
		}

		@Override
		public DropMortarShellProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new DropMortarShellProperties(buf);
		}
	}

}
