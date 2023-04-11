package rbasamoyai.createbigcannons.datagen.values;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.CBCTags;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BlockHardnessProvider implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

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
	public final void run(CachedOutput cache) throws IOException {
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
		String s = GSON.toJson(obj);
		HashCode s1 = Hashing.sha1().hashUnencodedChars(s);
		cache.writeIfNeeded(path, s.getBytes(), s1);
	}

	protected void registerHardnesses() {
		setHardness(CBCTags.BlockCBC.OBSIDIAN, 12);
		setHardness(Blocks.CRYING_OBSIDIAN, 12);
		setHardness(BlockTags.ANVIL, 6);
		setHardness(AllBlocks.ITEM_VAULT.get(), 6);
		setHardness(CBCTags.BlockCBC.SANDSTONE, 4.5);
		setHardness(CBCTags.BlockCBC.CONCRETE, 4.5);
		setHardness(CBCTags.BlockCBC.NETHERRACK, 3);
		setHardness(BlockTags.TERRACOTTA, 4.5);
	}

	protected final void setHardness(Block block, double hardness) { this.blocks.put(block, hardness); }
	protected final void setHardness(TagKey<Block> tag, double hardness) { this.tags.put(tag, hardness); }
	protected final void setHardness(ResourceLocation loc, double hardness) { this.locs.put(loc, hardness); }

	@Override public String getName() { return "Custom block hardness: " + this.modid; }

}
