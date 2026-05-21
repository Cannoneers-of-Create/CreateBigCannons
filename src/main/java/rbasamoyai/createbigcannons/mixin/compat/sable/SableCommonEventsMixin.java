package rbasamoyai.createbigcannons.mixin.compat.sable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.ryanhcode.sable.SableCommonEvents;
import dev.ryanhcode.sable.sublevel.plot.heat.SubLevelHeatMapManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlock;
import rbasamoyai.createbigcannons.cannons.CannonContraptionProviderBlock;
import rbasamoyai.createbigcannons.compat.sable.CBCSableConnectivityUtils;
import rbasamoyai.createbigcannons.index.CBCBlocks;

@Mixin(SableCommonEvents.class)
public class SableCommonEventsMixin {

    @WrapOperation(method = "handleBlockChange", at = @At(value = "INVOKE", target = "Ldev/ryanhcode/sable/sublevel/plot/heat/SubLevelHeatMapManager;onSolidAdded(Lnet/minecraft/core/BlockPos;)V"))
    private static void createbigcannon$handleBlockChange$onSolidAdded(SubLevelHeatMapManager instance, BlockPos blockPos, Operation<Void> original,
                                                                       @Local(argsOnly = true) ServerLevel level,
                                                                       @Local(ordinal = 1, argsOnly = true) BlockState newState) {
        original.call(instance, blockPos);
        if (newState.is(CBCBlocks.CANNON_MOUNT)) {
            BlockPos gapPos = blockPos.relative(newState.getValue(CannonMountBlock.VERTICAL_DIRECTION), -1);
            if (level.getBlockState(gapPos).isAir())
                original.call(instance, gapPos);
        } else if (newState.getBlock() instanceof CannonContraptionProviderBlock providerBlock) {
            for (BlockPos gapPos : CBCSableConnectivityUtils.getCannonMountGaps(blockPos, level, providerBlock.getFacing(newState))) {
                if (level.getBlockState(gapPos).isAir())
                    original.call(instance, gapPos);
            }
        }
    }

    @WrapOperation(method = "handleBlockChange", at = @At(value = "INVOKE", target = "Ldev/ryanhcode/sable/sublevel/plot/heat/SubLevelHeatMapManager;onSolidRemoved(Lnet/minecraft/core/BlockPos;)V"))
    private static void createbigcannons$handleBlockChange$onSolidRemoved(SubLevelHeatMapManager instance, BlockPos blockPos, Operation<Void> original,
                                                                          @Local(argsOnly = true) ServerLevel level,
                                                                          @Local(ordinal = 0, argsOnly = true) BlockState oldState) {
        if (CBCSableConnectivityUtils.isCannonMountAirGap(blockPos, level))
            return;
        original.call(instance, blockPos);
        if (oldState.is(CBCBlocks.CANNON_MOUNT)) {
            BlockPos gapPos = blockPos.relative(oldState.getValue(CannonMountBlock.VERTICAL_DIRECTION), -1);
            if (level.getBlockState(gapPos).isAir())
                original.call(instance, gapPos);
        } else if (oldState.getBlock() instanceof CannonContraptionProviderBlock providerBlock) {
            for (BlockPos gapPos : CBCSableConnectivityUtils.getCannonMountGaps(blockPos, level, providerBlock.getFacing(oldState))) {
                if (level.getBlockState(gapPos).isAir())
                    original.call(instance, gapPos);
            }
        }
    }

}
