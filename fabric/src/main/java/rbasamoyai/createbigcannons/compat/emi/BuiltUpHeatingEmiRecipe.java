package rbasamoyai.createbigcannons.compat.emi;

import static com.simibubi.create.compat.emi.recipes.CreateEmiRecipe.addSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.emi.CreateEmiAnimations;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;

public class BuiltUpHeatingEmiRecipe extends CBCEmiBlockRecipe<BuiltUpHeatingRecipe> {

	public BuiltUpHeatingEmiRecipe(EmiRecipeCategory category, BuiltUpHeatingRecipe recipe, int width, int height) {
		super(category, recipe, width, height);
		this.inputs = new ArrayList<>();
		for (BlockRecipeIngredient layer : recipe.layers()) {
			List<ItemStack> blockItems = layer.getBlockItems();
			this.inputs.add(EmiIngredient.of(Ingredient.of(blockItems.stream())));
		}
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		Set<BlockRecipeIngredient> layers = this.recipe.layers();

		int i = 0;
		int base = this.width / 2 - 11 * layers.size() + 1;

		for (EmiIngredient ingredient : this.inputs) {
			addSlot(widgets, ingredient, base + i * 22, 5);
			++i;
		}
		addSlot(widgets, EmiIngredient.of(Ingredient.of(this.recipe.getResultBlock())), this.width / 2 - 10, 74).recipeContext(this);

		widgets.addDrawable(0, 0, 0, 0, ((poseStack, mouseX, mouseY, delta) -> {
			int scale = 24;
			AllGuiTextures.JEI_SHADOW.render(poseStack, 23, 55);
			AllGuiTextures.JEI_SHADOW.render(poseStack, 99, 55);
			AllGuiTextures.JEI_LIGHT.render(poseStack, 118, 65);
			CBCGuiTextures.CANNON_BUILDING_ARROW.render(poseStack, 82, 34);

			poseStack.pushPose();
			poseStack.translate(33, 59, 0);
			poseStack.mulPose(Vector3f.XP.rotationDegrees(-12.5f));
			poseStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
			CreateEmiAnimations.defaultBlockElement(CBCBlocks.CANNON_BUILDER.getDefaultState().setValue(CannonBuilderBlock.STATE, CannonBuilderBlock.BuilderState.ACTIVATED))
				.rotateBlock(0, 180, 0)
				.atLocal(0, 0, 0)
				.scale(scale)
				.render(poseStack);

			CreateEmiAnimations.defaultBlockElement(AllBlocks.SHAFT.getDefaultState())
				.rotateBlock(0, CreateEmiAnimations.getCurrentAngle(), -90)
				.atLocal(0, 0, 0)
				.scale(scale)
				.render(poseStack);
			poseStack.popPose();

			poseStack.pushPose();
			poseStack.translate(109, 59, 0);
			poseStack.mulPose(Vector3f.XP.rotationDegrees(-12.5f));
			poseStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));

			CreateEmiAnimations.defaultBlockElement(AllPartialModels.ENCASED_FAN_INNER)
				.rotateBlock(180, 0, CreateEmiAnimations.getCurrentAngle() * 16)
				.scale(scale)
				.render(poseStack);

			CreateEmiAnimations.defaultBlockElement(AllBlocks.ENCASED_FAN.getDefaultState())
				.rotateBlock(0, 180, 0)
				.atLocal(0, 0, 0)
				.scale(scale)
				.render(poseStack);
			poseStack.popPose();

			poseStack.pushPose();
			poseStack.translate(109, 59, 0);
			poseStack.mulPose(Vector3f.XP.rotationDegrees(-12.5f));
			poseStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));

			GuiGameElement.of(Fluids.LAVA)
				.rotateBlock(0, 180, 0)
				.atLocal(0, 0, 2)
				.scale(scale)
				.render(poseStack);
			poseStack.popPose();
		}));
	}

}
