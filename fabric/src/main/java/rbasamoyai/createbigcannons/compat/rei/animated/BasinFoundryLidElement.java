package rbasamoyai.createbigcannons.compat.rei.animated;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class BasinFoundryLidElement extends Widget {

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		poseStack.pushPose();
		poseStack.translate(0, 0, 200);
		poseStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
		poseStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
		int scale = 23;

		GuiGameElement.of(CBCBlocks.BASIN_FOUNDRY_LID.getDefaultState())
			.atLocal(0, 0.65, 0)
			.scale(scale)
			.render(poseStack);

		GuiGameElement.of(AllBlocks.BASIN.getDefaultState())
			.atLocal(0, 1.65, 0)
			.scale(scale)
			.render(poseStack);

		poseStack.popPose();
	}

	@Override public List<? extends GuiEventListener> children() { return new ArrayList<>(); }

}
