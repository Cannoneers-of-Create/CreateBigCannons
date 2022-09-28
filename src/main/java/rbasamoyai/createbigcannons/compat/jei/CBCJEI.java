package rbasamoyai.createbigcannons.compat.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.contraptions.processing.BasinRecipe;
import com.simibubi.create.foundation.utility.Components;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.CBCRecipeTypes;
import rbasamoyai.createbigcannons.crafting.foundry.MeltingRecipe;

@JeiPlugin
public class CBCJEI implements IModPlugin {

	private final List<PackedCategory<?>> allCategories = new ArrayList<>();
	private IIngredientManager ingredientManager;
	
	private void loadCategories() {
		this.allCategories.clear();
		
		Supplier<List<BasinRecipe>> meltingSupplier = () -> {
			List<BasinRecipe> list = new ArrayList<>();
			CreateJEI.<MeltingRecipe>consumeTypedRecipes(list::add, CBCRecipeTypes.MELTING.getType());
			return list;
		};
		List<Supplier<? extends ItemStack>> meltingCatalysts = new ArrayList<>();
		meltingCatalysts.add(CBCBlocks.BASIN_FOUNDRY_LID::asStack);
		meltingCatalysts.add(AllBlocks.BASIN::asStack);
		CreateRecipeCategory.Info<BasinRecipe> meltingInfo = new CreateRecipeCategory.Info<BasinRecipe>(
				new mezz.jei.api.recipe.RecipeType<>(CreateBigCannons.resource("melting"), BasinRecipe.class),
				Components.translatable(CreateBigCannons.MOD_ID + ".recipe.melting"),
				new EmptyBackground(177, 103),
				new DoubleItemIcon(AllBlocks.BASIN::asStack, CBCBlocks.BASIN_FOUNDRY_LID::asStack),
				meltingSupplier,
				meltingCatalysts);
		this.allCategories.add(PackedCategory.packCreateCategory(new MeltingCategory(meltingInfo)));
	}
	
	private static final ResourceLocation PLUGIN_ID = CreateBigCannons.resource("jei_plugin");	
	@Override public ResourceLocation getPluginUid() { return PLUGIN_ID; }
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		this.loadCategories();
		registration.addRecipeCategories(this.allCategories.stream().map(PackedCategory::asCategory).toArray(IRecipeCategory[]::new));
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		this.ingredientManager = registration.getIngredientManager();
		this.allCategories.forEach(c -> c.registerRecipes(registration));
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		this.allCategories.forEach(c -> c.registerCatalysts(registration));
	}
	
	interface PackedCategory<T> {
		IRecipeCategory<T> asCategory();
		RecipeType<T> getType();
		void registerRecipes(IRecipeRegistration reg);
		void registerCatalysts(IRecipeCatalystRegistration reg);
		
		public static <R extends Recipe<?>> PackedCategory<R> packCreateCategory(CreateRecipeCategory<R> cat) {
			return new PackedCategory<R>() {
				@Override public IRecipeCategory<R> asCategory() { return cat; }
				@Override public RecipeType<R> getType() { return cat.getRecipeType(); }
				@Override public void registerRecipes(IRecipeRegistration reg) { cat.registerRecipes(reg); }
				@Override public void registerCatalysts(IRecipeCatalystRegistration reg) { cat.registerCatalysts(reg); }
			};
		}
	}
	
}
