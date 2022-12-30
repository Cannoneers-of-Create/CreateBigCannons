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
import rbasamoyai.createbigcannons.network.ServerboundTimedFuzePacket;

public class TimedFuzeScreen extends AbstractSimiContainerScreen<TimedFuzeContainer> {
	
	private ScrollInput setTime;
	private IconButton confirmButton;
	private int lastUpdated = -1;	
	
	public TimedFuzeScreen(TimedFuzeContainer menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
	}
	
	@Override
	protected void init() {
		this.setWindowSize(179, 83);
		this.setWindowOffset(1, 0);
		super.init();
		
		this.setTime = new ScrollInput(this.leftPos + 36, this.topPos + 29, 102, 18)
				.withRange(0, 100)
				.calling(state -> {
					this.lastUpdated = 0;
					int time = 20 + state * 5;
					int seconds = time / 20;
					int ticks = time - seconds * 20;
					this.setTime.titled(Lang.builder(CreateBigCannons.MOD_ID).translate("gui.set_timed_fuze.time", seconds, ticks).component());
				})
				.setState(Mth.clamp(this.menu.getTime() / 5 - 4, 0, 100));
		
		this.setTime.onChanged();
		this.addRenderableWidget(this.setTime);
		
		this.confirmButton = new IconButton(this.leftPos + this.imageWidth - 33, this.topPos + this.imageHeight - 24, AllIcons.I_CONFIRM);
		this.confirmButton.withCallback(this::onClose);
		this.addRenderableWidget(this.confirmButton);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		CBCGuiTextures.TIMED_FUZE_BG.render(poseStack, this.leftPos, this.topPos);
		drawCenteredString(poseStack, this.font, this.title, this.leftPos + this.imageWidth / 2 - 4, this.topPos + 3, 0xffffff);
		CBCGuiTextures.TIMED_FUZE_SELECTOR.render(poseStack, this.leftPos + 34 + this.setTime.getState(), this.topPos + 21);
		
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
		CBCNetwork.INSTANCE.sendToServer(new ServerboundTimedFuzePacket(20 + this.setTime.getState() * 5));
	}

}
