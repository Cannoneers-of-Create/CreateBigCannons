package rbasamoyai.createbigcannons.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;

public class CannonCastingCategory extends CBCBlockRecipeCategory<CannonCastingRecipe> {

	public CannonCastingCategory(Info<CannonCastingRecipe> info) {
		super(info);
	}
	
	@Override
	public void draw(CannonCastingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
	}
	
}
