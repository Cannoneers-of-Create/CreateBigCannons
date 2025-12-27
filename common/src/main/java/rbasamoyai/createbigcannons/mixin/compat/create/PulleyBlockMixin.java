package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.contraptions.pulley.PulleyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(PulleyBlock.class)
public abstract class PulleyBlockMixin {

	@Inject(method = "onRemove", at = @At("TAIL"))
	private void createbigcannons$onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci) {
		ContraptionRemix.removeInnerStateRopes(level, pos.below(), isMoving);
	}

}
