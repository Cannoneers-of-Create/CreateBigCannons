package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;
import static rbasamoyai.createbigcannons.index.CBCGuiTextures.AUTOCANNON_AMMO_CONTAINER_BG;
import static rbasamoyai.createbigcannons.index.CBCGuiTextures.AUTOCANNON_AMMO_CONTAINER_SELECTOR;
import static rbasamoyai.createbigcannons.index.CBCGuiTextures.CREATIVE_AUTOCANNON_AMMO_CONTAINER_BG;
import static rbasamoyai.createbigcannons.index.CBCGuiTextures.CREATIVE_AUTOCANNON_AMMO_CONTAINER_SELECTOR;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ServerboundSetContainerValuePacket;

public class AutocannonAmmoContainerScreen extends AbstractSimiContainerScreen<AutocannonAmmoContainerMenu> {

	protected ScrollInput setValue;
	protected int lastUpdated = -1;
	protected IconButton confirmButton;
	private List<Rect2i> extraAreas = Collections.emptyList();

	public AutocannonAmmoContainerScreen(AutocannonAmmoContainerMenu container, Inventory inv, Component title) {
		super(container, inv, title);
	}

	@Override
	protected void init() {
		boolean isCreative = this.menu.isCreativeContainer();
		CBCGuiTextures bg = isCreative ? CREATIVE_AUTOCANNON_AMMO_CONTAINER_BG : AUTOCANNON_AMMO_CONTAINER_BG;
		this.setWindowSize(bg.width, bg.height + 4 + PLAYER_INVENTORY.height);
		this.setWindowOffset(1, 0);
		super.init();

		this.setValue = this.getScrollInput();

		this.setValue.onChanged();
		this.addRenderableWidget(this.setValue);

		this.confirmButton = new IconButton(this.leftPos + this.imageWidth - 33, this.topPos + 59, AllIcons.I_CONFIRM);
		this.confirmButton.withCallback(this::onClose);
		this.addRenderableWidget(this.confirmButton);

		this.extraAreas = ImmutableList.of(new Rect2i(this.leftPos + bg.width, this.topPos + bg.height - 68, 60, 60));
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
		int invX = this.getLeftOfCentered(PLAYER_INVENTORY.width);
		int invY = this.topPos + AUTOCANNON_AMMO_CONTAINER_BG.height + 4;
		this.renderPlayerInventory(poseStack, invX, invY);
		boolean isCreative = this.menu.isCreativeContainer();
		int offsX = this.setValue.getState() * 8 - 8;

		CBCGuiTextures bg = isCreative ? CREATIVE_AUTOCANNON_AMMO_CONTAINER_BG : AUTOCANNON_AMMO_CONTAINER_BG;
		bg.render(poseStack, this.leftPos, this.topPos);
		if (isCreative) {
			this.font.draw(poseStack, this.title, this.leftPos + 4, this.topPos + 3, 0x54214f);
		} else {
			drawCenteredString(poseStack, this.font, this.title, this.leftPos + this.imageWidth / 2 - 4, this.topPos + 3, 0xffffff);
		}
		CBCGuiTextures sel = isCreative ? CREATIVE_AUTOCANNON_AMMO_CONTAINER_SELECTOR : AUTOCANNON_AMMO_CONTAINER_SELECTOR;
		sel.render(poseStack, this.leftPos + 86 + offsX, this.topPos + 23);

		BlockState state = isCreative ? CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER.getDefaultState()
			: CBCBlocks.AUTOCANNON_AMMO_CONTAINER.getDefaultState();
		state = state.setValue(AutocannonAmmoContainerBlock.CONTAINER_STATE, AutocannonAmmoContainerBlock.State.getFromFilled(this.menu.isFilled()));
		GuiGameElement.of(state)
			.scale(50)
			.rotate(30, 135, 0)
			.at(this.leftPos + bg.width + 32, this.topPos + bg.height, 200)
			.render(poseStack);
	}

	@Override
	protected void renderTooltip(PoseStack poseStack, int x, int y) {
		super.renderTooltip(poseStack, x, y);
		if (this.hoveredSlot != null && this.hoveredSlot.index == 1 && !this.hoveredSlot.hasItem()) {
			this.renderTooltip(poseStack, Lang.builder(CreateBigCannons.MOD_ID).translate("gui.autocannon_ammo_container.tracer_slot").component(), x ,y);
		}
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
		NetworkPlatform.sendToServer(new ServerboundSetContainerValuePacket(this.setValue.getState()));
	}

	protected ScrollInput getScrollInput() {
		return new ScrollInput(this.leftPos + 87, this.topPos + 31, 47, 6)
			.withRange(1, 7)
			.calling(state -> {
				this.lastUpdated = 0;
				this.setValue.titled(Lang.builder(CreateBigCannons.MOD_ID).translate("gui.autocannon_ammo_container.tracer_spacing", state).component());
			})
			.setState(Mth.clamp(this.menu.getValue(), 1, 6));
	}

	@Override public List<Rect2i> getExtraAreas() { return this.extraAreas; }

}
