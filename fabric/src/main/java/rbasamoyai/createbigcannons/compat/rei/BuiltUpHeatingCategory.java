package rbasamoyai.createbigcannons.compat.rei;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.rei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;

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
		CBCGuiTextures.CANNON_BUILDING_ARROW.render(stack, 83, 34);

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

}
