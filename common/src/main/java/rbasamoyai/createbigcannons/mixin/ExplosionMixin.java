package rbasamoyai.createbigcannons.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;

import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import rbasamoyai.createbigcannons.remix.CustomExplosion;

@Mixin(Explosion.class)
public class ExplosionMixin {

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
