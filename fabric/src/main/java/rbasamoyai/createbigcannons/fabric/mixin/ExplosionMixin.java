package rbasamoyai.createbigcannons.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.remix.CBCExplodableBlock;

@Mixin(Explosion.class)
public class ExplosionMixin {

	@WrapOperation(method = "finalizeExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private boolean createbigcannons$finalizeExplosion(Level level, BlockPos pos, BlockState newState, int flags, Operation<Boolean> original) {
		BlockState oldState = level.getBlockState(pos);
		if (oldState.getBlock() instanceof CBCExplodableBlock cbcExplodable)
			cbcExplodable.createbigcannons$onBlockExplode(level, pos, oldState, (Explosion) (Object) this);
		return original.call(level, pos, newState, flags);
	}

}
