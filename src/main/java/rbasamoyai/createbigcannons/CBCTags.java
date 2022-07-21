package rbasamoyai.createbigcannons;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.providers.ProviderType;

import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CBCTags {

	private static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate();
	
	public static class Blocks {
		public static final TagKey<Block>
			THICK_TUBING = createAndGenerateBlockTag(CreateBigCannons.resource("thick_tubing"));
		
		public static TagKey<Block> createAndGenerateBlockTag(ResourceLocation loc) {
			TagKey<Block> tag = BlockTags.create(loc); 
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag));
			return tag;
		}
		
		public static void addBlocksToBlockTag(TagKey<Block> tag, BlockProvider... blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = prov.tag(tag);
				for (BlockProvider bp : blocks) {
					app.add(bp.get());
				}
			});
		}
		
		public static void sectionRegister() {
			
		}
		
		@FunctionalInterface public interface BlockProvider { Block get(); }
	}
		
	public static void register() {
		Blocks.sectionRegister();
	}
	
}
