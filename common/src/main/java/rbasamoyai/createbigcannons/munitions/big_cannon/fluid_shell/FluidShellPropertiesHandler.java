package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class FluidShellPropertiesHandler extends EntityPropertiesTypeHandler<FluidShellProperties> {

	private static final FluidShellProperties DEFAULT = new FluidShellProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, BigCannonProjectilePropertiesComponent.DEFAULT,
		BigCannonFuzePropertiesComponent.DEFAULT, 0, 0, 0, 0);

	@Override
	protected FluidShellProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromJson(id, obj);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromJson(id, obj);
		int fluidShellCapacity = Math.max(1, getOrWarn(obj, "fluid_shell_capacity", id, 2000, JsonElement::getAsInt));
		int mBPerFluidBlob = Math.max(25, getOrWarn(obj, "millibuckets_per_fluid_blob", id, 250, JsonElement::getAsInt));
		int mBPerAoeRadius = Math.max(25, getOrWarn(obj, "millibuckets_per_area_of_effect_radius", id, 50, JsonElement::getAsInt));
		float fluidBlobSpread = Math.max(0.01f, getOrWarn(obj, "fluid_blob_spread", id, 1f, JsonElement::getAsFloat));
		return new FluidShellProperties(ballistics, damage, bigCannonProperties, fuze, fluidShellCapacity, mBPerFluidBlob,
			mBPerAoeRadius, fluidBlobSpread);
	}

	@Override
	protected FluidShellProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromNetwork(buf);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromNetwork(buf);
		int fluidShellCapacity = buf.readVarInt();
		int mBPerFluidBlob = buf.readVarInt();
		int mBPerAoeRadius = buf.readVarInt();
		float fluidBlobSpread = buf.readFloat();
		return new FluidShellProperties(ballistics, damage, bigCannonProperties, fuze, fluidShellCapacity, mBPerFluidBlob,
			mBPerAoeRadius, fluidBlobSpread);
	}

	@Override
	protected void writePropertiesToNetwork(FluidShellProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.bigCannonProperties().toNetwork(buf);
		properties.fuze().toNetwork(buf);
		buf.writeVarInt(properties.fluidShellCapacity())
			.writeVarInt(properties.mBPerFluidBlob())
			.writeVarInt(properties.mBPerAoeRadius())
			.writeFloat(properties.fluidBlobSpread());
	}

	@Override protected FluidShellProperties getNoPropertiesValue() { return DEFAULT; }

}
