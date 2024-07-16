package rbasamoyai.createbigcannons.index;

import com.simibubi.create.foundation.gui.element.ScreenElement;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public enum CBCGuiTextures implements ScreenElement {
	// JEI Icons
	CASTING_ARROW("jei_icons", 0, 0, 32, 32),
	CASTING_ARROW_1("jei_icons", 32, 0, 32, 32),
	CANNON_CAST_SHADOW("jei_icons", 64, 0, 96, 32),
	CANNON_BUILDING_ARROW("jei_icons", 160, 0, 8, 32),

	TIMED_FUZE_BG("backgrounds1", 0, 0, 179, 83),
	TIMED_FUZE_SELECTOR("backgrounds1", 179, 0, 7, 26),

	PROXIMITY_FUZE_BG("backgrounds1", 0, 84, 179, 83),
	PROXIMITY_FUZE_SELECTOR("backgrounds1", 179, 84, 9, 26),

	AUTOCANNON_AMMO_CONTAINER_BG("backgrounds1", 0, 168, 179, 83),
	AUTOCANNON_AMMO_CONTAINER_SELECTOR("backgrounds1", 179, 168, 9, 14),
	CREATIVE_AUTOCANNON_AMMO_CONTAINER_BG("backgrounds2", 0, 0, 199, 83),
	CREATIVE_AUTOCANNON_AMMO_CONTAINER_SELECTOR("backgrounds2", 199, 0, 9, 14)
	;

	private final ResourceLocation texture;
	public final int texX;
	public final int texY;
	public final int width;
	public final int height;

	CBCGuiTextures(String path, int texX, int texY, int width, int height) {
		this(CreateBigCannons.MOD_ID, path, texX, texY, width, height);
	}

	CBCGuiTextures(String namespace, String path, int texX, int texY, int width, int height) {
		this.texture = CBCUtils.location(namespace, "textures/gui/" + path + ".png");
		this.texX = texX;
		this.texY = texY;
		this.width = width;
		this.height = height;
	}

	@Override
	public void render(GuiGraphics graphics, int x, int y) {
		graphics.blit(this.texture, x, y, 0, this.texX, this.texY, this.width, this.height, 256, 256);
	}

}
