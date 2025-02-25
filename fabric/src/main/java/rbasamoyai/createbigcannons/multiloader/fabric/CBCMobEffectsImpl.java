package rbasamoyai.createbigcannons.multiloader.fabric;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.MilkSaturatedEffect;

public class CBCMobEffectsImpl {
	private static final ResourceLocation MILK_SATURATED_RESOURCE_LOCATION = CreateBigCannons.resource("milk_saturated");

	public static void registerAll(){
		Registry.register(
			BuiltInRegistries.MOB_EFFECT,
			MILK_SATURATED_RESOURCE_LOCATION,
			new MilkSaturatedEffect(MobEffectCategory.NEUTRAL, 0xffffff)
		);
	}

	public static MobEffect getMilkSaturatedEffect() {
		return BuiltInRegistries.MOB_EFFECT.get(MILK_SATURATED_RESOURCE_LOCATION);
	}
}
