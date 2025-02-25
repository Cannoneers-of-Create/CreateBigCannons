package rbasamoyai.createbigcannons.multiloader.forge;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.MilkSaturatedEffect;

public class CBCMobEffectsImpl {
	public static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CreateBigCannons.MOD_ID);

	public static final RegistryObject<MobEffect> MILK_SATURATED_EFFECT = MOB_EFFECT.register("milk_saturated",
		() -> new MilkSaturatedEffect(MobEffectCategory.NEUTRAL, 0xffffff));

	public static void register(IEventBus eventBus){
		MOB_EFFECT.register(eventBus);
	}

	public static MobEffect getMilkSaturatedEffect() {
		return MILK_SATURATED_EFFECT.get();
	}
}
