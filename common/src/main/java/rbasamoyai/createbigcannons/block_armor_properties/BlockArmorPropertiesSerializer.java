package rbasamoyai.createbigcannons.block_armor_properties;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;

public interface BlockArmorPropertiesSerializer<T extends BlockArmorPropertiesProvider> {

	T loadBlockArmorPropertiesFromJson(Block block, JsonObject obj);
	void toNetwork(T properties, FriendlyByteBuf buf);
	T fromNetwork(FriendlyByteBuf buf);

}
