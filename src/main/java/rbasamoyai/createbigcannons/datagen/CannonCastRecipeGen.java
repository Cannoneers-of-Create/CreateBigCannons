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
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCFluids;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;

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
		builder("cast_iron_cannon_barrel")
		.castingShape(CannonCastShape.VERY_SMALL)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.get())
		.save(cons);
		
		builder("cast_iron_cannon_chamber")
		.castingShape(CannonCastShape.MEDIUM)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.get())
		.castingTime(1800)
		.save(cons);
		
		builder("cast_iron_cannon_end")
		.castingShape(CannonCastShape.CANNON_END)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.CAST_IRON_CANNON_END.get())
		.castingTime(1500)
		.save(cons);
		
		builder("cast_iron_sliding_breech")
		.castingShape(CannonCastShape.UNBORED_SLIDING_BREECH)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.get())
		.castingTime(1500)
		.save(cons);
	}
	
	protected Builder builder(String name) {
		return new Builder(name);
	}

	@Override
	public String getName() {
		return "Cannon Casting Recipes: " + this.modid;
	}
	
	private class Builder {
		private final ResourceLocation id;
		
		private CannonCastShape shape = null;
		private FluidIngredient ingredient = null;
		private int castingTime = 1200;
		private Block result = null;
		
		private Builder(String name) {
			this.id = new ResourceLocation(CannonCastRecipeGen.this.modid, name);
		}
		
		public Builder castingShape(CannonCastShape shape) {
			this.shape = shape;
			return this;
		}
		
		public Builder ingredient(Fluid ingredient) {
			this.ingredient = FluidIngredient.fromFluid(ingredient, 1);
			return this;
		}
		
		public Builder ingredient(TagKey<Fluid> ingredient) {
			this.ingredient = FluidIngredient.fromTag(ingredient, 1);
			return this;
		}
		
		public Builder castingTime(int castingTime) {
			this.castingTime = castingTime;
			return this;
		}
		
		public Builder result(Block result) {
			this.result = result;
			return this;
		}
		
		public void save(Consumer<CannonCastingRecipe> cons) {
			Objects.requireNonNull(this.shape, "Recipe " + this.id.toString() + " has no casting shape specified");
			Objects.requireNonNull(this.ingredient, "Recipe " + this.id.toString() + " has no fluid ingredient specified");
			Objects.requireNonNull(this.result, "Recipe " + this.id.toString() + " has no result specified");
			cons.accept(new CannonCastingRecipe(this.shape, this.ingredient, this.result, this.castingTime, this.id));
		}
		
	}

}
