package rbasamoyai.createbigcannons.equipment.gas_mask;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class GasMaskOverlay {

	public static final ResourceLocation GAS_MASK_OVERLAY = CreateBigCannons.resource("textures/misc/gas_mask_overlay.png");

	public static void renderOverlay(GuiGraphics graphics, float partialTicks, int windowWidth, int windowHeight) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null || minecraft.options.getCameraType() != CameraType.FIRST_PERSON
			|| !GasMaskItem.canShowGasMaskOverlay(minecraft.player))
			return;
		// Adapted from Gui#renderTextureOverlay
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		graphics.blit(GAS_MASK_OVERLAY, 0, 0, -90, 0.0F, 0.0F, windowWidth, windowHeight, windowWidth, windowHeight);
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

}
