package rbasamoyai.createbigcannons.compat.rei;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.rei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;

import java.util.List;
import java.util.Set;

import static com.simibubi.create.compat.rei.category.CreateRecipeCategory.basicSlot;

public class BuiltUpHeatingCategory extends CBCBlockRecipeCategory<BuiltUpHeatingRecipe> {

	public BuiltUpHeatingCategory(Info<BuiltUpHeatingRecipe> info) {
		super(info);
	}

	@Override
	public void draw(BuiltUpHeatingRecipe recipe, PoseStack stack, double mouseX, double mouseY) {
		super.draw(recipe, stack, mouseX, mouseY);
		int scale = 24;
		AllGuiTextures.JEI_SHADOW.render(stack, 23, 55);
		AllGuiTextures.JEI_SHADOW.render(stack, 99, 55);
		AllGuiTextures.JEI_LIGHT.render(stack, 118, 65);
		CBCGuiTextures.CANNON_BUILDING_ARROW.render(stack, 82, 34);

		stack.pushPose();
		stack.translate(33, 59, 0);
		stack.mulPose(Vector3f.XP.rotationDegrees(-12.5f));
		stack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
		AnimatedKinetics.defaultBlockElement(CBCBlocks.CANNON_BUILDER.getDefaultState().setValue(CannonBuilderBlock.STATE, CannonBuilderBlock.BuilderState.ACTIVATED))
			.rotateBlock(0, 180, 0)
			.atLocal(0, 0, 0)
			.scale(scale)
			.render(stack);

		AnimatedKinetics.defaultBlockElement(AllBlocks.SHAFT.getDefaultState())
			.rotateBlock(0, AnimatedKinetics.getCurrentAngle(), -90)
			.atLocal(0, 0, 0)
			.scale(scale)
			.render(stack);
		stack.popPose();

		stack.pushPose();
		stack.translate(109, 59, 0);
		stack.mulPose(Vector3f.XP.rotationDegrees(-12.5f));
		stack.mulPose(Vector3f.YP.rotationDegrees(22.5f));

		AnimatedKinetics.defaultBlockElement(AllPartialModels.ENCASED_FAN_INNER)
			.rotateBlock(180, 0, AnimatedKinetics.getCurrentAngle() * 16)
			.scale(scale)
			.render(stack);

		AnimatedKinetics.defaultBlockElement(AllBlocks.ENCASED_FAN.getDefaultState())
			.rotateBlock(0, 180, 0)
			.atLocal(0, 0, 0)
			.scale(scale)
			.render(stack);
		stack.popPose();

		stack.pushPose();
		stack.translate(109, 59, 0);
		stack.mulPose(Vector3f.XP.rotationDegrees(-12.5f));
		stack.mulPose(Vector3f.YP.rotationDegrees(22.5f));

		GuiGameElement.of(Fluids.LAVA)
			.rotateBlock(0, 180, 0)
			.atLocal(0, 0, 2)
			.scale(scale)
			.render(stack);
		stack.popPose();
	}

	@Override
	public void addWidgets(CBCDisplay<BuiltUpHeatingRecipe> display, List<Widget> ingredients, Point origin) {
		BuiltUpHeatingRecipe recipe = display.getRecipe();
		Set<BlockRecipeIngredient> layers = recipe.layers();

		int i = 0;
		int width = this.getDisplayWidth(null);
		int base = width / 2 - 11 * layers.size() + 1;

		for (BlockRecipeIngredient layer : layers) {
			ingredients.add(basicSlot(base + i * 22, 5, origin).markInput().entries(EntryIngredients.ofItemStacks(layer.getBlockItems())).backgroundEnabled(true));
			++i;
		}
		ingredients.add(basicSlot(width / 2 - 10, 74, origin).markOutput().entry(EntryStacks.of(new ItemStack(recipe.getResultBlock()))).backgroundEnabled(true));
	}

}
