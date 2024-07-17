package rbasamoyai.createbigcannons.fabric.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.Window;
import com.simibubi.create.content.equipment.goggles.GoggleOverlayRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorInspectionToolItem;

@Mixin(GoggleOverlayRenderer.class)
public class GoggleOverlayRendererMixin {

	@Inject(method = "renderOverlay",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0))
	private static void createbigcannons$renderOverlay(GuiGraphics graphics, float partialTicks, Window window, CallbackInfo ci,
													   @Local List<Component> tooltip, @Local ClientLevel level, @Local BlockPos pos) {
		Minecraft minecraft = Minecraft.getInstance();
		if (!BlockArmorInspectionToolItem.isHoldingTool(minecraft.player))
			return;
		BlockArmorInspectionToolItem.addBlockArmorInfo(tooltip, level, pos, level.getBlockState(pos));
	}

}
