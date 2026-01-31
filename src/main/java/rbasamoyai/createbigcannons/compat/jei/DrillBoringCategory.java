package rbasamoyai.createbigcannons.compat.jei;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class DrillBoringCategory extends CBCBlockRecipeCategory<DrillBoringBlockRecipe> {

	public DrillBoringCategory(Info<DrillBoringBlockRecipe> info) {
		super(info);
	}

	@Override
	public void draw(DrillBoringBlockRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		int scale = 23;
		AllGuiTextures.JEI_SHADOW.render(graphics, 35, 28);
		AllGuiTextures.JEI_SHADOW.render(graphics, 97, 36);
		AllGuiTextures.JEI_LONG_ARROW.render(graphics, 54, 54);
		PoseStack stack = graphics.pose();
		stack.pushPose();
		stack.translate(45, 35, 10);
		stack.mulPose(Axis.XP.rotationDegrees(-12.5f));
		stack.mulPose(Axis.YP.rotationDegrees(60.0f));
		AnimatedKinetics.defaultBlockElement(CBCBlocks.CANNON_DRILL.getDefaultState().setValue(CannonDrillBlock.STATE, MechanicalPistonBlock.PistonState.RETRACTED))
			.rotateBlock(0, 180, 0)
			.atLocal(0, 0, 0)
			.scale(scale)
			.render(graphics);

		BlockState shaft = AllBlocks.SHAFT.getDefaultState();
		Direction.Axis axis = shaft.getValue(BlockStateProperties.AXIS);

		AnimatedKinetics.defaultBlockElement(shaft)
			.rotateBlock(0, AnimatedKinetics.getCurrentAngle(), -90)
			.atLocal(0, 0, 0)
			.scale(scale)
			.render(graphics);

		List<ItemStack> ingredients = recipe.ingredients();
		Block block = !ingredients.isEmpty() && ingredients.get(0).getItem() instanceof BlockItem item ? item.getBlock() : Blocks.BARRIER;

		BlockState state = block.defaultBlockState();
		if (state.hasProperty(BlockStateProperties.FACING)) {
			state = state.setValue(BlockStateProperties.FACING, Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE));
		}
		if (state.hasProperty(BlockStateProperties.AXIS)) {
			state = state.setValue(BlockStateProperties.AXIS, axis);
		}

		AnimatedKinetics.defaultBlockElement(state)
			.rotateBlock(90, 0, AnimatedKinetics.getCurrentAngle())
			.atLocal(0, 0, 2)
			.scale(scale)
			.render(graphics);

		AnimatedKinetics.defaultBlockElement(AllBlocks.MECHANICAL_BEARING.getDefaultState())
			.rotateBlock(0, 0, 0)
			.atLocal(0, 0, 3)
			.scale(scale)
			.render(graphics);

		AnimatedKinetics.defaultBlockElement(AllPartialModels.BEARING_TOP)
			.rotateBlock(-90, 0, AnimatedKinetics.getCurrentAngle())
			.atLocal(0, 0, 3)
			.scale(scale)
			.render(graphics);

		AnimatedKinetics.defaultBlockElement(AllPartialModels.SHAFT_HALF)
			.rotateBlock(0, 0, AnimatedKinetics.getCurrentAngle())
			.atLocal(0, 0, 3)
			.scale(scale)
			.render(graphics);
		stack.popPose();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DrillBoringBlockRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 21, 51)
			.addItemStacks(recipe.ingredients())
			.setBackground(getRenderedSlot(), -1, -1);

		builder.addSlot(RecipeIngredientRole.OUTPUT, 141, 51)
			.addItemStack(new ItemStack(recipe.getResultBlock()))
			.setBackground(getRenderedSlot(), -1, -1);
	}

}
