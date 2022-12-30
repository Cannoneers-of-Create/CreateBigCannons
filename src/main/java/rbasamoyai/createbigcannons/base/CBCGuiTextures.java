package rbasamoyai.createbigcannons.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.ScreenElement;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.CreateBigCannons;

public enum CBCGuiTextures implements ScreenElement {
	// JEI Icons
	CASTING_ARROW("jei_icons", 0, 0, 32, 32),
	CASTING_ARROW_1("jei_icons", 32, 0, 32, 32),
	CANNON_CAST_SHADOW("jei_icons", 64, 0, 96, 32),
	CANNON_BUILDING_ARROW("jei_icons", 160, 0, 8, 32),
	
	// Fuze GUI
	// Timed Fuze
	TIMED_FUZE_BG("backgrounds1", 0, 0, 179, 83),
	TIMED_FUZE_SELECTOR("backgrounds1", 179, 0, 7, 26),
	// Proximity Fuze
	PROXIMITY_FUZE_BG("backgrounds1", 0, 91, 179, 83),
	PROXIMITY_FUZE_SELECTOR("backgrounds1", 179, 91, 9, 26),
	
	;
	
	private final ResourceLocation texture;
	private final int texX;
	private final int texY;
	private final int texW;
	private final int texH;
	
	private CBCGuiTextures(String path, int texX, int texY, int texW, int texH) {
		this(CreateBigCannons.MOD_ID, path, texX, texY, texW, texH);
	}
	
	private CBCGuiTextures(String namespace, String path, int texX, int texY, int texW, int texH) {
		this.texture = new ResourceLocation(namespace, "textures/gui/" + path + ".png");
		this.texX = texX;
		this.texY = texY;
		this.texW = texW;
		this.texH = texH;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void bind() {
		RenderSystem.setShaderTexture(0, this.texture);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void render(PoseStack ms, int x, int y) {
		this.bind();
		GuiComponent.blit(ms, x, y, 0, this.texX, this.texY, this.texW, this.texH, 256, 256);
	}
	
}
