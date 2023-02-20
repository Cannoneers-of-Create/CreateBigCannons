package rbasamoyai.createbigcannons.datagen.values;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BlockHardnessProvider implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();

	private final DataGenerator gen;
	protected final String modid;
	protected final String name;

	private final Map<Block, Double> blocks = new HashMap<>();
	private final Map<TagKey<Block>, Double> tags = new HashMap<>();
	private final Map<ResourceLocation, Double> locs = new HashMap<>();

	public BlockHardnessProvider(String modid, String name, DataGenerator gen) {
		this.modid = modid;
		this.name = name;
		this.gen = gen;
	}

	public BlockHardnessProvider(String modid, DataGenerator gen) {
		this(modid, "default", gen);
	}

	@Override
	public final void run(CachedOutput cache) {
		this.registerHardnesses();

		JsonObject obj = new JsonObject();
		for (Map.Entry<Block, Double> entry : this.blocks.entrySet()) {
			obj.addProperty(Registry.BLOCK.getKey(entry.getKey()).toString(), entry.getValue());
		}
		for (Map.Entry<ResourceLocation, Double> entry : this.locs.entrySet()) {
			obj.addProperty(entry.getKey().toString(), entry.getValue());
		}
		for (Map.Entry<TagKey<Block>, Double> entry : this.tags.entrySet()) {
			obj.addProperty("#" + entry.getKey().location(), entry.getValue());
		}

		Path path = this.gen.getOutputFolder().resolve("data/" + this.modid + "/block_hardness/" + this.name + ".json");
		try {
			DataProvider.saveStable(cache, obj, path);
		} catch (IOException e) {
			LOGGER.error("Couldn't save block hardness values to {}", path, e);
		}
	}

	protected void registerHardnesses() {
		setHardness(Tags.Blocks.OBSIDIAN, 12);
		setHardness(Blocks.CRYING_OBSIDIAN, 12);
		setHardness(BlockTags.ANVIL, 6);
	}

	protected final void setHardness(Block block, double hardness) { this.blocks.put(block, hardness); }
	protected final void setHardness(TagKey<Block> tag, double hardness) { this.tags.put(tag, hardness); }
	protected final void setHardness(ResourceLocation loc, double hardness) { this.locs.put(loc, hardness); }

	@Override public String getName() { return "Custom block hardness: " + this.modid; }

}
