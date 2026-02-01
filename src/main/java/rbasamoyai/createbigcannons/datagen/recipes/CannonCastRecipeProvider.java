package rbasamoyai.createbigcannons.datagen.recipes;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class CannonCastRecipeProvider extends BlockRecipeProvider {

	public CannonCastRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
		this.info = CreateBigCannons.resource("cannon_casting");
	}

	@Override
	protected void registerRecipes(BiConsumer<ResourceLocation, BlockRecipe> cons) {
		TagKey<Fluid> castIronTag = fluidTag("molten_cast_iron");

		builder(CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.get())
		.castingShape(CannonCastShape.VERY_SMALL)
		.ingredient(castIronTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.get())
		.castingShape(CannonCastShape.MEDIUM)
		.ingredient(castIronTag)
		.save(cons);

		builder(CBCBlocks.CAST_IRON_CANNON_END.get())
		.castingShape(CannonCastShape.CANNON_END)
		.ingredient(castIronTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.get())
		.castingShape(CannonCastShape.SLIDING_BREECH)
		.ingredient(castIronTag)
		.save(cons);

		TagKey<Fluid> bronzeTag = fluidTag("molten_bronze");

		builder(CBCBlocks.UNBORED_BRONZE_CANNON_BARREL.get())
		.castingShape(CannonCastShape.VERY_SMALL)
		.ingredient(bronzeTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER.get())
		.castingShape(CannonCastShape.MEDIUM)
		.ingredient(bronzeTag)
		.save(cons);

		builder(CBCBlocks.BRONZE_CANNON_END.get())
		.castingShape(CannonCastShape.CANNON_END)
		.ingredient(bronzeTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH.get())
		.castingShape(CannonCastShape.SLIDING_BREECH)
		.ingredient(bronzeTag)
		.save(cons);

		TagKey<Fluid> steelTag = fluidTag("molten_steel");

		builder(CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.VERY_SMALL)
		.ingredient(steelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.SMALL)
		.ingredient(steelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.MEDIUM)
		.ingredient(steelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.LARGE)
		.ingredient(steelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.VERY_LARGE)
		.ingredient(steelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_STEEL_SLIDING_BREECH.get())
		.castingShape(CannonCastShape.SLIDING_BREECH)
		.ingredient(steelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_STEEL_SCREW_BREECH.get())
		.castingShape(CannonCastShape.SCREW_BREECH)
		.ingredient(steelTag)
		.save(cons);

		TagKey<Fluid> nethersteelTag = fluidTag("molten_nethersteel");

		builder(CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.VERY_SMALL)
		.ingredient(nethersteelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.SMALL)
		.ingredient(nethersteelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.MEDIUM)
		.ingredient(nethersteelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.LARGE)
		.ingredient(nethersteelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.castingShape(CannonCastShape.VERY_LARGE)
		.ingredient(nethersteelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH.get())
		.castingShape(CannonCastShape.SCREW_BREECH)
		.ingredient(nethersteelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BREECH.get())
		.castingShape(CannonCastShape.AUTOCANNON_BREECH)
		.ingredient(castIronTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING.get())
		.castingShape(CannonCastShape.AUTOCANNON_RECOIL_SPRING)
		.ingredient(castIronTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BARREL.get())
		.castingShape(CannonCastShape.AUTOCANNON_BARREL)
		.ingredient(castIronTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BREECH.get())
		.castingShape(CannonCastShape.AUTOCANNON_BREECH)
		.ingredient(bronzeTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING.get())
		.castingShape(CannonCastShape.AUTOCANNON_RECOIL_SPRING)
		.ingredient(bronzeTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BARREL.get())
		.castingShape(CannonCastShape.AUTOCANNON_BARREL)
		.ingredient(bronzeTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BREECH.get())
		.castingShape(CannonCastShape.AUTOCANNON_BREECH)
		.ingredient(steelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING.get())
		.castingShape(CannonCastShape.AUTOCANNON_RECOIL_SPRING)
		.ingredient(steelTag)
		.save(cons);

		builder(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BARREL.get())
		.castingShape(CannonCastShape.AUTOCANNON_BARREL)
		.ingredient(steelTag)
		.save(cons);
	}

	protected Builder builder(Block result) { return new Builder(result); }

	protected static class Builder {
		private CannonCastShape shape = null;
		private FluidIngredient ingredient = null;
		private final Block result;

		private Builder(Block result) {
            this.result = result;
		}

		public Builder castingShape(CannonCastShape shape) {
			this.shape = shape;
			return this;
		}

		public Builder ingredient(Fluid ingredient) {
			this.ingredient = IndexPlatform.fluidIngredientFrom(ingredient, 1);
			return this;
		}

		public Builder ingredient(TagKey<Fluid> ingredient) {
			this.ingredient = IndexPlatform.fluidIngredientFrom(ingredient, 1);
			return this;
		}

        public void save(BiConsumer<ResourceLocation, BlockRecipe> cons) {
            this.save(cons, CBCRegistryUtils.getBlockLocation(this.result));
        }

		public void save(BiConsumer<ResourceLocation, BlockRecipe> cons, ResourceLocation id) {
			Objects.requireNonNull(this.shape, "Recipe " + id + " has no casting shape specified");
			Objects.requireNonNull(this.ingredient, "Recipe " + id + " has no fluid ingredient specified");
			Objects.requireNonNull(this.result, "Recipe " + id + " has no result specified");
			cons.accept(id, new CannonCastingRecipe(this.shape, this.ingredient, this.result));
		}
	}

	private static TagKey<Fluid> fluidTag(String path) {
		return TagKey.create(CBCRegistryUtils.getFluidRegistryKey(), ResourceLocation.fromNamespaceAndPath("c", path));
	}

}
