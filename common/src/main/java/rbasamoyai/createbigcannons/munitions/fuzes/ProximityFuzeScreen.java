package rbasamoyai.createbigcannons.munitions.fuzes;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ServerBoundSetProximityAnglePacket;

public class ProximityFuzeScreen extends AbstractFuzeScreen<ProximityFuzeContainer> {

	protected ScrollInput AngleScroll;

	public ProximityFuzeScreen(ProximityFuzeContainer menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
	}

	@Override
	protected void init() {
		super.init();
		AngleScroll = new ScrollInput(this.leftPos + 36, this.topPos + 39, 102, 12)
			.withRange(0, 91)
			.calling(state -> {
				this.lastUpdated = 0;
				AngleScroll.titled(Lang.builder(CreateBigCannons.MOD_ID).translate("gui.set_proximity_fuze.degrees", state).component());
			})
			.setState(Mth.clamp(this.menu.data.get(1), 0, 91));
		AngleScroll.onChanged();
		this.addRenderableWidget(AngleScroll);
	}

	@Override
	protected void containerTick() {
		super.containerTick();

		if (this.lastUpdated >= 0) {
			this.lastUpdated++;
		}
		if (this.lastUpdated >= 20) {
			this.updateServerAngle();
			this.lastUpdated = -1;
		}
	}

	@Override
	public void removed() {
		super.removed();
		this.updateServerAngle();
	}

	@Override
	public void onClose() {
		this.updateServerAngle();
		super.onClose();
	}

	private void updateServerAngle() {
		NetworkPlatform.sendToServer(new ServerBoundSetProximityAnglePacket(this.AngleScroll.getState()));
	}

	@Override
	protected ScrollInput getScrollInput() {
		return new ScrollInput(this.leftPos + 36, this.topPos + 21, 102, 12)
				.withRange(1, 33)
				.calling(state -> {
					this.lastUpdated = 0;
					this.setValue.titled(Lang.builder(CreateBigCannons.MOD_ID).translate("gui.set_proximity_fuze.distance", state).component());
				})
				.setState(Mth.clamp(this.menu.getValue(), 1, 33));
	}

	@Override public int getUpdateState() { return this.setValue.getState(); }

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

		CBCGuiTextures.PROXIMITY_FUZE_BG.render(graphics, this.leftPos, this.topPos);
		graphics.drawCenteredString(this.font, this.title, this.leftPos + this.imageWidth / 2 - 4, this.topPos + 3, 0xffffff);
		int top = 32;
		double s = 100.0d / (double)(top - 1);
		int offsX = this.setValue.getState();
		offsX = offsX == top ? 100 : (int) Math.ceil((double) offsX * s - s);
		CBCGuiTextures.PROXIMITY_FUZE_SELECTOR.render(graphics, this.leftPos + 32 + offsX, this.topPos + 13);

		int top2 = 90;
		double s2 = 100.0d / (double)(top2);
		int offsX2 = AngleScroll.getState();
		offsX2 = offsX2 == top2? 100 : (int) Math.ceil((double) offsX2 * s2 - s2);
		CBCGuiTextures.PROXIMITY_FUZE_SELECTOR.render(graphics, this.leftPos + 32 + offsX2, this.topPos + 31);

		GuiGameElement.of(this.menu.getStackToRender())
			.<GuiGameElement.GuiItemRenderBuilder>at(this.leftPos + 185, this.topPos + 26, -200)
			.scale(5)
			.render(graphics);
	}

}
