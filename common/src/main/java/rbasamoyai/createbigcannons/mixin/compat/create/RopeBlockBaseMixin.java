package rbasamoyai.createbigcannons.mixin.compat.create;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(targets = "com.simibubi.create.content.contraptions.pulley.PulleyBlock$RopeBlockBase")
public abstract class RopeBlockBaseMixin {

	@Inject(method = "onRemove",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 1))
	private void createbigcannons$onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci) {
		ContraptionRemix.removeInnerStateRopes(level, pos.above(), isMoving);
		ContraptionRemix.removeInnerStateRopes(level, pos.below(), isMoving);
	}

}
