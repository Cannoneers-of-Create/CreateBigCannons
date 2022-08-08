package rbasamoyai.createbigcannons.datagen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCFluids;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.CannonCastingRecipe;

public class CannonCastRecipeGen implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	
	private final DataGenerator gen;
	protected final String modid;
	
	CannonCastRecipeGen(DataGenerator gen) {
		this(CreateBigCannons.MOD_ID, gen);
	}
	
	public CannonCastRecipeGen(String modid, DataGenerator gen) {
		this.modid = modid;
		this.gen = gen;
	}
	
	@Override
	public void run(HashCache cache) throws IOException {
		Path path = this.gen.getOutputFolder();
		Set<ResourceLocation> set = new HashSet<>();
		this.registerRecipes(recipe -> {
			if (!set.add(recipe.id())) {
				throw new IllegalStateException("Duplicate casting recipe " + recipe.id());
			} else {
				saveRecipe(cache, recipe.serializeRecipe(), path.resolve("data/" + recipe.id().getNamespace() + "/block_recipes/" + recipe.id().getPath() + ".json"));
			}
		});
	}
	
	private static void saveRecipe(HashCache cache, JsonObject obj, Path path) {
		try {
			String s = GSON.toJson(obj);
			String s1 = SHA1.hashUnencodedChars(s).toString();
			if (!Objects.equals(cache.getHash(path), s1) || !Files.exists(path)) {
				Files.createDirectories(path.getParent());
				BufferedWriter writer = Files.newBufferedWriter(path);
				
				try {
					writer.write(s);
				} catch (Throwable throwable) {
					if (writer != null) {
						try {
							writer.close();
						} catch (Throwable throwable1) {
							throwable.addSuppressed(throwable1);
						}
					}
					throw throwable;
				}
				
				if (writer != null) {
					writer.close();
				}
			}
			
			cache.putNew(path, s1);
		} catch (IOException e) {
			LOGGER.error("Couldn't save casting recipe {}", path, e);
		}
	}
	
	protected void registerRecipes(Consumer<CannonCastingRecipe> cons) {
		cons.accept(recipe(CannonCastShape.VERY_SMALL, FluidIngredient.fromFluid(CBCFluids.MOLTEN_CAST_IRON.get(), 1), CBCBlocks.CAST_IRON_CANNON_BARREL.get(), "cast_iron_barrel_cast"));
	}
	
	protected CannonCastingRecipe recipe(CannonCastShape shape, FluidIngredient ingredient, Block result, String name) {
		return new CannonCastingRecipe(shape, ingredient, result, new ResourceLocation(this.modid, name)); 
	}

	@Override
	public String getName() {
		return "Cannon Casting Recipes: " + this.modid;
	}

}
