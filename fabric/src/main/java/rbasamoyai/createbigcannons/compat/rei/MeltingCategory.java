package rbasamoyai.createbigcannons.compat.rei;

import com.simibubi.create.compat.rei.category.BasinCategory;
import com.simibubi.create.compat.rei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import net.minecraft.client.gui.GuiGraphics;
import rbasamoyai.createbigcannons.compat.rei.animated.BasinFoundryLidElement;

public class MeltingCategory extends BasinCategory {

	private final BasinFoundryLidElement lid = new BasinFoundryLidElement();

	private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

	public MeltingCategory(Info<BasinRecipe> info, boolean needsHeating) {
		super(info, needsHeating);
	}

	@Override
	public void draw(BasinRecipe recipe, GuiGraphics graphics, double mouseX, double mouseY) {
		super.draw(recipe, graphics, mouseX, mouseY);

		int dw = this.getDisplayWidth(null);

		HeatCondition heatCondition = recipe.getRequiredHeat();
		if (heatCondition != HeatCondition.NONE) {
			this.heater.withHeat(heatCondition.visualizeAsBlazeBurner())
				.draw(graphics, dw / 2 + 3, 55);
		}
		this.lid.render(graphics, dw / 2 + 3, 34, 0);
	}

}
