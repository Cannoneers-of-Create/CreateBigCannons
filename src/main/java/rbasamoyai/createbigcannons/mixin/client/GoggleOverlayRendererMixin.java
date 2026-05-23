package rbasamoyai.createbigcannons.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.equipment.goggles.GoggleOverlayRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorInspectionToolItem;

@Mixin(GoggleOverlayRenderer.class)
public class GoggleOverlayRendererMixin { // TODO c6 playtest, possibly move to common?

	@WrapOperation(method = "renderOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0))
	private static BlockState createbigcannons$renderOverlay(ClientLevel instance, BlockPos blockPos, Operation<BlockState> original,
                                                             @Local List<Component> tooltip, @Local ClientLevel level, @Local BlockPos pos) {
        BlockState ret = original.call(instance, blockPos);
		Minecraft minecraft = Minecraft.getInstance();
		if (BlockArmorInspectionToolItem.isHoldingTool(minecraft.player))
            BlockArmorInspectionToolItem.addBlockArmorInfo(tooltip, level, pos, ret);
        return ret;
    }

}
