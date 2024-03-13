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

public class DelayedImpactFuzeScreen extends AbstractFuzeScreen<DelayedImpactFuzeContainer> {

	public DelayedImpactFuzeScreen(DelayedImpactFuzeContainer menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
	}

	@Override
	protected ScrollInput getScrollInput() {
		return new ScrollInput(this.leftPos + 36, this.topPos + 29, 102, 18)
				.withRange(0, 100)
				.calling(state -> {
					this.lastUpdated = 0;
					int time = state + 1;
					int seconds = time / 20;
					int ticks = time - seconds * 20;
					this.setValue.titled(Lang.builder(CreateBigCannons.MOD_ID).translate("gui.set_timed_fuze.time", seconds, ticks).component());
				})
				.setState(Mth.clamp(this.menu.getValue() - 1, 0, 100));
	}

	@Override public int getUpdateState() { return this.setValue.getState() + 1; }

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

		CBCGuiTextures.TIMED_FUZE_BG.render(poseStack, this.leftPos, this.topPos);
		drawCenteredString(poseStack, this.font, this.title, this.leftPos + this.imageWidth / 2 - 4, this.topPos + 3, 0xffffff);
		CBCGuiTextures.TIMED_FUZE_SELECTOR.render(poseStack, this.leftPos + 34 + this.setValue.getState(), this.topPos + 21);

		GuiGameElement.of(this.menu.getStackToRender())
			.<GuiGameElement.GuiItemRenderBuilder>at(this.leftPos + 185, this.topPos + 26, -200)
			.scale(5)
			.render(poseStack);
	}

}
