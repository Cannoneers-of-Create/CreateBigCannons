package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(MechanicalPistonBlock.class)
public abstract class MechanicalPistonBlockMixin {

	@Inject(method = "playerWillDestroy",
			at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/piston/MechanicalPistonBlock;isExtensionPole(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 1, shift = At.Shift.BEFORE))
	private void createbigcannons$playerWillDestroy$0(Level level, BlockPos pos, BlockState state, Player player, CallbackInfo ci,
													  @Local Direction direction,
													  @Local(ordinal = 3) BlockPos currentPos,
													  @Local(ordinal = 1) LocalRef<BlockState> blockRef) {
		blockRef.set(ContraptionRemix.getInnerCannonState(level, blockRef.get(), currentPos, direction));
	}

	@Redirect(method = "playerWillDestroy",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
	private boolean createbigcannons$playerWillDestroy$1(Level instance, BlockPos pos, boolean drops) {
		return !ContraptionRemix.removeCannonContentsOnBreak(instance, pos, drops) && instance.destroyBlock(pos, drops);
	}

}
