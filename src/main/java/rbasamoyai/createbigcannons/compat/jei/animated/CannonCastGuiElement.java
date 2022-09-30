package rbasamoyai.createbigcannons.compat.jei.animated;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import mezz.jei.api.gui.drawable.IDrawable;
import rbasamoyai.createbigcannons.CBCBlockPartials;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CannonCastGuiElement implements IDrawable {

	private CannonCastShape currentShape = CannonCastShape.VERY_SMALL.get();
	
	@Override public int getWidth() { return 50; }
	@Override public int getHeight() { return 50; }

	@Override
	public void draw(PoseStack poseStack, int xOffset, int yOffset) {
		poseStack.pushPose();
		poseStack.translate(xOffset, yOffset, 200);
		poseStack.mulPose(Vector3f.XP.rotationDegrees(-22.5f));
		poseStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
		int scale = 23;
		
		GuiGameElement.of(CBCBlockPartials.cannonCastFor(this.currentShape))
			.atLocal(0, 0.25, 0)
			.scale(scale)
			.render(poseStack);
		
		poseStack.popPose();
	}
	
	public CannonCastGuiElement withShape(CannonCastShape shape) { this.currentShape = shape; return this; }

}
