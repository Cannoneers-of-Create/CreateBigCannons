package rbasamoyai.createbigcannons.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rbasamoyai.createbigcannons.base.CBCTooltip;

import java.util.List;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(method = "appendHoverText", at = @At("HEAD"))
	private void CreateBigCannons$appendBlockHardnessTooltip(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced, CallbackInfo ci) {
		CBCTooltip.appendBlockHardnessText((BlockItem) stack.getItem(), tooltipComponents, isAdvanced);
	}
}
