package rbasamoyai.createbigcannons.datagen.values;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.google.gson.JsonObject;
import com.simibubi.create.AllBlocks;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import rbasamoyai.createbigcannons.CBCTags;

public class BlockHardnessProvider extends CBCDataProvider {

	private final Map<Block, Double> blocks = new LinkedHashMap<>();
	private final Map<TagKey<Block>, Double> tags = new LinkedHashMap<>();
	private final Map<ResourceLocation, Double> locs = new LinkedHashMap<>();

	public BlockHardnessProvider(String modid, DataGenerator gen) {
		super(modid, gen, "block_hardness");
	}

	@Override
	protected final void generateData(BiConsumer<ResourceLocation, JsonObject> cons) {
		this.addEntries();
		for (Map.Entry<Block, Double> entry : this.blocks.entrySet()) {
			cons.accept(Registry.BLOCK.getKey(entry.getKey()), writeHardness(entry.getValue()));
		}
		for (Map.Entry<ResourceLocation, Double> entry : this.locs.entrySet()) {
			cons.accept(entry.getKey(), writeHardness(entry.getValue()));
		}
		for (Map.Entry<TagKey<Block>, Double> entry : this.tags.entrySet()) {
			ResourceLocation srcLoc = entry.getKey().location();
			ResourceLocation tagLoc = new ResourceLocation(srcLoc.getNamespace(), "tags/" + srcLoc.getPath());
			cons.accept(tagLoc, writeHardness(entry.getValue()));
		}
	}

	protected void addEntries() {
		setHardness(CBCTags.CBCBlockTags.OBSIDIAN, 12);
		setHardness(Blocks.CRYING_OBSIDIAN, 12);
		setHardness(BlockTags.ANVIL, 6);
		setHardness(AllBlocks.ITEM_VAULT.get(), 6);
		setHardness(CBCTags.CBCBlockTags.SANDSTONE, 4.5);
		setHardness(CBCTags.CBCBlockTags.CONCRETE, 4.5);
		setHardness(CBCTags.CBCBlockTags.NETHERRACK, 3);
		setHardness(BlockTags.TERRACOTTA, 4.5);
	}

	protected final void setHardness(Block block, double hardness) {
		this.blocks.put(block, hardness);
	}

	protected final void setHardness(TagKey<Block> tag, double hardness) {
		this.tags.put(tag, hardness);
	}

	protected final void setHardness(ResourceLocation loc, double hardness) {
		this.locs.put(loc, hardness);
	}

	private static JsonObject writeHardness(double v) {
		JsonObject obj = new JsonObject();
		obj.addProperty("block_hardness", v);
		return obj;
	}

	@Override
	public String getName() {
		return "Custom block hardness: " + this.modid;
	}

}
