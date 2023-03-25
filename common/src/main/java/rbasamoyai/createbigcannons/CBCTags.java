package rbasamoyai.createbigcannons;

import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

public class CBCTags {
	
	public static class BlockCBC {
		public static final TagKey<Block>
			THICK_TUBING = makeTag("thick_tubing"),
			REDUCES_SPREAD = makeTag("reduces_spread"),
			WEAK_CANNON_END = makeTag("weak_cannon_end"),
			DRILL_CAN_PASS_THROUGH = makeTag("drill_can_pass_through"),
			DEFLECTS_SHOTS = makeTag("deflects_shots"),
			DOESNT_DEFLECT_SHOTS = makeTag("doesnt_deflect_shots"),
			BOUNCES_SHOTS = makeTag("bounces_shots"),
			DOESNT_BOUNCE_SHOTS = makeTag("doesnt_bounce_shots"),
			// Datagen tags
			OBSIDIAN = commonTag("obsidian", "obsidian", "obsidian" /* No Fabric c: tag */);
		
		public static TagKey<Block> makeTag(String path) {
			TagKey<Block> tag = TagKey.create(Registry.BLOCK_REGISTRY, CreateBigCannons.resource(path));
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag));
			return tag;
		}

		public static TagKey<Block> commonTag(String mainPath, String forgePath, String fabricPath) {
			TagKey<Block> mainTag = makeTag(mainPath);
			addOptionalTagsToBlockTag(mainTag, Arrays.asList(
					new ResourceLocation("forge", forgePath),
					new ResourceLocation("c", fabricPath)));
			return mainTag;
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
		
		public static void addTagsToBlockTag(TagKey<Block> tag, List<TagKey<Block>> tags) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = prov.tag(tag);
				tags.forEach(app::addTag);
			});
		}

		public static void addOptionalTagsToBlockTag(TagKey<Block> tag, List<ResourceLocation> ops) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = prov.tag(tag);
				ops.forEach(app::addOptionalTag);
			});
		}
		
		public static void sectionRegister() {}
	}
	
	public static class ItemCBC {
		public static final TagKey<Item>
			IMPACT_FUZE_HEAD = makeTag("impact_fuze_head"),
			NUGGET_CAST_IRON = commonTag("nugget_cast_iron", "nuggets/cast_iron", "cast_iron_nuggets"),
			INGOT_CAST_IRON = commonTag("ingot_cast_iron", "ingots/cast_iron", "cast_iron_ingots"),
			BLOCK_CAST_IRON = commonTag("block_cast_iron", "storage_blocks/cast_iron", "cast_iron_blocks"),
			NUGGET_BRONZE = commonTag("nugget_bronze", "nuggets/bronze", "bronze_nuggets"),
			INGOT_BRONZE = commonTag("ingot_bronze", "ingots/bronze", "bronze_ingots"),
			BLOCK_BRONZE = commonTag("block_bronze", "storage_blocks/bronze", "bronze_blocks"),
			NUGGET_STEEL = commonTag("nugget_steel", "nuggets/steel", "steel_blocks"),
			INGOT_STEEL = commonTag("ingot_steel", "ingots/steel", "steel_ingots"),
			BLOCK_STEEL = commonTag("block_steel", "storage_blocks/steel", "steel_blocks"),

			// Crafting tags
			INGOT_IRON = commonTag("ingot_iron", "ingots/iron", "iron_ingots"),
			NUGGET_IRON = commonTag("nugget_iron", "nuggets/iron", "iron_nuggets"),
			SHEET_IRON = commonTag("sheet_iron", "plate/iron", "iron_plates"),
			GUNPOWDER = commonTag("gunpowder", "gunpowder", "gunpowder" /* No fabric c: tag */),
			GEMS_QUARTZ = commonTag("gems_quartz", "gems/quartz", "quartz"),
			DUSTS_REDSTONE = commonTag("dusts_redstone", "dusts/redstone", "redstone_dusts"),
			STONE = commonTag("stone", "stone", "stone");

		
		public static TagKey<Item> makeTag(String loc) {
			TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, CreateBigCannons.resource(loc));
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag));
			return tag;
		}

		public static TagKey<Item> commonTag(String mainPath, String forgePath, String fabricPath) {
			TagKey<Item> mainTag = makeTag(mainPath);
			addOptionalTagsToItemTag(mainTag, Arrays.asList(
					new ResourceLocation("forge", forgePath),
					new ResourceLocation("c", fabricPath)));
			return mainTag;
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

		public static void addOptionalTagsToItemTag(TagKey<Item> tag, List<ResourceLocation> ops) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = prov.tag(tag);
				ops.forEach(app::addOptionalTag);
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
