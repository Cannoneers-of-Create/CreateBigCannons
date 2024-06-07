package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelBurst;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin extends LivingEntity {

	@Shadow protected abstract void playBrokenSound();
	@Shadow protected abstract void showBreakingParticles();

	ArmorStandMixin(EntityType<? extends LivingEntity> type, Level level) { super(type, level); }

	@Inject(method = "hurt",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;getDirectEntity()Lnet/minecraft/world/entity/Entity;", ordinal = 0),
			cancellable = true)
	private void createbigcannons$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		Entity directEntity = source.getDirectEntity();
		if (!(directEntity instanceof ShrapnelBurst) && !(directEntity instanceof AbstractCannonProjectile<?>)) return;
		this.playBrokenSound();
		this.showBreakingParticles();
		this.kill();
		cir.setReturnValue(true);
	}

}
