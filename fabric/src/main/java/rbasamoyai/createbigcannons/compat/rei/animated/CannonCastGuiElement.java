package rbasamoyai.createbigcannons.compat.rei.animated;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import net.minecraft.client.gui.components.Widget;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CannonCastGuiElement implements Widget {

	private CannonCastShape currentShape = CannonCastShape.VERY_SMALL;

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		poseStack.pushPose();
		poseStack.translate(0, 0, 200);
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
