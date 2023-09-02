package rbasamoyai.createbigcannons.fabric;

import com.simibubi.create.AllSoundEvents;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.fabric.network.CBCNetworkFabric;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.DefaultFluidCompat;

public class CreateBigCannonsFabric implements ModInitializer {

	public static final LazyRegistrar<ParticleType<?>> PARTICLE_REGISTER = LazyRegistrar.create(Registries.PARTICLE_TYPE, CreateBigCannons.MOD_ID);

	@Override
	public void onInitialize() {
		CreateBigCannons.init();
		CreateBigCannons.REGISTRATE.register();
		CBCParticleTypes.register();
		CBCSoundEvents.register(AllSoundEvents.SoundEntry::register);

		CBCRegistries.init();
		CBCConfigs.registerConfigs((t, c) -> ForgeConfigRegistry.INSTANCE.register(CreateBigCannons.MOD_ID, t, c));

		CBCNetworkFabric.INSTANCE.initServerListener();
		DefaultFluidCompat.registerMinecraftBlobEffects();

		ModConfigEvents.loading(CreateBigCannons.MOD_ID).register(CBCConfigs::onLoad);
		ModConfigEvents.reloading(CreateBigCannons.MOD_ID).register(CBCConfigs::onReload);

        CBCCommonFabricEvents.register();
    }

}
