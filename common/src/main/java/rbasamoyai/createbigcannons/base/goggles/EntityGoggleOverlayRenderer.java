package rbasamoyai.createbigcannons.base.goggles;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.gui.RemovedGuiUtils;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CClient;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

// Heavily copied from GoggleOverlayRenderer --ritchie
public class EntityGoggleOverlayRenderer {

	public static int hoverTicks = 0;

	public static void renderOverlay(PoseStack poseStack, float partialTicks, int windowWidth, int windowHeight) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR) return;

		HitResult objectMouseOver = mc.hitResult;
		if (!(objectMouseOver instanceof EntityHitResult result)) {
			hoverTicks = 0;
			return;
		}

		hoverTicks++;

		Entity entity = result.getEntity();
		boolean wearingGoggles = GogglesItem.isWearingGoggles(mc.player);
		boolean shiftKey = mc.player.isShiftKeyDown();

		boolean hasGoggleInformation = entity instanceof IHaveEntityGoggleInformation;
		boolean hasHoveringInformation = entity instanceof IHaveEntityHoverInformation;

		boolean goggleAddedInformation = false;
		boolean hoverAddedInformation = false;

		List<Component> tooltip = new ArrayList<>();

		if (hasGoggleInformation && wearingGoggles) {
			goggleAddedInformation = ((IHaveEntityGoggleInformation) entity).addToGoggleTooltip(tooltip, shiftKey);
		}
		if (hasHoveringInformation) {
			if (!tooltip.isEmpty()) tooltip.add(Components.immutableEmpty());
			hoverAddedInformation = ((IHaveEntityHoverInformation) entity).addToTooltip(tooltip, shiftKey);
			if (goggleAddedInformation && !hoverAddedInformation) tooltip.remove(tooltip.size() - 1);
		}

		if (entity instanceof IDisplayEntityAssemblyExceptions deae && deae.addExceptionToTooltip(tooltip)) {
			hasHoveringInformation = true;
			hoverAddedInformation = true;
		}

		// break early if goggle or hover returned false when present
		if (tooltip.isEmpty() || (hasGoggleInformation && !goggleAddedInformation) && (hasHoveringInformation && !hoverAddedInformation)) {
			hoverTicks = 0;
			return;
		}

		poseStack.pushPose();

		int tooltipTextWidth = 0;
		for (FormattedText textLine : tooltip) {
			int textLineWidth = mc.font.width(textLine);
			if (textLineWidth > tooltipTextWidth)
				tooltipTextWidth = textLineWidth;
		}

		int tooltipHeight = 8;
		if (tooltip.size() > 1) {
			tooltipHeight += 2; // gap between title lines and next lines
			tooltipHeight += (tooltip.size() - 1) * 10;
		}

		CClient cfg = AllConfigs.client();
		int posX = windowWidth / 2 + cfg.overlayOffsetX.get();
		int posY = windowHeight / 2 + cfg.overlayOffsetY.get();

		posX = Math.min(posX, windowWidth - tooltipTextWidth - 20);
		posY = Math.min(posY, windowHeight - tooltipHeight - 20);

		float fade = Mth.clamp((hoverTicks + partialTicks) / 24f, 0, 1);
		Boolean useCustom = cfg.overlayCustomColor.get();
		Color colorBackground = useCustom ? new Color(cfg.overlayBackgroundColor.get())
			: Theme.c(Theme.Key.VANILLA_TOOLTIP_BACKGROUND)
			.scaleAlpha(.75f);
		Color colorBorderTop = useCustom ? new Color(cfg.overlayBorderColorTop.get())
			: Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, true)
			.copy();
		Color colorBorderBot = useCustom ? new Color(cfg.overlayBorderColorBot.get())
			: Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, false)
			.copy();

		if (fade < 1) {
			poseStack.translate(Math.pow(1 - fade, 3) * Math.signum(cfg.overlayOffsetX.get() + .5f) * 8, 0, 0);
			colorBackground.scaleAlpha(fade);
			colorBorderTop.scaleAlpha(fade);
			colorBorderBot.scaleAlpha(fade);
		}

		RemovedGuiUtils.drawHoveringText(poseStack, tooltip, posX, posY, windowWidth, windowHeight, -1, colorBackground.getRGB(),
			colorBorderTop.getRGB(), colorBorderBot.getRGB(), mc.font);

		ItemStack item = AllItems.GOGGLES.asStack();
		GuiGameElement.of(item)
			.at(posX + 10, posY - 16, 450)
			.render(poseStack);
		poseStack.popPose();
	}

}
