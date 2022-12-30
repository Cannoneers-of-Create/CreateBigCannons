package rbasamoyai.createbigcannons.munitions.fuzes;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.container.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCGuiTextures;
import rbasamoyai.createbigcannons.network.CBCNetwork;
import rbasamoyai.createbigcannons.network.ServerboundProximityFuzePacket;

public class ProximityFuzeScreen extends AbstractSimiContainerScreen<ProximityFuzeContainer> {
	
	private ScrollInput setDistance;
	private IconButton confirmButton;
	private int lastUpdated = -1;	
	
	public ProximityFuzeScreen(ProximityFuzeContainer menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
	}
	
	@Override
	protected void init() {
		this.setWindowSize(179, 83);
		this.setWindowOffset(1, 0);
		super.init();
		
		this.setDistance = new ScrollInput(this.leftPos + 36, this.topPos + 29, 102, 18)
				.withRange(1, 33)
				.calling(state -> {
					this.lastUpdated = 0;
					this.setDistance.titled(Lang.builder(CreateBigCannons.MOD_ID).translate("gui.set_proximity_fuze.distance", state).component());
				})
				.setState(Mth.clamp(this.menu.getDistance(), 1, 33));
		
		this.setDistance.onChanged();
		this.addRenderableWidget(this.setDistance);
		
		this.confirmButton = new IconButton(this.leftPos + this.imageWidth - 33, this.topPos + this.imageHeight - 24, AllIcons.I_CONFIRM);
		this.confirmButton.withCallback(this::onClose);
		this.addRenderableWidget(this.confirmButton);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		CBCGuiTextures.PROXIMITY_FUZE_BG.render(poseStack, this.leftPos, this.topPos);
		drawCenteredString(poseStack, this.font, this.title, this.leftPos + this.imageWidth / 2 - 4, this.topPos + 3, 0xffffff);
		int top = 32;
		double s = 100.0d / (double)(top - 1);
		int offsX = this.setDistance.getState();
		offsX = offsX == top ? 100 : (int) Math.ceil((double) offsX * s - s);
		CBCGuiTextures.PROXIMITY_FUZE_SELECTOR.render(poseStack, this.leftPos + 32 + offsX, this.topPos + 21);
		
		GuiGameElement.of(this.menu.getStackToRender())
			.<GuiGameElement.GuiItemRenderBuilder>at(this.leftPos + 185, this.topPos + 26, -200)
			.scale(5)
			.render(poseStack);
	}
	
	@Override
	protected void containerTick() {
		super.containerTick();
		
		if (this.lastUpdated >= 0) {
			this.lastUpdated++;
		}	
		if (this.lastUpdated >= 20) {
			this.updateServer();
			this.lastUpdated = -1;
		}
	}
	
	@Override
	public void removed() {
		super.removed();
		this.updateServer();
	}
	
	@Override
	public void onClose() {
		this.updateServer();
		super.onClose();
	}
	
	private void updateServer() {
		CBCNetwork.INSTANCE.sendToServer(new ServerboundProximityFuzePacket(this.setDistance.getState()));
	}

}
