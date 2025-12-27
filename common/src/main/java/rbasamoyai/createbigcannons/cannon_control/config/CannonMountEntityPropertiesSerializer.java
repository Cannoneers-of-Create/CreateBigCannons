package rbasamoyai.createbigcannons.cannon_control.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;

public interface CannonMountEntityPropertiesSerializer<T extends CannonMountEntityPropertiesProvider> {

	T fromJson(EntityType<?> entityType, ICannonContraptionType contraptionType, JsonObject obj);
	void toNetwork(T properties, FriendlyByteBuf buf);
	T fromNetwork(FriendlyByteBuf buf);

}
