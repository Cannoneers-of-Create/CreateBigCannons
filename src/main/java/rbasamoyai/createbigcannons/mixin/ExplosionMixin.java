package rbasamoyai.createbigcannons.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import rbasamoyai.createbigcannons.remix.CustomExplosion;

@Mixin(Explosion.class)
public class ExplosionMixin {

	@Shadow @Final private Level level;

	@WrapOperation(method = "explode",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ExplosionDamageCalculator;getBlockExplosionResistance(Lnet/minecraft/world/level/Explosion;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Ljava/util/Optional;"))
    private Optional<Float> createbigcannons$explode(ExplosionDamageCalculator instance, Explosion explosion, BlockGetter reader,
                                                     BlockPos pos, BlockState state, FluidState fluid, Operation<Optional<Float>> original,
                                                     @Local(ordinal = 0) float power) {
        if (this instanceof CustomExplosion customExplosion)
            customExplosion.editBlock(this.level, pos, state, fluid, power);
        return original.call(instance, explosion, reader, pos, state, fluid);
	}

	@WrapOperation(method = "finalizeExplosion",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"))
	private void createbigcannons$finalizeExplosion(Level instance, double x, double y, double z, SoundEvent sound,
													SoundSource category, float volume, float pitch, boolean distanceDelay,
													Operation<Void> original) {
		if (this instanceof CustomExplosion customExplosion) {
			customExplosion.playLocalSound(instance, x, y, z);
		} else {
			original.call(instance, x, y, z, sound, category, volume, pitch, distanceDelay);
		}
	}

}
