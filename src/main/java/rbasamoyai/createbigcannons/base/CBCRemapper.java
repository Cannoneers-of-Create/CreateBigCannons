package rbasamoyai.createbigcannons.base;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;

@Mod.EventBusSubscriber(modid = CreateBigCannons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CBCRemapper {
	
	private static final Map<String, ResourceLocation> REMAP = new HashMap<>();
	
	static {
		REMAP.put("cannon_cast_wand", CBCItems.CANNON_CRAFTING_WAND.getId());
		REMAP.put("nether_gunmetal_cannon_barrel", CBCBlocks.NETHERSTEEL_CANNON_BARREL.getId());
		REMAP.put("nether_gunmetal_cannon_chamber", CBCBlocks.NETHERSTEEL_CANNON_CHAMBER.getId());
		REMAP.put("nether_gunmetal_screw_breech", CBCBlocks.NETHERSTEEL_SCREW_BREECH.getId());
		REMAP.put("unbored_sliding_breech_cast_mould", CBCBlocks.SLIDING_BREECH_CAST_MOULD.getId());
	}
	
	@SubscribeEvent
	public static void onRemapItem(RegistryEvent.MissingMappings<Item> event) {
		for (Mapping<Item> mapping : event.getMappings(CreateBigCannons.MOD_ID)) {
			ResourceLocation key = mapping.key;
			String path = key.getPath();
			ResourceLocation remapLoc = REMAP.get(path);
			if (remapLoc == null) continue;
			Item remapped = ForgeRegistries.ITEMS.getValue(remapLoc);
			if (remapped == null) continue;
			CreateBigCannons.LOGGER.warn("Remapping item '{}' to '{}'", key, remapLoc);
			try {
				mapping.remap(remapped);
			} catch (Throwable t) {
				CreateBigCannons.LOGGER.warn("Remapping item '{}' to '{}' failed: {}", key, remapLoc, t);
			}
		}
	}
	
	@SubscribeEvent
	public static void onRemapBlock(RegistryEvent.MissingMappings<Block> event) {
		for (Mapping<Block> mapping : event.getMappings(CreateBigCannons.MOD_ID)) {
			ResourceLocation key = mapping.key;
			String path = key.getPath();
			ResourceLocation remapLoc = REMAP.get(path);
			if (remapLoc == null) continue;
			Block remapped = ForgeRegistries.BLOCKS.getValue(remapLoc);
			if (remapped == null) continue;
			CreateBigCannons.LOGGER.warn("Remapping block '{}' to '{}'", key, remapLoc);
			try {
				mapping.remap(remapped);
			} catch (Throwable t) {
				CreateBigCannons.LOGGER.warn("Remapping block '{}' to '{}' failed: {}", key, remapLoc, t);
			}
		}
	}
	
}
