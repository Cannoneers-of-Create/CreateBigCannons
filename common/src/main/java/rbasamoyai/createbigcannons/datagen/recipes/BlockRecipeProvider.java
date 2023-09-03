package rbasamoyai.createbigcannons.datagen.recipes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;

import org.slf4j.Logger;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class BlockRecipeProvider implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	private final PackOutput output;
	protected final String modid;
	protected ResourceLocation info;

	public BlockRecipeProvider(String modid, PackOutput output) {
		this.modid = modid;
		this.output	= output;
	}

	protected static final List<DataProvider.Factory<BlockRecipeProvider>> GENERATORS = new ArrayList<>();

	public static void registerAll(DataGenerator.PackGenerator gen) {
		GENERATORS.add(CannonCastRecipeProvider::new);
		GENERATORS.add(BuiltUpHeatingRecipeProvider::new);
		GENERATORS.add(DrillBoringRecipeProvider::new);

		gen.addProvider(output -> new DataProvider() {
			@Override
			public CompletableFuture<?> run(CachedOutput cache) {
				return CompletableFuture.allOf(GENERATORS.stream().map(gen -> {
					try {
						return gen.create(output).run(cache);
					} catch (Exception e) {
						throw e;
					}
				}).toArray(i -> new CompletableFuture[i]));
			}

			@Override
			public String getName() {
				return "Create Big Cannons Block Recipes";
			}
		});
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Path path = this.output.getOutputFolder();
		Map<ResourceLocation, FinishedBlockRecipe> map = new HashMap<>();
		this.registerRecipes(recipe -> {
			if (map.put(recipe.id(), recipe) != null) {
				throw new IllegalStateException("Duplicate block recipe " + recipe.id());
			}
		});
		return CompletableFuture.allOf(map.entrySet().stream()
			.map(e -> {
				ResourceLocation id = e.getKey();
				FinishedBlockRecipe recipe = e.getValue();
				return DataProvider.saveStable(cache, recipe.serializeRecipe(), path.resolve("data/" + id.getNamespace() + "/block_recipes/" + id.getPath() + ".json"));
			}).toArray(i -> new CompletableFuture[i]));
	}

	private static void saveRecipe(CachedOutput cache, JsonObject obj, Path path) {
		try {
			String s = GSON.toJson(obj);
			HashCode s1 = Hashing.sha1().hashUnencodedChars(s);
			cache.writeIfNeeded(path, s.getBytes(), s1);
		} catch (IOException e) {
			LOGGER.error("Couldn't save block recipe {}", path, e);
		}
	}

	protected abstract void registerRecipes(Consumer<FinishedBlockRecipe> cons);

	@Override
	public String getName() {
		return "Create Big Cannons Block Recipes: " + (this.info == null ? "unknown id" : this.info);
	}

}
