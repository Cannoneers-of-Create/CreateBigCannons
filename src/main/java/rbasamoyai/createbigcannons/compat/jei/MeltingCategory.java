package rbasamoyai.createbigcannons.compat.jei;

import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;
import rbasamoyai.createbigcannons.compat.jei.animated.BasinFoundryLidElement;

public class MeltingCategory extends BasinCategory {

	private final BasinFoundryLidElement lid = new BasinFoundryLidElement();
	private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

	public MeltingCategory(Info<BasinRecipe> info) {
		super(info, true);
	}

	@Override
	public void draw(BasinRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);

		HeatCondition heatCondition = recipe.getRequiredHeat();
		if (heatCondition != HeatCondition.NONE) {
			this.heater.withHeat(heatCondition.visualizeAsBlazeBurner())
				.draw(graphics, this.getBackground().getWidth() / 2 + 3, 55);
		}
		this.lid.draw(graphics, getBackground().getWidth() / 2 + 3, 34);
	}

}
