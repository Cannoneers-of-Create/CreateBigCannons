package rbasamoyai.createbigcannons.compat.jei.animated;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CannonCastGuiElement implements IDrawable {

	private CannonCastShape currentShape = CannonCastShape.VERY_SMALL;

	@Override public int getWidth() { return 50; }
	@Override public int getHeight() { return 50; }

	@Override
	public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
		PoseStack poseStack = graphics.pose();
		poseStack.pushPose();
		poseStack.translate(xOffset, yOffset, 200);
		poseStack.mulPose(Axis.XP.rotationDegrees(-22.5f));
		poseStack.mulPose(Axis.YP.rotationDegrees(22.5f));
		int scale = 23;

		GuiGameElement.of(CBCBlockPartials.cannonCastFor(this.currentShape))
			.atLocal(0, 0.25, 0)
			.scale(scale)
			.render(graphics);

		poseStack.popPose();
	}

	public CannonCastGuiElement withShape(CannonCastShape shape) { this.currentShape = shape; return this; }

}
