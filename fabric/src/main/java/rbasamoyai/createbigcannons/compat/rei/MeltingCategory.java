package rbasamoyai.createbigcannons.compat.rei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.rei.category.BasinCategory;
import com.simibubi.create.compat.rei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import rbasamoyai.createbigcannons.compat.rei.animated.BasinFoundryLidElement;

public class MeltingCategory extends BasinCategory {

	private final BasinFoundryLidElement lid = new BasinFoundryLidElement();

	private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

	public MeltingCategory(Info<BasinRecipe> info, boolean needsHeating) {
		super(info, needsHeating);
	}

	@Override
	public void draw(BasinRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY) {
		super.draw(recipe, matrixStack, mouseX, mouseY);

		int dw = this.getDisplayWidth(null);

		HeatCondition heatCondition = recipe.getRequiredHeat();
		if (heatCondition != HeatCondition.NONE) {
			this.heater.withHeat(heatCondition.visualizeAsBlazeBurner())
				.draw(matrixStack, dw / 2 + 3, 55);
		}
		this.lid.render(matrixStack, dw / 2 + 3, 34, 0);
	}

}
