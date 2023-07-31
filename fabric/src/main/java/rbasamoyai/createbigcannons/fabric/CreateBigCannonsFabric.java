package rbasamoyai.createbigcannons.fabric;

import com.simibubi.create.AllSoundEvents;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.fabric.network.CBCNetworkFabric;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.DefaultFluidCompat;

public class CreateBigCannonsFabric implements ModInitializer {

	public static final LazyRegistrar<ParticleType<?>> PARTICLE_REGISTER = LazyRegistrar.create(Registry.PARTICLE_TYPE, CreateBigCannons.MOD_ID);

	@Override
	public void onInitialize() {
		CreateBigCannons.init();
		CreateBigCannons.REGISTRATE.register();
		CBCParticleTypes.register();
		CBCSoundEvents.register(AllSoundEvents.SoundEntry::register);

		CBCRegistries.init();
		CBCConfigs.registerConfigs((t, c) -> ModLoadingContext.registerConfig(CreateBigCannons.MOD_ID, t, c));

		CBCNetworkFabric.INSTANCE.initServerListener();
		DefaultFluidCompat.registerMinecraftBlobEffects();

		ModConfigEvent.LOADING.register(CBCConfigs::onLoad);
		ModConfigEvent.RELOADING.register(CBCConfigs::onReload);

		CBCCommonFabricEvents.register();
	}
}
