package rbasamoyai.createbigcannons.datagen.values;

import java.util.LinkedHashMap;
import java.util.Map;

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

	public BlockHardnessProvider(String modid, String name, DataGenerator gen) {
		super(modid, name, gen, "block_hardness");
	}

	public BlockHardnessProvider(String modid, DataGenerator gen) {
		this(modid, "default", gen);
	}

	@Override
	protected void generateData() {
		setHardness(CBCTags.BlockCBC.OBSIDIAN, 12);
		setHardness(Blocks.CRYING_OBSIDIAN, 12);
		setHardness(BlockTags.ANVIL, 6);
		setHardness(AllBlocks.ITEM_VAULT.get(), 6);
		setHardness(CBCTags.BlockCBC.SANDSTONE, 4.5);
		setHardness(CBCTags.BlockCBC.CONCRETE, 4.5);
		setHardness(CBCTags.BlockCBC.NETHERRACK, 3);
		setHardness(BlockTags.TERRACOTTA, 4.5);
	}

	@Override
	protected void write(JsonObject obj) {
		for (Map.Entry<Block, Double> entry : this.blocks.entrySet()) {
			obj.addProperty(Registry.BLOCK.getKey(entry.getKey()).toString(), entry.getValue());
		}
		for (Map.Entry<ResourceLocation, Double> entry : this.locs.entrySet()) {
			obj.addProperty(entry.getKey().toString(), entry.getValue());
		}
		for (Map.Entry<TagKey<Block>, Double> entry : this.tags.entrySet()) {
			obj.addProperty("#" + entry.getKey().location(), entry.getValue());
		}
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

	@Override
	public String getName() {
		return "Custom block hardness: " + this.modid;
	}

}
