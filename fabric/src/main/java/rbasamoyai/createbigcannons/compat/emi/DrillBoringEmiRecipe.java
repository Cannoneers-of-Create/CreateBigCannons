package rbasamoyai.createbigcannons.compat.emi;

import static com.simibubi.create.compat.emi.recipes.CreateEmiRecipe.addSlot;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.emi.CreateEmiAnimations;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class DrillBoringEmiRecipe extends CBCEmiBlockRecipe<DrillBoringBlockRecipe> {

	private final EmiIngredient inputIngredient;

	public DrillBoringEmiRecipe(EmiRecipeCategory category, DrillBoringBlockRecipe recipe, int width, int height) {
		super(category, recipe, width, height);
		this.inputIngredient = EmiIngredient.of(Ingredient.of(this.recipe.ingredients().stream()));
		this.inputs = List.of(this.inputIngredient);
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		addSlot(widgets, this.inputIngredient, 21, 51);
		addSlot(widgets, EmiStack.of(this.recipe.getResultBlock()), 141, 51).recipeContext(this);

		widgets.addDrawable(0, 0, 0, 0, ((graphics, mouseX, mouseY, delta) -> {
			int scale = 23;
			AllGuiTextures.JEI_SHADOW.render(graphics, 35, 28);
			AllGuiTextures.JEI_SHADOW.render(graphics, 97, 36);
			AllGuiTextures.JEI_LONG_ARROW.render(graphics, 54, 54);
			PoseStack poseStack = graphics.pose();
			poseStack.pushPose();
			poseStack.translate(45, 35, 10);
			poseStack.mulPose(Axis.XP.rotationDegrees(-12.5f));
			poseStack.mulPose(Axis.YP.rotationDegrees(60.0f));
			CreateEmiAnimations.defaultBlockElement(CBCBlocks.CANNON_DRILL.getDefaultState().setValue(CannonDrillBlock.STATE, MechanicalPistonBlock.PistonState.RETRACTED))
				.rotateBlock(0, 180, 0)
				.atLocal(0, 0, 0)
				.scale(scale)
				.render(graphics);

			BlockState shaft = AllBlocks.SHAFT.getDefaultState();
			Direction.Axis axis = shaft.getValue(BlockStateProperties.AXIS);

			CreateEmiAnimations.defaultBlockElement(shaft)
				.rotateBlock(0, CreateEmiAnimations.getCurrentAngle(), -90)
				.atLocal(0, 0, 0)
				.scale(scale)
				.render(graphics);

			List<ItemStack> ingredients = this.recipe.ingredients();
			Block block = !ingredients.isEmpty() && ingredients.get(0).getItem() instanceof BlockItem item ? item.getBlock() : Blocks.BARRIER;

			BlockState state = block.defaultBlockState();
			if (state.hasProperty(BlockStateProperties.FACING)) {
				state = state.setValue(BlockStateProperties.FACING, Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE));
			}
			if (state.hasProperty(BlockStateProperties.AXIS)) {
				state = state.setValue(BlockStateProperties.AXIS, axis);
			}

			CreateEmiAnimations.defaultBlockElement(state)
				.rotateBlock(90, 0, CreateEmiAnimations.getCurrentAngle())
				.atLocal(0, 0, 2)
				.scale(scale)
				.render(graphics);

			CreateEmiAnimations.defaultBlockElement(AllBlocks.MECHANICAL_BEARING.getDefaultState())
				.rotateBlock(0, 0, 0)
				.atLocal(0, 0, 3)
				.scale(scale)
				.render(graphics);

			CreateEmiAnimations.defaultBlockElement(AllPartialModels.BEARING_TOP)
				.rotateBlock(-90, 0, CreateEmiAnimations.getCurrentAngle())
				.atLocal(0, 0, 3)
				.scale(scale)
				.render(graphics);

			CreateEmiAnimations.defaultBlockElement(AllPartialModels.SHAFT_HALF)
				.rotateBlock(0, 0, CreateEmiAnimations.getCurrentAngle())
				.atLocal(0, 0, 3)
				.scale(scale)
				.render(graphics);
			poseStack.popPose();
		}));
	}
}
