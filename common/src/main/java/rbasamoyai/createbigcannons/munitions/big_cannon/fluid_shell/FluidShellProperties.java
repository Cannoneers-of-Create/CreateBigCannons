package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class FluidShellProperties extends FuzedBigCannonProjectileProperties {

	private final int fluidShellCapacity;
	private final int mBPerFluidBlob;
	private final int mBPerAoeRadius;
	private final float fluidBlobSpread;

	public FluidShellProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable, boolean ignoresEntityArmor,
								double gravity, double drag, boolean isQuadraticDrag, float knockback, int addedChargePower, float minimumChargePower,
								boolean canSquib, float addedRecoil, boolean baseFuze, int fluidShellCapacity, int mBPerFluidBlob,
								int mBPerAoeRadius, float fluidBlobSpread) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, isQuadraticDrag, knockback, addedChargePower, minimumChargePower, canSquib, addedRecoil, baseFuze);
		this.fluidShellCapacity = fluidShellCapacity;
		this.mBPerFluidBlob = mBPerFluidBlob;
		this.mBPerAoeRadius = mBPerAoeRadius;
		this.fluidBlobSpread = fluidBlobSpread;
	}

	public FluidShellProperties(String id, JsonObject obj) {
		super(id, obj);
		this.fluidShellCapacity = Math.max(1, getOrWarn(obj, "fluid_shell_capacity", id, 2000, JsonElement::getAsInt));
		this.mBPerFluidBlob = Math.max(25, getOrWarn(obj, "millibuckets_per_fluid_blob", id, 250, JsonElement::getAsInt));
		this.mBPerAoeRadius = Math.max(25, getOrWarn(obj, "millibuckets_per_area_of_effect_radius", id, 50, JsonElement::getAsInt));
		this.fluidBlobSpread = Math.max(0.01f, getOrWarn(obj, "fluid_blob_spread", id, 1f, JsonElement::getAsFloat));
	}

	public FluidShellProperties(FriendlyByteBuf buf) {
		super(buf);
		this.fluidShellCapacity = buf.readVarInt();
		this.mBPerFluidBlob = buf.readVarInt();
		this.mBPerAoeRadius = buf.readVarInt();
		this.fluidBlobSpread = buf.readFloat();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeVarInt(this.fluidShellCapacity)
			.writeVarInt(this.mBPerFluidBlob)
			.writeVarInt(this.mBPerAoeRadius)
			.writeFloat(this.fluidBlobSpread);
	}

	public int fluidShellCapacity() { return this.fluidShellCapacity; }
	public int mBPerFluidBlob() { return this.mBPerFluidBlob; }
	public int mBPerAoeRadius() { return this.mBPerAoeRadius; }
	public float fluidBlobSpread() { return this.fluidBlobSpread; }

	public static class Serializer implements MunitionPropertiesSerializer<FluidShellProperties> {
		@Override
		public FluidShellProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new FluidShellProperties(loc.toString(), obj);
		}

		@Override
		public FluidShellProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new FluidShellProperties(buf);
		}
	}

}
