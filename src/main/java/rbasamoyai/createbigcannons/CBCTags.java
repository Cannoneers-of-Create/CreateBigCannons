package rbasamoyai.createbigcannons;

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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

public class CBCTags {
	
	public static class BlockCBC {
		public static final TagKey<Block>
			THICK_TUBING = createAndGenerateBlockTag(CreateBigCannons.resource("thick_tubing")),
			REDUCES_SPREAD = createAndGenerateBlockTag(CreateBigCannons.resource("reduces_spread")),
			WEAK_CANNON_END = createAndGenerateBlockTag(CreateBigCannons.resource("weak_cannon_end")),
			DRILL_CAN_PASS_THROUGH = createAndGenerateBlockTag(CreateBigCannons.resource("drill_can_pass_through")),
			DEFLECTS_SHOTS = createAndGenerateBlockTag(CreateBigCannons.resource("deflects_shots")),
			DOESNT_DEFLECT_SHOTS = createAndGenerateBlockTag(CreateBigCannons.resource("doesnt_deflect_shots")),
			BOUNCES_SHOTS = createAndGenerateBlockTag(CreateBigCannons.resource("bounces_shots")),
			DOESNT_BOUNCE_SHOTS = createAndGenerateBlockTag(CreateBigCannons.resource("doesnt_bounce_shots"));
		
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
		
		public static void sectionRegister() {}
	}
	
	public static class ItemCBC {
		public static final TagKey<Item>
			IMPACT_FUZE_HEAD = createAndGenerateItemTag(CreateBigCannons.resource("impact_fuze_head")),
			NUGGET_CAST_IRON = forgeTag("nuggets/cast_iron"),
			INGOT_CAST_IRON = forgeTag("ingots/cast_iron"),
			BLOCK_CAST_IRON = forgeTag("storage_blocks/cast_iron"),
			NUGGET_BRONZE = forgeTag("nuggets/bronze"),
			INGOT_BRONZE = forgeTag("ingots/bronze"),
			BLOCK_BRONZE = forgeTag("storage_blocks/bronze"),
			NUGGET_STEEL = forgeTag("nuggets/steel"),
			INGOT_STEEL = forgeTag("ingots/steel"),
			BLOCK_STEEL = forgeTag("storage_blocks/steel");
		
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
