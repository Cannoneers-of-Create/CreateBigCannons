package rbasamoyai.createbigcannons.block_armor_properties;

import com.google.gson.JsonObject;

import net.minecraft.world.level.block.Block;

public interface BlockArmorPropertiesSerializer {

	BlockArmorPropertiesProvider loadBlockArmorPropertiesFromJson(Block block, String id, JsonObject obj);

}
