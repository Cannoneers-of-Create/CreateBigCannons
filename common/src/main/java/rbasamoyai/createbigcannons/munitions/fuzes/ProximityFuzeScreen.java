package rbasamoyai.createbigcannons.munitions.fuzes;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;

public class ProximityFuzeScreen extends AbstractFuzeScreen<ProximityFuzeContainer> {

	public ProximityFuzeScreen(ProximityFuzeContainer menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
	}

	@Override
	protected ScrollInput getScrollInput() {
		return new ScrollInput(this.leftPos + 36, this.topPos + 29, 102, 18)
				.withRange(1, 33)
				.calling(state -> {
					this.lastUpdated = 0;
					this.setValue.titled(Lang.builder(CreateBigCannons.MOD_ID).translate("gui.set_proximity_fuze.distance", state).component());
				})
				.setState(Mth.clamp(this.menu.getValue(), 1, 33));
	}

	@Override public int getUpdateState() { return this.setValue.getState(); }

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

		CBCGuiTextures.PROXIMITY_FUZE_BG.render(poseStack, this.leftPos, this.topPos);
		drawCenteredString(poseStack, this.font, this.title, this.leftPos + this.imageWidth / 2 - 4, this.topPos + 3, 0xffffff);
		int top = 32;
		double s = 100.0d / (double)(top - 1);
		int offsX = this.setValue.getState();
		offsX = offsX == top ? 100 : (int) Math.ceil((double) offsX * s - s);
		CBCGuiTextures.PROXIMITY_FUZE_SELECTOR.render(poseStack, this.leftPos + 32 + offsX, this.topPos + 21);

		GuiGameElement.of(this.menu.getStackToRender())
			.<GuiGameElement.GuiItemRenderBuilder>at(this.leftPos + 185, this.topPos + 26, -200)
			.scale(5)
			.render(poseStack);
	}

}
