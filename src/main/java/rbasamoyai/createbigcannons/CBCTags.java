package rbasamoyai.createbigcannons;

import java.util.Arrays;
import java.util.List;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.providers.ProviderType;

import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class CBCTags {

	private static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate();
	
	public static class BlockCBC {
		public static final TagKey<Block>
			THICK_TUBING = createAndGenerateBlockTag(CreateBigCannons.resource("thick_tubing")),
			SHRAPNEL_DESTROYS = createAndGenerateBlockTag(CreateBigCannons.resource("shrapnel_shatterable")),
			GRAPESHOT_DESTROYS = createAndGenerateBlockTag(CreateBigCannons.resource("grapeshot_shatterable"));
		
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
		
		public static void addBlocksToBlockTag(TagKey<Block> tag, BlockProvider... blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = prov.tag(tag);
				for (BlockProvider bp : blocks) {
					app.add(bp.get());
				}
			});
		}
		
		public static void addTagsToBlockTag(TagKey<Block> tag, List<TagKey<Block>> tags) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = prov.tag(tag);
				tags.forEach(app::addTag);
			});
		}
		
		public static void sectionRegister() {
			addTagsToBlockTag(SHRAPNEL_DESTROYS, Arrays.asList(Tags.Blocks.GLASS, Tags.Blocks.GLASS_PANES, BlockTags.LEAVES));
			addBlocksToBlockTag(SHRAPNEL_DESTROYS, Blocks.FLOWER_POT);
			
			addTagsToBlockTag(GRAPESHOT_DESTROYS, Arrays.asList(SHRAPNEL_DESTROYS, BlockTags.PLANKS, BlockTags.WOODEN_SLABS, BlockTags.WOODEN_STAIRS, BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.LOGS));
			addBlocksToBlockTag(GRAPESHOT_DESTROYS, Blocks.MELON, Blocks.PUMPKIN);
		}
		
		@FunctionalInterface public interface BlockProvider { Block get(); }
	}
		
	public static void register() {
		BlockCBC.sectionRegister();
	}
	
}
