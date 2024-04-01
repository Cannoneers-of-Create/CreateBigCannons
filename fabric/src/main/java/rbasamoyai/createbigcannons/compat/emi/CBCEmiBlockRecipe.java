package rbasamoyai.createbigcannons.compat.emi;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;

public abstract class CBCEmiBlockRecipe<T extends BlockRecipe> implements EmiRecipe {

	protected final EmiRecipeCategory category;
	protected final T recipe;
	protected final ResourceLocation id;

	protected List<EmiIngredient> inputs = List.of();
	protected List<EmiStack> output;
	protected int width;
	protected int height;

	protected CBCEmiBlockRecipe(EmiRecipeCategory category, T recipe, int width, int height) {
		this.category = category;
		this.recipe = recipe;
		this.id = recipe.getId();
		this.width = width;
		this.height = height;
		this.output = List.of(EmiStack.of(recipe.getResultBlock()));
	}

	@Override public EmiRecipeCategory getCategory() { return this.category; }

	@Nullable
	@Override
	public ResourceLocation getId() { return this.id; }

	@Override public List<EmiIngredient> getInputs() { return this.inputs; }
	@Override public List<EmiStack> getOutputs() { return this.output; }
	@Override public int getDisplayWidth() { return this.width; }
	@Override public int getDisplayHeight() { return this.height; }

}
