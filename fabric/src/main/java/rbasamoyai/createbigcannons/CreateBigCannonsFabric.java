package rbasamoyai.createbigcannons;

import net.fabricmc.api.ModInitializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.network.CBCNetworkFabric;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlob;

public class CreateBigCannonsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CreateBigCannons.init();
        CBCRegistries.init();
        CBCConfigs.registerConfigs((t, c) -> ModLoadingContext.registerConfig(CreateBigCannons.MOD_ID, t, c));

        CreateBigCannons.REGISTRATE.register();
        CBCNetworkFabric.init();
        FluidBlob.registerDefaultBlobEffects();

        ModConfigEvent.LOADING.register(CBCConfigs::onLoad);
        ModConfigEvent.RELOADING.register(CBCConfigs::onReload);

        this.registerSerializers();
    }

    private void registerSerializers() {
        EntityDataSerializers.registerSerializer(FluidBlob.FLUID_STACK_SERIALIZER);
    }

}
