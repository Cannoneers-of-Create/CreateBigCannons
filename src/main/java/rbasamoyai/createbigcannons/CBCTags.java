package rbasamoyai.createbigcannons;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.simibubi.create.foundation.block.CopperBlockSet;
import com.simibubi.create.foundation.utility.Iterate;
import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.base.tag_utils.ForcedTagEntry;
import rbasamoyai.createbigcannons.mixin.TagAppenderAccessor;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class CBCTags {

	public static class CBCBlockTags {
		public static final TagKey<Block>
			THICK_TUBING = makeTag("thick_tubing"),
			REDUCES_SPREAD = makeTag("reduces_spread"),
			DRILL_CAN_PASS_THROUGH = makeTag("drill_can_pass_through"),
			// Datagen tags
			OBSIDIAN = commonTag("obsidian", "obsidian", "obsidian" /* No Fabric c: tag */),
			SANDSTONE = commonTag("sandstone", "sandstone", "sandstone"),
			CONCRETE = commonTag("concrete", "concrete", "concrete"),
			NETHERRACK = commonTag("netherrack", "netherrack", "netherrack"),
			SPARK_EFFECT_ON_IMPACT = makeTag("spark_effect_on_impact"),
			SPLINTER_EFFECT_ON_IMPACT = makeTag("splinter_effect_on_impact"),
			GLASS_EFFECT_ON_IMPACT = makeTag("glass_effect_on_impact");

		public static TagKey<Block> makeTag(String path) {
			TagKey<Block> tag = TagKey.create(CBCRegistryUtils.getBlockRegistryKey(), CreateBigCannons.resource(path));
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> ((TagsProvider<Block>) prov).tag(tag));
			return tag;
		}

		public static TagKey<Block> commonTag(String mainPath, String forgePath, String fabricPath) {
			TagKey<Block> mainTag = makeTag(mainPath);
			addOptionalTagsToBlockTag(mainTag, Arrays.asList(
					CBCUtils.location("forge", forgePath),
					CBCUtils.location("c", forgePath), // For forge -> fabric ports, e.g. Create
					CBCUtils.location("c", fabricPath)));
			return mainTag;
		}

		public static void addBlocksToBlockTag(TagKey<Block> tag, Block... blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = ((TagsProvider<Block>) prov).tag(tag);
				for (Block block : blocks) {
					CBCRegistryUtils.getBlockRegistry().getResourceKey(block).ifPresent(app::add);
				}
			});
		}

		public static void addBlocksToBlockTag(TagKey<Block> tag, Supplier<List<? extends Block>> blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = ((TagsProvider<Block>) prov).tag(tag);
				for (Block b : blocks.get()) {
					CBCRegistryUtils.getBlockRegistry().getResourceKey(b).ifPresent(app::add);
				}
			});
		}

		@SafeVarargs
		public static void addBlocksToBlockTag(TagKey<Block> tag, Supplier<? extends Block>... blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = ((TagsProvider<Block>) prov).tag(tag);
				for (Supplier<? extends Block> b : blocks)
					CBCRegistryUtils.getBlockRegistry().getResourceKey(b.get()).ifPresent(app::add);
			});
		}

		@SafeVarargs
		public static void addTagsToBlockTag(TagKey<Block> tag, TagKey<Block>... tags) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = ((TagsProvider<Block>) prov).tag(tag);
				for (TagKey<Block> t : tags)
					addTag(app, t);
			});
		}

		public static void addOptionalTagsToBlockTag(TagKey<Block> tag, List<ResourceLocation> ops) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = ((TagsProvider<Block>) prov).tag(tag);
				ops.forEach(app::addOptionalTag);
			});
		}

		public static void addOptionalTagsToBlockTag(TagKey<Block> tag, ResourceLocation... ops) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = ((TagsProvider<Block>) prov).tag(tag);
				for (ResourceLocation op : ops)
					app.addOptionalTag(op);
			});
		}

		public static void sectionRegister() {
			addBlocksToBlockTag(CONCRETE, Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE, Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE);
			addBlocksToBlockTag(SPARK_EFFECT_ON_IMPACT, Blocks.IRON_BLOCK, Blocks.IRON_DOOR, Blocks.IRON_BARS, Blocks.IRON_TRAPDOOR,
				Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.RAW_IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.RAW_GOLD_BLOCK,
				Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.NETHERITE_BLOCK, Blocks.ANCIENT_DEBRIS, Blocks.RAW_COPPER_BLOCK,
				Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER,
				Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER,
				Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS,
				Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB,
				Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER,
				Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER,
				Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
				Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB);
			addBlocksToBlockTag(SPARK_EFFECT_ON_IMPACT, AllBlocks.ZINC_BLOCK, AllBlocks.ANDESITE_ALLOY_BLOCK,
				AllBlocks.INDUSTRIAL_IRON_BLOCK, AllBlocks.RAILWAY_CASING, AllBlocks.BRASS_BLOCK, AllBlocks.ANDESITE_BARS,
				AllBlocks.BRASS_BARS, AllBlocks.COPPER_BARS, AllBlocks.METAL_GIRDER, AllBlocks.METAL_GIRDER_ENCASED_SHAFT,
				AllBlocks.ITEM_VAULT, AllBlocks.FLUID_TANK, AllBlocks.FLUID_PIPE, AllBlocks.FLUID_VALVE, AllBlocks.MECHANICAL_PUMP);
			iterateOverCopperSet(AllBlocks.COPPER_SHINGLES, SPARK_EFFECT_ON_IMPACT);
			iterateOverCopperSet(AllBlocks.COPPER_TILES, SPARK_EFFECT_ON_IMPACT);

			addTagsToBlockTag(SPLINTER_EFFECT_ON_IMPACT, BlockTags.PLANKS, BlockTags.LOGS, BlockTags.WOODEN_STAIRS,
				BlockTags.WOODEN_SLABS, BlockTags.WOODEN_DOORS, BlockTags.WOODEN_TRAPDOORS, BlockTags.WOODEN_FENCES,
				BlockTags.FENCE_GATES, BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.WOODEN_BUTTONS);
			addBlocksToBlockTag(SPLINTER_EFFECT_ON_IMPACT, AllBlocks.ANDESITE_CASING, AllBlocks.COPPER_CASING, AllBlocks.BRASS_CASING,
				AllBlocks.ANDESITE_ENCASED_SHAFT, AllBlocks.ANDESITE_ENCASED_COGWHEEL, AllBlocks.ANDESITE_ENCASED_LARGE_COGWHEEL,
				AllBlocks.BRASS_ENCASED_SHAFT, AllBlocks.BRASS_ENCASED_COGWHEEL, AllBlocks.BRASS_ENCASED_LARGE_COGWHEEL,
				AllBlocks.ENCASED_FLUID_PIPE, AllBlocks.ENCASED_CHAIN_DRIVE, AllBlocks.ADJUSTABLE_CHAIN_GEARSHIFT,
				AllBlocks.COGWHEEL, AllBlocks.LARGE_COGWHEEL, AllBlocks.WATER_WHEEL, AllBlocks.LARGE_WATER_WHEEL,
				AllBlocks.WATER_WHEEL_STRUCTURAL, AllBlocks.LINEAR_CHASSIS, AllBlocks.SECONDARY_LINEAR_CHASSIS,
				AllBlocks.RADIAL_CHASSIS, AllBlocks.ANDESITE_DOOR, AllBlocks.COPPER_DOOR, AllBlocks.BRASS_DOOR);

			addOptionalTagsToBlockTag(GLASS_EFFECT_ON_IMPACT, CBCUtils.location("c", "glass"), CBCUtils.location("forge", "glass"),
				CBCUtils.location("c", "glass_panes"), CBCUtils.location("forge", "glass_panes"));
			addBlocksToBlockTag(GLASS_EFFECT_ON_IMPACT, AllBlocks.FRAMED_GLASS_DOOR, AllBlocks.FRAMED_GLASS_TRAPDOOR,
				AllPaletteBlocks.OAK_WINDOW, AllPaletteBlocks.OAK_WINDOW_PANE, AllPaletteBlocks.SPRUCE_WINDOW, AllPaletteBlocks.SPRUCE_WINDOW_PANE,
				AllPaletteBlocks.BIRCH_WINDOW, AllPaletteBlocks.BIRCH_WINDOW_PANE, AllPaletteBlocks.JUNGLE_WINDOW, AllPaletteBlocks.JUNGLE_WINDOW_PANE,
				AllPaletteBlocks.DARK_OAK_WINDOW, AllPaletteBlocks.DARK_OAK_WINDOW_PANE, AllPaletteBlocks.ACACIA_WINDOW, AllPaletteBlocks.ACACIA_WINDOW_PANE,
				AllPaletteBlocks.WARPED_WINDOW, AllPaletteBlocks.WARPED_WINDOW_PANE, AllPaletteBlocks.CRIMSON_WINDOW, AllPaletteBlocks.CRIMSON_WINDOW_PANE,
				AllPaletteBlocks.ORNATE_IRON_WINDOW, AllPaletteBlocks.ORNATE_IRON_WINDOW_PANE);
		}

		private static void iterateOverCopperSet(CopperBlockSet set, TagKey<Block> tag) {
			for (boolean waxed : Iterate.falseAndTrue) {
				for (CopperBlockSet.Variant<?> variant : set.getVariants()) {
					for (WeatherState weathering : WeatherState.values()) {
						addBlocksToBlockTag(tag, set.get(variant, weathering, waxed));
					}
				}
			}
		}
	}

	public static class CBCItemTags {
		// TODO: Update fabric ore tags to use fabric-convention-tags in 1.21
		public static final TagKey<Item>
			IMPACT_FUZE_HEAD = makeTag("impact_fuze_head"),
			NUGGET_CAST_IRON = commonTag("nugget_cast_iron", "nuggets/cast_iron", "cast_iron_nuggets"),
			INGOT_CAST_IRON = commonTag("ingot_cast_iron", "ingots/cast_iron", "cast_iron_ingots"),
			BLOCK_CAST_IRON = commonTag("block_cast_iron", "storage_blocks/cast_iron", "cast_iron_blocks"),
			NUGGET_BRONZE = commonTag("nugget_bronze", "nuggets/bronze", "bronze_nuggets"),
			INGOT_BRONZE = commonTag("ingot_bronze", "ingots/bronze", "bronze_ingots"),
			BLOCK_BRONZE = commonTag("block_bronze", "storage_blocks/bronze", "bronze_blocks"),
			NUGGET_STEEL = commonTag("nugget_steel", "nuggets/steel", "steel_nuggets"),
			INGOT_STEEL = commonTag("ingot_steel", "ingots/steel", "steel_ingots"),
			BLOCK_STEEL = commonTag("block_steel", "storage_blocks/steel", "steel_blocks"),
			NUGGET_NETHERSTEEL = commonTag("nugget_nethersteel", "nuggets/nethersteel", "nethersteel_nuggets"),
			INGOT_NETHERSTEEL = commonTag("ingot_nethersteel", "ingots/nethersteel", "nethersteel_ingots"),
			BLOCK_NETHERSTEEL = commonTag("block_nethersteel", "blocks/nethersteel", "nethersteel_blocks"),

			// Crafting tags
			INGOT_IRON = commonTag("ingot_iron", "ingots/iron", "iron_ingots"),
			NUGGET_IRON = commonTag("nugget_iron", "nuggets/iron", "iron_nuggets"),
			SHEET_IRON = commonTag("sheet_iron", "plates/iron", "iron_plates"),
			NUGGET_COPPER = commonTag("nugget_copper", "nuggets/copper", "copper_nuggets"),
			INGOT_COPPER = commonTag("ingot_copper", "ingots/copper", "copper_ingots"),
			GUNPOWDER = commonTag("gunpowder", "gunpowder", "gunpowder" /* No fabric c: tag */),
			GEMS_QUARTZ = commonTag("gems_quartz", "gems/quartz", "quartz"),
			DUSTS_REDSTONE = commonTag("dusts_redstone", "dusts/redstone", "redstone_dusts"),
			STONE = commonTag("stone", "stone", "stone"),
			SHEET_BRASS = commonTag("sheet_brass", "plates/brass", "brass_plates"),
			INGOT_BRASS = commonTag("ingot_brass", "ingots/brass", "brass_ingots"),
			SHEET_COPPER = commonTag("sheet_copper", "plates/copper", "copper_plates"),
			SHEET_GOLD = commonTag("sheet_copper", "plates/gold", "gold_plates"),
			SHEET_STEEL = commonTag("sheet_steel", "plates/steel", "steel_plates"),
			DUST_GLOWSTONE = commonTag("dust_glowstone", "dusts/glowstone", "glowstone_dusts"),
			GLASS = commonTag("glass", "glass", "glass"),
			INEXPENSIVE_BIG_CARTRIDGE_SHEET = makeTag("inexpensive_big_cartridge_sheet"),
			NITROPOWDER = makeTag("nitropowder"),
			BIG_CANNON_PROPELLANT = makeTag("big_cannon_propellant"),
			BIG_CANNON_PROPELLANT_BAGS = makeTag("big_cannon_propellant_bags"),
			BIG_CANNON_CARTRIDGES = makeTag("big_cannon_cartridges"),
			BIG_CANNON_PROJECTILES = makeTag("big_cannon_projectiles"),
			AUTOCANNON_AMMO_CONTAINERS = makeTag("autocannon_ammo_containers"),
			FUZES = makeTag("fuzes"),
			SPENT_AUTOCANNON_CASINGS = makeTag("spent_autocannon_casings"),
			AUTOCANNON_CARTRIDGES = makeTag("autocannon_cartridges"),
			AUTOCANNON_ROUNDS = makeTag("autocannon_rounds"),
			GUNPOWDER_PINCH = makeTag("gunpowder_pinch"),
			GUNCOTTON = makeTag("guncotton"),
			CAN_BE_NITRATED = makeTag("can_be_nitrated"),
			HIGH_EXPLOSIVE_MATERIALS = makeTag("high_explosive_materials"),
			NITRO_ACIDIFIERS = makeTag("nitro_acidifiers"),
			GELATINIZERS = makeTag("gelatinizers"),
			GAS_MASKS = makeTag("gas_masks");

		public static TagKey<Item> makeTag(String loc) {
			TagKey<Item> tag = CBCRegistryUtils.createItemTag(CreateBigCannons.resource(loc));
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> ((TagsProvider<Item>) prov).tag(tag));
			return tag;
		}

		public static TagKey<Item> commonTag(String mainPath, String forgePath, String fabricPath) {
			TagKey<Item> mainTag = makeTag(mainPath);
			addOptionalTagsToItemTag(mainTag, Arrays.asList(
					CBCUtils.location("forge", forgePath),
					CBCUtils.location("c", forgePath), // For forge -> fabric ports, e.g. Create
					CBCUtils.location("c", fabricPath)));
			return mainTag;
		}

		public static void addItemsToItemTag(TagKey<Item> tag, Item... items) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = ((TagsProvider<Item>) prov).tag(tag);
				for (Item item : items) {
					CBCRegistryUtils.getItemRegistry().getResourceKey(item).ifPresent(app::add);
				}
			});
		}

		public static void addItemsToItemTag(TagKey<Item> tag, ItemLike... items) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = ((TagsProvider<Item>) prov).tag(tag);
				for (ItemLike bp : items) {
					CBCRegistryUtils.getItemRegistry().getResourceKey(bp.asItem()).ifPresent(app::add);
				}
			});
		}

		@SafeVarargs
		public static void addTagsToItemTag(TagKey<Item> tag, TagKey<Item>... tags) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = ((TagsProvider<Item>) prov).tag(tag);
				for(TagKey<Item> t : tags) {
					addTag(app, t);
				}
			});
		}

		public static void addIdsToItemTag(TagKey<Item> tag, ResourceLocation... ids) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = ((TagsProvider<Item>) prov).tag(tag);
				for (ResourceLocation id : ids) {
					app.addOptional(id);
				}
			});
		}

		public static void addOptionalTagsToItemTag(TagKey<Item> tag, List<ResourceLocation> ops) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = ((TagsProvider<Item>) prov).tag(tag);
				ops.forEach(app::addOptionalTag);
			});
		}

		public static void sectionRegister() {
			addItemsToItemTag(IMPACT_FUZE_HEAD, Items.STONE_BUTTON, Items.POLISHED_BLACKSTONE_BUTTON);
			addIdsToItemTag(BLOCK_BRONZE, alloyed("bronze_block"));
			addIdsToItemTag(BLOCK_STEEL, alloyed("steel_block"));
			addIdsToItemTag(BLOCK_CAST_IRON, createdeco("cast_iron_block"));
			addTagsToItemTag(INEXPENSIVE_BIG_CARTRIDGE_SHEET, SHEET_GOLD, SHEET_COPPER);
			addTagsToItemTag(BIG_CANNON_PROPELLANT, BIG_CANNON_PROPELLANT_BAGS, BIG_CANNON_CARTRIDGES);
			addItemsToItemTag(CAN_BE_NITRATED, Items.PAPER);
			addItemsToItemTag(GELATINIZERS, Items.SLIME_BALL);
			addTagsToItemTag(NITRO_ACIDIFIERS, DUSTS_REDSTONE);
		}
	}

	public static class CBCFluidTags {
		public static final TagKey<Fluid>
			MOLTEN_METAL = makeTag("molten_metal");

		public static TagKey<Fluid> makeTag(String loc) {
			TagKey<Fluid> tag = TagKey.create(CBCRegistryUtils.getFluidRegistryKey(), CreateBigCannons.resource(loc));
			REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, prov -> ((TagsProvider<Fluid>) prov).tag(tag));
			return tag;
		}
	}

	public static void register() {
		CBCBlockTags.sectionRegister();
		CBCItemTags.sectionRegister();
	}

	private static ResourceLocation alloyed(String path) { return CBCUtils.location("alloyed", path); }
	private static ResourceLocation createdeco(String path) { return CBCUtils.location("createdeco", path); }

	private static <T> void addTag(TagAppender<T> app, TagKey<T> tag) {
		TagAppenderAccessor accessor = (TagAppenderAccessor) app;
		TagBuilder builder = accessor.getBuilder();
		builder.add(new ForcedTagEntry(TagEntry.tag(tag.location())));
	}

}
