package rbasamoyai.createbigcannons.equipment.gas_mask;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class GasMaskOverlay {

	public static final ResourceLocation GAS_MASK_OVERLAY = CreateBigCannons.resource("textures/misc/gas_mask_overlay.png");

	public static void renderOverlay(PoseStack poseStack, float partialTicks, int windowWidth, int windowHeight) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null || minecraft.options.getCameraType() != CameraType.FIRST_PERSON
			|| !GasMaskItem.canShowGasMaskOverlay(minecraft.player))
			return;
		// FIX FOR JUST ENOUGH GUNS OR ANY ODD INVOCATIONS OF RenderSystem#enableBlend/RenderSystem#disableBlend...
		boolean previousBlendState = GL11.glGetBoolean(GL11.GL_BLEND);
		RenderSystem.enableBlend();

		// Adapted from Gui#renderTextureOverlay
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, GAS_MASK_OVERLAY);
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.getBuilder();
		double offset = -90;
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(0.0, windowHeight, offset).uv(0.0F, 1.0F).endVertex();
		bufferBuilder.vertex(windowWidth, windowHeight, offset).uv(1.0F, 1.0F).endVertex();
		bufferBuilder.vertex(windowWidth, 0.0, offset).uv(1.0F, 0.0F).endVertex();
		bufferBuilder.vertex(0.0, 0.0, offset).uv(0.0F, 0.0F).endVertex();
		tesselator.end();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		if (!previousBlendState)
			RenderSystem.disableBlend();
	}

}
