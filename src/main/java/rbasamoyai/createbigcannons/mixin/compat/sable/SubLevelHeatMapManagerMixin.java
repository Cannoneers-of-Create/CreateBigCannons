package rbasamoyai.createbigcannons.mixin.compat.sable;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.plot.heat.SubLevelHeatMapManager;
import net.minecraft.core.BlockPos;
import rbasamoyai.createbigcannons.compat.sable.CBCSableConnectivityUtils;

@Mixin(SubLevelHeatMapManager.class)
public class SubLevelHeatMapManagerMixin {

    @Shadow @Final @Nonnull private ServerSubLevel subLevel;

    @WrapOperation(method = "step", at = @At(value = "INVOKE", target = "Ldev/ryanhcode/sable/sublevel/plot/heat/SubLevelHeatMapManager;isSolidAt(Lnet/minecraft/core/BlockPos;)Z", ordinal = 0))
    private boolean createbigcannons$step$isSolidAt$0(SubLevelHeatMapManager instance, BlockPos blockPos, Operation<Boolean> original) {
        if (original.call(instance, blockPos))
            return true;
        return CBCSableConnectivityUtils.isCannonMountAirGap(blockPos, this.subLevel.getLevel());
    }

    @WrapOperation(method = "step", at = @At(value = "INVOKE", target = "Ldev/ryanhcode/sable/sublevel/plot/heat/SubLevelHeatMapManager;isSolidAt(Lnet/minecraft/core/BlockPos;)Z", ordinal = 3))
    private boolean createbigcannons$step$isSolidAt$3(SubLevelHeatMapManager instance, BlockPos blockPos, Operation<Boolean> original) {
        if (original.call(instance, blockPos))
            return true;
        return CBCSableConnectivityUtils.isCannonMountAirGap(blockPos, this.subLevel.getLevel());
    }

}
