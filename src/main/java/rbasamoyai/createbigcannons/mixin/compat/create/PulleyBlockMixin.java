package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.contraptions.pulley.PulleyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(PulleyBlock.class)
public abstract class PulleyBlockMixin {

	@WrapMethod(method = "onRemove")
	private void createbigcannons$onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, Operation<Void> original) {
        original.call(state, level, pos, newState, isMoving);
		ContraptionRemix.removeInnerStateRopes(level, pos.below(), isMoving);
	}

}
