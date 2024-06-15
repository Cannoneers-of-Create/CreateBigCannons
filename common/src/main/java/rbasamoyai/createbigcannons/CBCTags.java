package rbasamoyai.createbigcannons;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class CBCTags {

	public static class CBCBlockTags {
		public static final TagKey<Block>
			THICK_TUBING = makeTag("thick_tubing"),
			REDUCES_SPREAD = makeTag("reduces_spread"),
			DRILL_CAN_PASS_THROUGH = makeTag("drill_can_pass_through"),
			DEFLECTS_SHOTS = makeTag("deflects_shots"),
			DOESNT_DEFLECT_SHOTS = makeTag("doesnt_deflect_shots"),
			BOUNCES_SHOTS = makeTag("bounces_shots"),
			DOESNT_BOUNCE_SHOTS = makeTag("doesnt_bounce_shots"),
			// Datagen tags
			OBSIDIAN = commonTag("obsidian", "obsidian", "obsidian" /* No Fabric c: tag */),
			SANDSTONE = commonTag("sandstone", "sandstone", "sandstone"),
			CONCRETE = commonTag("concrete", "concrete", "concrete"),
			NETHERRACK = commonTag("netherrack", "netherrack", "netherrack");

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
				((TagsProvider<Block>) prov).tag(tag).add(blocks);
			});
		}

		public static void addBlocksToBlockTag(TagKey<Block> tag, Supplier<List<? extends Block>> blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = ((TagsProvider<Block>) prov).tag(tag);
				for (Block b : blocks.get()) {
					app.add(b);
				}
			});
		}

		@SafeVarargs
		public static void addTagsToBlockTag(TagKey<Block> tag, TagKey<Block>... tags) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = ((TagsProvider<Block>) prov).tag(tag);
				for (TagKey<Block> t : tags) {
					app.addTag(t);
				}
			});
		}

		public static void addOptionalTagsToBlockTag(TagKey<Block> tag, List<ResourceLocation> ops) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = ((TagsProvider<Block>) prov).tag(tag);
				ops.forEach(app::addOptionalTag);
			});
		}

		public static void sectionRegister() {
			addBlocksToBlockTag(CONCRETE, Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE, Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE);
		}
	}

	public static class CBCItemTags {
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
			GELATINIZERS = makeTag("gelatinizers");

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
				((TagsProvider<Item>) prov).tag(tag).add(items);
			});
		}

		public static void addItemsToItemTag(TagKey<Item> tag, ItemLike... items) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = ((TagsProvider<Item>) prov).tag(tag);
				for (ItemLike bp : items) {
					app.add(bp.asItem());
				}
			});
		}

		@SafeVarargs
		public static void addTagsToItemTag(TagKey<Item> tag, TagKey<Item>... tags) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = ((TagsProvider<Item>) prov).tag(tag);
				for(TagKey<Item> t : tags) {
					app.addTag(t);
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

}
