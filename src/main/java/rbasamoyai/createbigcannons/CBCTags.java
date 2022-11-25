package rbasamoyai.createbigcannons;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class CBCTags {

	private static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate();
	
	public static class BlockCBC {
		public static final TagKey<Block>
			THICK_TUBING = createAndGenerateBlockTag(CreateBigCannons.resource("thick_tubing")),
			REDUCES_SPREAD = createAndGenerateBlockTag(CreateBigCannons.resource("reduces_spread")),
			SHRAPNEL_SHATTERABLE = createAndGenerateBlockTag(CreateBigCannons.resource("shrapnel_shatterable")),
			SHRAPNEL_VULNERABLE = createAndGenerateBlockTag(CreateBigCannons.resource("shrapnel_vulnerable")),
			GRAPESHOT_SHATTERABLE = createAndGenerateBlockTag(CreateBigCannons.resource("grapeshot_shatterable")),
			GRAPESHOT_VULNERABLE = createAndGenerateBlockTag(CreateBigCannons.resource("grapeshot_vulnerable")),
			WEAK_CANNON_END = createAndGenerateBlockTag(CreateBigCannons.resource("weak_cannon_end")),
			DRILL_CAN_PASS_THROUGH = createAndGenerateBlockTag(CreateBigCannons.resource("drill_can_pass_through"));
		
		public static TagKey<Block> createAndGenerateBlockTag(ResourceLocation loc) {
			TagKey<Block> tag = BlockTags.create(loc); 
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag));
			return tag;
		}
		
		public static void addBlocksToBlockTag(TagKey<Block> tag, Block... blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				prov.tag(tag).add(blocks);
			});
		}
		
		public static void addBlocksToBlockTag(TagKey<Block> tag, Supplier<List<? extends Block>> blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = prov.tag(tag);
				for (Block b : blocks.get()) {
					app.add(b);
				}
			});
		}
		
		public static void addTagsToBlockTag(TagKey<Block> tag, Supplier<List<TagKey<Block>>> tags) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = prov.tag(tag);
				tags.get().forEach(app::addTag);
			});
		}
		
		public static void sectionRegister() {
			addTagsToBlockTag(SHRAPNEL_SHATTERABLE, () -> Arrays.asList(BlockTags.IMPERMEABLE, Tags.Blocks.GLASS_PANES, BlockTags.LEAVES, BlockTags.WOOL, BlockTags.FLOWER_POTS));
			addTagsToBlockTag(SHRAPNEL_SHATTERABLE, () -> Arrays.asList(AllTags.AllBlockTags.WINDMILL_SAILS.tag));
			addBlocksToBlockTag(SHRAPNEL_SHATTERABLE, Blocks.TRIPWIRE);
			addBlocksToBlockTag(SHRAPNEL_SHATTERABLE, () -> Arrays.asList(AllBlocks.FRAMED_GLASS_DOOR.get(), AllBlocks.FRAMED_GLASS_TRAPDOOR.get(), AllBlocks.ROPE.get()));
			
			addTagsToBlockTag(SHRAPNEL_VULNERABLE, () -> Arrays.asList(BlockTags.PLANKS, BlockTags.WOODEN_SLABS, BlockTags.WOODEN_STAIRS, BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.LOGS, BlockTags.WOODEN_DOORS, BlockTags.WOODEN_TRAPDOORS));
			addBlocksToBlockTag(SHRAPNEL_VULNERABLE, Blocks.MELON, Blocks.PUMPKIN);
			addBlocksToBlockTag(SHRAPNEL_VULNERABLE, () -> Arrays.asList(AllBlocks.BELT.get(), AllBlocks.SHAFT.get(), AllBlocks.COGWHEEL.get(), AllBlocks.LARGE_COGWHEEL.get(), AllBlocks.POWERED_SHAFT.get(), AllBlocks.GANTRY_SHAFT.get()));
			addBlocksToBlockTag(SHRAPNEL_VULNERABLE, () -> Arrays.asList(AllBlocks.ANDESITE_CASING.get(), AllBlocks.ANDESITE_ENCASED_COGWHEEL.get(), AllBlocks.ANDESITE_ENCASED_LARGE_COGWHEEL.get(), AllBlocks.ANDESITE_ENCASED_SHAFT.get()));
			addBlocksToBlockTag(SHRAPNEL_VULNERABLE, () -> Arrays.asList(AllBlocks.MECHANICAL_BEARING.get(), AllBlocks.MECHANICAL_DRILL.get(), AllBlocks.MECHANICAL_HARVESTER.get(), AllBlocks.MECHANICAL_MIXER.get(), AllBlocks.MECHANICAL_PISTON.get(), AllBlocks.MECHANICAL_PISTON_HEAD.get(), AllBlocks.MECHANICAL_PLOUGH.get(), AllBlocks.MECHANICAL_PRESS.get(), AllBlocks.MECHANICAL_SAW.get(), AllBlocks.STICKY_MECHANICAL_PISTON.get(), AllBlocks.PISTON_EXTENSION_POLE.get(), AllBlocks.ROPE_PULLEY.get()));
			addBlocksToBlockTag(SHRAPNEL_VULNERABLE, () -> Arrays.asList(AllBlocks.ANDESITE_TUNNEL.get(), AllBlocks.ANDESITE_BELT_FUNNEL.get(), AllBlocks.ANDESITE_FUNNEL.get()));
			
			addTagsToBlockTag(GRAPESHOT_SHATTERABLE, () -> Arrays.asList(SHRAPNEL_SHATTERABLE, SHRAPNEL_VULNERABLE));
			
			addBlocksToBlockTag(GRAPESHOT_VULNERABLE, Blocks.IRON_DOOR, Blocks.IRON_TRAPDOOR, Blocks.IRON_BARS);
			addBlocksToBlockTag(GRAPESHOT_VULNERABLE, () -> Arrays.asList(AllBlocks.BRASS_CASING.get(), AllBlocks.BRASS_ENCASED_COGWHEEL.get(), AllBlocks.BRASS_ENCASED_LARGE_COGWHEEL.get(), AllBlocks.BRASS_ENCASED_SHAFT.get(), AllBlocks.MECHANICAL_CRAFTER.get(), AllBlocks.MECHANICAL_ARM.get()));
			addBlocksToBlockTag(GRAPESHOT_VULNERABLE, () -> Arrays.asList(AllBlocks.COPPER_CASING.get(), AllBlocks.ENCASED_FLUID_PIPE.get(), AllBlocks.FLUID_PIPE.get(), AllBlocks.GLASS_FLUID_PIPE.get(), AllBlocks.FLUID_VALVE.get(), AllBlocks.COPPER_VALVE_HANDLE.get(), AllBlocks.MECHANICAL_PUMP.get(), AllBlocks.HOSE_PULLEY.get()));
			addBlocksToBlockTag(GRAPESHOT_VULNERABLE, () -> Arrays.asList(AllBlocks.BRASS_TUNNEL.get(), AllBlocks.BRASS_BELT_FUNNEL.get(), AllBlocks.BRASS_FUNNEL.get()));
		}
	}
	
	public static class ItemCBC {
		public static final TagKey<Item>
			IMPACT_FUZE_HEAD = createAndGenerateItemTag(CreateBigCannons.resource("impact_fuze_head")),
			NUGGET_CAST_IRON = forgeTag("nuggets/cast_iron"),
			INGOT_CAST_IRON = forgeTag("ingots/cast_iron"),
			BLOCK_CAST_IRON = forgeTag("blocks/cast_iron"),
			NUGGET_BRONZE = forgeTag("nuggets/bronze"),
			INGOT_BRONZE = forgeTag("ingots/bronze"),
			BLOCK_BRONZE = forgeTag("blocks/bronze"),
			NUGGET_STEEL = forgeTag("nuggets/steel"),
			INGOT_STEEL = forgeTag("ingots/steel"),
			BLOCK_STEEL = forgeTag("blocks/steel");
		
		public static TagKey<Item> createAndGenerateItemTag(ResourceLocation loc) {
			TagKey<Item> tag = ItemTags.create(loc); 
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag));
			return tag;
		}
		
		public static TagKey<Item> forgeTag(String path) {
			TagKey<Item> tag = ForgeRegistries.ITEMS.tags().createOptionalTagKey(new ResourceLocation("forge", path), Collections.emptySet());
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag));
			return tag;
		}
		
		public static void addItemsToItemTag(TagKey<Item> tag, Item... items) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				prov.tag(tag).add(items);
			});
		}
		
		public static void addItemsToItemTag(TagKey<Item> tag, ItemLike... items) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = prov.tag(tag);
				for (ItemLike bp : items) {
					app.add(bp.asItem());
				}
			});
		}
		
		public static void addTagsToItemTag(TagKey<Item> tag, List<TagKey<Item>> tags) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = prov.tag(tag);
				tags.forEach(app::addTag);
			});
		}
		
		public static void addIdsToItemTag(TagKey<Item> tag, ResourceLocation... ids) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = prov.tag(tag);
				for (ResourceLocation id : ids) {
					app.addOptional(id);
				}
			});
		}
		
		public static void sectionRegister() {
			addItemsToItemTag(IMPACT_FUZE_HEAD, Items.STONE_BUTTON, Items.POLISHED_BLACKSTONE_BUTTON);
			addIdsToItemTag(BLOCK_BRONZE, alloyed("bronze_block"));
			addIdsToItemTag(BLOCK_STEEL, alloyed("steel_block"));
			addIdsToItemTag(BLOCK_CAST_IRON, createdeco("cast_iron_block"));
		}
	}
		
	public static void register() {
		BlockCBC.sectionRegister();
		ItemCBC.sectionRegister();
	}
	
	private static ResourceLocation alloyed(String path) { return new ResourceLocation("alloyed", path); }
	private static ResourceLocation createdeco(String path) { return new ResourceLocation("createdeco", path); }
	
}
