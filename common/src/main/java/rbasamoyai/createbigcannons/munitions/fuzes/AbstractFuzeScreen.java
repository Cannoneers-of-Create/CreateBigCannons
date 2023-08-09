package rbasamoyai.createbigcannons.munitions.fuzes;

import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.ScrollInput;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ServerboundSetContainerValuePacket;

public abstract class AbstractFuzeScreen<T extends AbstractFuzeContainer> extends AbstractSimiContainerScreen<T> {

	protected ScrollInput setValue;
	protected int lastUpdated = -1;
	protected IconButton confirmButton;

	protected AbstractFuzeScreen(T container, Inventory inv, Component title) {
		super(container, inv, title);
	}

	protected abstract ScrollInput getScrollInput();


	@Override
	protected void init() {
		this.setWindowSize(179, 83);
		this.setWindowOffset(1, 0);
		super.init();

		this.setValue = this.getScrollInput();

		this.setValue.onChanged();
		this.addRenderableWidget(this.setValue);

		this.confirmButton = new IconButton(this.leftPos + this.imageWidth - 33, this.topPos + this.imageHeight - 24, AllIcons.I_CONFIRM);
		this.confirmButton.withCallback(this::onClose);
		this.addRenderableWidget(this.confirmButton);
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
		NetworkPlatform.sendToServer(new ServerboundSetContainerValuePacket(this.getUpdateState()));
	}

	public abstract int getUpdateState();

}
