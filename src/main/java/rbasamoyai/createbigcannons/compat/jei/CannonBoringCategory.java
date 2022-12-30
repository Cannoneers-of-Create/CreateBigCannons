package rbasamoyai.createbigcannons.compat.jei;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock.PistonState;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;

public class CannonBoringCategory extends CBCBlockRecipeCategory<CannonBoringRecipe> {

	public CannonBoringCategory(Info<CannonBoringRecipe> info) {
		super(info);
	}
	
	@Override
	public void draw(CannonBoringRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		int scale = 23;
		AllGuiTextures.JEI_SHADOW.render(stack, 35, 28);
		AllGuiTextures.JEI_SHADOW.render(stack, 97, 36);
		AllGuiTextures.JEI_LONG_ARROW.render(stack, 54, 54);
		stack.pushPose();
		stack.translate(45, 35, 10);
		stack.mulPose(Vector3f.XP.rotationDegrees(-12.5f));
		stack.mulPose(Vector3f.YP.rotationDegrees(60.0f));
		AnimatedKinetics.defaultBlockElement(CBCBlocks.CANNON_DRILL.getDefaultState().setValue(CannonDrillBlock.STATE, PistonState.RETRACTED))
		.rotateBlock(0, 180, 0)
		.atLocal(0, 0, 0)
		.scale(scale)
		.render(stack);
		
		AnimatedKinetics.defaultBlockElement(AllBlocks.SHAFT.getDefaultState())
		.rotateBlock(0, AnimatedKinetics.getCurrentAngle(), -90)
		.atLocal(0, 0, 0)
		.scale(scale)
		.render(stack);
		
		AnimatedKinetics.defaultBlockElement(recipe.ingredientBlock().defaultBlockState())
		.rotateBlock(0, 0, AnimatedKinetics.getCurrentAngle())
		.atLocal(0, 0, 2)
		.scale(scale)
		.render(stack);
		
		AnimatedKinetics.defaultBlockElement(AllBlocks.MECHANICAL_BEARING.getDefaultState())
		.rotateBlock(0, 0, 0)
		.atLocal(0, 0, 3)
		.scale(scale)
		.render(stack);
		
		AnimatedKinetics.defaultBlockElement(AllBlockPartials.BEARING_TOP)
		.rotateBlock(-90, 0, AnimatedKinetics.getCurrentAngle())
		.atLocal(0, 0, 3)
		.scale(scale)
		.render(stack);
		
		AnimatedKinetics.defaultBlockElement(AllBlockPartials.SHAFT_HALF)
		.rotateBlock(0, 0, AnimatedKinetics.getCurrentAngle())
		.atLocal(0, 0, 3)
		.scale(scale)
		.render(stack);
		stack.popPose();
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CannonBoringRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 21, 51)
		.addItemStack(recipe.ingredients().get(0))
		.setBackground(getRenderedSlot(), -1, -1);
		
		builder.addSlot(RecipeIngredientRole.OUTPUT, 141, 51)
		.addItemStack(new ItemStack(recipe.getResultBlock()))
		.setBackground(getRenderedSlot(), -1, -1);
	}

}
