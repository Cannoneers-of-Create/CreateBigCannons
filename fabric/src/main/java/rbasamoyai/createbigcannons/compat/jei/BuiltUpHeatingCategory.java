package rbasamoyai.createbigcannons.compat.jei;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

import java.util.Set;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock.BuilderState;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;

public class BuiltUpHeatingCategory extends CBCBlockRecipeCategory<BuiltUpHeatingRecipe> {


	public BuiltUpHeatingCategory(Info<BuiltUpHeatingRecipe> info) {
		super(info);
	}

	@Override
	public void draw(BuiltUpHeatingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		int scale = 24;
		AllGuiTextures.JEI_SHADOW.render(graphics, 23, 55);
		AllGuiTextures.JEI_SHADOW.render(graphics, 99, 55);
		AllGuiTextures.JEI_LIGHT.render(graphics, 118, 65);
		CBCGuiTextures.CANNON_BUILDING_ARROW.render(graphics, 83, 34);

		PoseStack stack = graphics.pose();
		stack.pushPose();
		stack.translate(33, 59, 0);
		stack.mulPose(Axis.XP.rotationDegrees(-12.5f));
		stack.mulPose(Axis.YP.rotationDegrees(22.5f));
		AnimatedKinetics.defaultBlockElement(CBCBlocks.CANNON_BUILDER.getDefaultState().setValue(CannonBuilderBlock.STATE, BuilderState.ACTIVATED))
			.rotateBlock(0, 180, 0)
			.atLocal(0, 0, 0)
			.scale(scale)
			.render(graphics);

		AnimatedKinetics.defaultBlockElement(AllBlocks.SHAFT.getDefaultState())
			.rotateBlock(0, AnimatedKinetics.getCurrentAngle(), -90)
			.atLocal(0, 0, 0)
			.scale(scale)
			.render(graphics);
		stack.popPose();

		stack.pushPose();
		stack.translate(109, 59, 0);
		stack.mulPose(Axis.XP.rotationDegrees(-12.5f));
		stack.mulPose(Axis.YP.rotationDegrees(22.5f));

		AnimatedKinetics.defaultBlockElement(AllPartialModels.ENCASED_FAN_INNER)
			.rotateBlock(180, 0, AnimatedKinetics.getCurrentAngle() * 16)
			.scale(scale)
			.render(graphics);

		AnimatedKinetics.defaultBlockElement(AllBlocks.ENCASED_FAN.getDefaultState())
			.rotateBlock(0, 180, 0)
			.atLocal(0, 0, 0)
			.scale(scale)
			.render(graphics);
		stack.popPose();

		stack.pushPose();
		stack.translate(109, 59, 0);
		stack.mulPose(Axis.XP.rotationDegrees(-12.5f));
		stack.mulPose(Axis.YP.rotationDegrees(22.5f));

		GuiGameElement.of(Fluids.LAVA)
			.rotateBlock(0, 180, 0)
			.atLocal(0, 0, 2)
			.scale(scale)
			.render(graphics);
		stack.popPose();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BuiltUpHeatingRecipe recipe, IFocusGroup focuses) {
		int i = 0;
		Set<BlockRecipeIngredient> layers = recipe.layers();
		int base = this.getBackground().getWidth() / 2 - 11 * layers.size() + 1;
		for (BlockRecipeIngredient layer : layers) {
			IRecipeSlotBuilder builder1 = builder.addSlot(RecipeIngredientRole.INPUT, base + i * 22, 5);
			builder1.addItemStacks(layer.getBlockItems());
			builder1.setBackground(getRenderedSlot(), -1, -1);
			++i;
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, this.getBackground().getWidth() / 2 - 9, 80)
			.addItemStack(new ItemStack(recipe.getResultBlock()))
			.setBackground(getRenderedSlot(), -1, -1);
	}

}
