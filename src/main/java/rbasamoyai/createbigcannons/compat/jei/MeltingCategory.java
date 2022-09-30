package rbasamoyai.createbigcannons.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.contraptions.processing.BasinRecipe;
import com.simibubi.create.content.contraptions.processing.HeatCondition;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import rbasamoyai.createbigcannons.compat.jei.animated.BasinFoundryLidElement;

public class MeltingCategory extends BasinCategory {

	private final BasinFoundryLidElement lid = new BasinFoundryLidElement();
	private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();
	
	public MeltingCategory(Info<BasinRecipe> info) {
		super(info, true);
	}
	
	@Override
	public void draw(BasinRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
		super.draw(recipe, recipeSlotsView, matrixStack, mouseX, mouseY);
		
		HeatCondition heatCondition = recipe.getRequiredHeat();
		if (heatCondition != HeatCondition.NONE) {
			this.heater.withHeat(heatCondition.visualizeAsBlazeBurner())
				.draw(matrixStack, this.getBackground().getWidth() / 2 + 3, 55);
		}
		this.lid.draw(matrixStack, getBackground().getWidth() / 2 + 3, 34);
	}

}
