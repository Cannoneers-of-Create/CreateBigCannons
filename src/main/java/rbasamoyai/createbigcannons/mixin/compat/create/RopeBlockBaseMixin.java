package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(targets = "com.simibubi.create.content.contraptions.pulley.PulleyBlock$RopeBlockBase")
public abstract class RopeBlockBaseMixin {

	@WrapOperation(method = "onRemove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 1))
	private BlockState createbigcannons$onRemove(Level instance, BlockPos pos, Operation<BlockState> original, @Local(name = "isMoving") boolean isMoving) {
        BlockState ret = original.call(instance, pos);
		ContraptionRemix.removeInnerStateRopes(instance, pos.above(), isMoving);
		ContraptionRemix.removeInnerStateRopes(instance, pos.below(), isMoving);
        return ret;
    }

}
