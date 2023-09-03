package rbasamoyai.createbigcannons.compat.jei.animated;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class BasinFoundryLidElement implements IDrawable {

	@Override
	public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
		PoseStack poseStack = graphics.pose();
		poseStack.pushPose();
		poseStack.translate(xOffset, yOffset, 200);
		poseStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
		poseStack.mulPose(Axis.YP.rotationDegrees(22.5f));
		int scale = 23;

		GuiGameElement.of(CBCBlocks.BASIN_FOUNDRY_LID.getDefaultState())
			.atLocal(0, 0.65, 0)
			.scale(scale)
			.render(graphics);

		GuiGameElement.of(AllBlocks.BASIN.getDefaultState())
			.atLocal(0, 1.65, 0)
			.scale(scale)
			.render(graphics);

		poseStack.popPose();
	}

	@Override
	public int getHeight() {
		return 50;
	}

	@Override
	public int getWidth() {
		return 50;
	}

}
