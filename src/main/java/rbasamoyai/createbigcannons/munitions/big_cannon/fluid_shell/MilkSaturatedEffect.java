package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class MilkSaturatedEffect extends MobEffect {
	public MilkSaturatedEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
		List<MobEffectInstance> instances = livingEntity.getActiveEffects().stream().toList();
		livingEntity.removeAllEffects();
		for (MobEffectInstance instance : instances) {
			if (instance.getEffect() instanceof MilkSaturatedEffect) {
				livingEntity.addEffect(
					instance
				);
			} else {
				livingEntity.addEffect(
					new MobEffectInstance(
						instance.getEffect(),
						instance.getDuration() - (80 + (amplifier * 60)),
						instance.getAmplifier(),
						instance.isAmbient(),
						instance.isVisible(),
						instance.showIcon()
					)
				);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 20 == 0;
	}
}
