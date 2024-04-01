package rbasamoyai.createbigcannons.compat.emi;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.emi.CreateEmiAnimations;
import com.simibubi.create.compat.emi.recipes.basin.BasinEmiRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.widget.WidgetHolder;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class MeltingEmiRecipe extends BasinEmiRecipe {

	public MeltingEmiRecipe(EmiRecipeCategory category, BasinRecipe recipe) {
		super(category, recipe, true);
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		super.addWidgets(widgets);

		HeatCondition requiredHeat = recipe.getRequiredHeat();
		if (requiredHeat != HeatCondition.NONE) {
			CreateEmiAnimations.addBlazeBurner(widgets, widgets.getWidth() / 2 + 3, 55, requiredHeat.visualizeAsBlazeBurner());
		}
		widgets.addDrawable(widgets.getWidth() / 2 + 3, 34, 0, 0, ((graphics, mouseX, mouseY, delta) -> {
			PoseStack poseStack = graphics.pose();
			poseStack.pushPose();
			poseStack.translate(0, 0, 200);
			poseStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
			poseStack.mulPose(Axis.YP.rotationDegrees(22.5f));
			int scale = 23;

			CreateEmiAnimations.blockElement(CBCBlocks.BASIN_FOUNDRY_LID.getDefaultState())
				.atLocal(0, 0.65, 0)
				.scale(scale)
				.render(graphics);

			CreateEmiAnimations.blockElement(AllBlocks.BASIN.getDefaultState())
				.atLocal(0, 1.65, 0)
				.scale(scale)
				.render(graphics);

			poseStack.popPose();
		}));
	}

}
