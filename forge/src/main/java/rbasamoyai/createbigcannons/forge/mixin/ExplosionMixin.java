package rbasamoyai.createbigcannons.forge.mixin;

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

	@WrapOperation(method = "finalizeExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;onBlockExploded(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;)V"))
	public void onBlockExploded(BlockState instance, Level level, BlockPos pos, Explosion explosion, Operation<Void> original) {
		if (instance.getBlock() instanceof CBCExplodableBlock cbcExplodable)
			cbcExplodable.createbigcannons$onBlockExplode(level, pos, instance, explosion);
		original.call(instance, level, pos, explosion);
	}

}
