package rbasamoyai.createbigcannons.compat.jei;

import java.util.List;
import java.util.function.Supplier;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;

public abstract class CBCBlockRecipeCategory<T extends BlockRecipe> implements IRecipeCategory<T> {

	private final RecipeType<T> type;
	private final Component title;
	private final IDrawable background;
	private final IDrawable icon;
	private final Supplier<List<T>> recipes;
	private final List<Supplier<? extends ItemStack>> catalysts;
	
	protected CBCBlockRecipeCategory(Info<T> info) {
		this.type = info.type;
		this.title = info.title;
		this.background = info.background;
		this.icon = info.icon;
		this.recipes = info.recipes;
		this.catalysts = info.catalysts;
	}
	
	@Override public Component getTitle() { return title; }
	@Override public IDrawable getBackground() { return this.background; }
	@Override public IDrawable getIcon() { return this.icon; }
	@Override public ResourceLocation getUid() { return this.type.getUid(); }
	@Override public Class<? extends T> getRecipeClass() { return this.type.getRecipeClass(); }
	@Override public RecipeType<T> getRecipeType() { return this.type; }
	
	public void registerRecipes(IRecipeRegistration reg) {
		reg.addRecipes(this.type, this.recipes.get());
	}
	
	public void registerCatalysts(IRecipeCatalystRegistration reg) {
		this.catalysts.forEach(s -> reg.addRecipeCatalyst(s.get(), this.type));
	}
	
	public static record Info<T extends BlockRecipe>(RecipeType<T> type, Component title, IDrawable background, IDrawable icon, Supplier<List<T>> recipes, List<Supplier<? extends ItemStack>> catalysts) {
	}

	public interface Factory<T extends BlockRecipe> {
		CBCBlockRecipeCategory<T> create(Info<T> info);
	}
	
}
