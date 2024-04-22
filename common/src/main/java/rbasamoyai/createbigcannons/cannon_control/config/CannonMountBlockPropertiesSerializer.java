package rbasamoyai.createbigcannons.cannon_control.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntityType;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;

public interface CannonMountBlockPropertiesSerializer<T extends CannonMountBlockPropertiesProvider> {

	T fromJson(BlockEntityType<?> beType, ICannonContraptionType contraptionType, JsonObject obj);
	void toNetwork(T properties, FriendlyByteBuf buf);
	T fromNetwork(FriendlyByteBuf buf);

}
