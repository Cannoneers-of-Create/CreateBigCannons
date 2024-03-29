package rbasamoyai.createbigcannons.compat.emi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.emi.DoubleItemIcon;
import com.simibubi.create.compat.emi.recipes.DeployingEmiRecipe;
import com.simibubi.create.compat.emi.recipes.fan.FanEmiRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiShapedRecipe;
import dev.emi.emi.recipe.EmiShapelessRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.compat.common_jei.IncompleteCannonBlockRecipe;
import rbasamoyai.createbigcannons.compat.common_jei.MunitionAssemblyRecipes;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;

public class CBCEmiPlugin implements EmiPlugin {

	private static final Map<ResourceLocation, EmiRecipeCategory> ALL_CATEGORIES = new HashMap<>();

	public static final EmiRecipeCategory
		MELTING = registerInternal("melting", DoubleItemIcon.of(AllBlocks.BASIN.get(), CBCBlocks.BASIN_FOUNDRY_LID.get())),
		BUILT_UP_HEATING = registerInternal("built_up_heating", EmiStack.of(CBCBlocks.CANNON_BUILDER.get())),
		CANNON_CASTING = registerInternal("cannon_casting", EmiStack.of(CBCBlocks.CASTING_SAND.get())),
		DRILL_BORING = registerInternal("drill_boring", EmiStack.of(CBCBlocks.CANNON_DRILL.get())),
		INCOMPLETE_CANNON_BLOCKS = registerInternal("incomplete_cannon_blocks", EmiStack.of(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH.get()));

	@Override
	public void register(EmiRegistry registry) {
		for (Map.Entry<ResourceLocation, EmiRecipeCategory> entry : ALL_CATEGORIES.entrySet()) {
			registry.addCategory(entry.getValue());
		}

		registry.addWorkstation(MELTING, EmiStack.of(CBCBlocks.BASIN_FOUNDRY_LID.get()));
		registry.addWorkstation(MELTING, EmiStack.of(AllBlocks.BASIN.get()));

		// TODO: add other moving blocks when reworking cannon builder
		registry.addWorkstation(BUILT_UP_HEATING, EmiStack.of(CBCBlocks.CANNON_BUILDER.get()));
		registry.addWorkstation(BUILT_UP_HEATING, FanEmiRecipe.getFan("fan_blasting"));

		registry.addWorkstation(CANNON_CASTING, EmiStack.of(CBCBlocks.CASTING_SAND.get()));

		registry.addWorkstation(DRILL_BORING, EmiStack.of(CBCBlocks.CANNON_DRILL.get()));
		registry.addWorkstation(DRILL_BORING, EmiStack.of(AllBlocks.MECHANICAL_BEARING.get()));
		registry.addWorkstation(DRILL_BORING, EmiStack.of(AllBlocks.WINDMILL_BEARING.get()));

		this.addAllItemRecipes(registry, CBCRecipeTypes.MELTING, MELTING, MeltingEmiRecipe::new);
		this.addAllBlockRecipes(registry, BlockRecipeType.BUILT_UP_HEATING, BUILT_UP_HEATING, (category, recipe) -> new BuiltUpHeatingEmiRecipe(category, recipe, 177, 103));
		this.addAllBlockRecipes(registry, BlockRecipeType.CANNON_CASTING, CANNON_CASTING, (category, recipe) -> new CannonCastingEmiRecipe(category, recipe, 177, 103));
		this.addAllBlockRecipes(registry, BlockRecipeType.DRILL_BORING, DRILL_BORING, (category, recipe) -> new DrillBoringEmiRecipe(category, recipe, 177, 77));

		for (IncompleteCannonBlockRecipe r : IncompleteCannonBlockRecipe.makeAllIncompleteRecipes()) {
			registry.addRecipe(new IncompleteCannonBlockEmiRecipe(INCOMPLETE_CANNON_BLOCKS, r, 177, 60));
		}

		List<CraftingRecipe> munitionCraftingRecipes = new ArrayList<>();
		munitionCraftingRecipes.addAll(MunitionAssemblyRecipes.getFuzingRecipes());
		munitionCraftingRecipes.addAll(MunitionAssemblyRecipes.getAutocannonRoundRecipes());
		munitionCraftingRecipes.addAll(MunitionAssemblyRecipes.getBigCartridgeFillingRecipe());
		munitionCraftingRecipes.addAll(MunitionAssemblyRecipes.getTracerRecipes());
		this.addHardcodedCraftingRecipes(registry, munitionCraftingRecipes);

		List<DeployerApplicationRecipe> munitionDeployerRecipes = new ArrayList<>();
		munitionDeployerRecipes.addAll(MunitionAssemblyRecipes.getFuzingDeployerRecipes());
		munitionDeployerRecipes.addAll(MunitionAssemblyRecipes.getAutocannonRoundDeployerRecipes());
		munitionDeployerRecipes.addAll(MunitionAssemblyRecipes.getBigCartridgeDeployerRecipe());
		munitionDeployerRecipes.addAll(MunitionAssemblyRecipes.getTracerDeployerRecipes());
		for (DeployerApplicationRecipe r : munitionDeployerRecipes) {
			registry.addRecipe(new DeployingEmiRecipe(r));
		}
	}

	private static EmiRecipeCategory registerInternal(String id, EmiRenderable icon) {
		ResourceLocation loc = CreateBigCannons.resource(id);
		EmiRecipeCategory category = new EmiRecipeCategory(loc, icon);
		ALL_CATEGORIES.put(loc, category);
		return category;
	}

	private void addHardcodedCraftingRecipes(EmiRegistry registry, Collection<CraftingRecipe> recipes) {
		for (CraftingRecipe r : recipes) {
			if (r instanceof ShapedRecipe shaped) {
				registry.addRecipe(new EmiShapedRecipe(shaped));
			} else if (r instanceof ShapelessRecipe shapeless) {
				registry.addRecipe(new EmiShapelessRecipe(shapeless));
			} else {
				List<EmiIngredient> ingredients = new ArrayList<>();
				for (Ingredient i : r.getIngredients()) {
					ingredients.add(EmiIngredient.of(i));
				}
				registry.addRecipe(new EmiCraftingRecipe(ingredients, EmiStack.of(r.getResultItem()), r.getId()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Recipe<?>> void addAllItemRecipes(EmiRegistry registry, CBCRecipeTypes type, Function<T, EmiRecipe> constructor) {
		for (T recipe : (Collection<T>) registry.getRecipeManager().getAllRecipesFor(type.getType())) {
			registry.addRecipe(constructor.apply(recipe));
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Recipe<?>> void addAllItemRecipes(EmiRegistry registry, CBCRecipeTypes type, EmiRecipeCategory category,
														 BiFunction<EmiRecipeCategory, T, EmiRecipe> constructor) {
		for (T recipe : (Collection<T>) registry.getRecipeManager().getAllRecipesFor(type.getType())) {
			registry.addRecipe(constructor.apply(category, recipe));
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends BlockRecipe> void addAllBlockRecipes(EmiRegistry registry, BlockRecipeType<?> type, Function<T, EmiRecipe> constructor) {
		for (T recipe : (Collection<T>) BlockRecipesManager.getRecipesOfType(type)) {
			registry.addRecipe(constructor.apply(recipe));
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends BlockRecipe> void addAllBlockRecipes(EmiRegistry registry, BlockRecipeType<T> type, EmiRecipeCategory category,
															BiFunction<EmiRecipeCategory, T, EmiRecipe> constructor) {
		for (T recipe : (Collection<T>) BlockRecipesManager.getRecipesOfType(type)) {
			registry.addRecipe(constructor.apply(category, recipe));
		}
	}

}
