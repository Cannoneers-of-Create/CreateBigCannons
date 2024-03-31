package rbasamoyai.createbigcannons.datagen.recipes;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public abstract class BlockRecipeProvider implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	private final DataGenerator gen;
	protected final String modid;
	protected ResourceLocation info;

	public BlockRecipeProvider(String modid, DataGenerator gen) {
		this.modid = modid;
		this.gen = gen;
	}

	protected static final List<BlockRecipeProvider> GENERATORS = new ArrayList<>();

	public static void registerAll(DataGenerator gen) {
		GENERATORS.add(new CannonCastRecipeProvider(gen));
		GENERATORS.add(new BuiltUpHeatingRecipeProvider(gen));
		GENERATORS.add(new DrillBoringRecipeProvider(gen));

		gen.addProvider(true, new DataProvider() {
			@Override
			public void run(CachedOutput cache) throws IOException {
				GENERATORS.forEach(gen -> {
					try {
						gen.run(cache);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}

			@Override
			public String getName() {
				return "Create Big Cannons Block Recipes";
			}
		});
	}

	@Override
	public void run(CachedOutput cache) throws IOException {
		Path path = this.gen.getOutputFolder();
		Set<ResourceLocation> set = new HashSet<>();
		this.registerRecipes(recipe -> {
			if (!set.add(recipe.id())) {
				throw new IllegalStateException("Duplicate block recipe " + recipe.id());
			} else {
				saveRecipe(cache, recipe.serializeRecipe(), path.resolve("data/" + recipe.id().getNamespace() + "/block_recipes/" + recipe.id().getPath() + ".json"));
			}
		});
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
		return "Create Big Cannons Block Recipes: " + (this.info == null ? "unknown entityId" : this.info);
	}

}
