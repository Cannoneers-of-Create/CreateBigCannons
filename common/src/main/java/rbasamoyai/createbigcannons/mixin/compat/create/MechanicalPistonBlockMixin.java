package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(MechanicalPistonBlock.class)
public abstract class MechanicalPistonBlockMixin {

    @ModifyExpressionValue(method = "playerWillDestroy",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
	private BlockState createbigcannons$playerWillDestroy$0(BlockState state,
                                                            @Local(argsOnly = true) Level level,
                                                            @Local Direction direction,
                                                            @Local(ordinal = 3) BlockPos currentPos) {
		return ContraptionRemix.getInnerCannonState(level, state, currentPos, direction);
    }

	@WrapOperation(method = "playerWillDestroy",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
	private boolean createbigcannons$playerWillDestroy$1(Level instance, BlockPos pos, boolean drops, Operation<Boolean> original) {
		return !ContraptionRemix.removeCannonContentsOnBreak(instance, pos, drops) && original.call(instance, pos, drops);
	}

}
