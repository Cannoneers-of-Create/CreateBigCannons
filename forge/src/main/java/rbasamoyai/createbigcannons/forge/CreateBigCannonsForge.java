package rbasamoyai.createbigcannons.forge;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.NewRegistryEvent;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.BlockRecipeFinder;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlob;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;
import rbasamoyai.createbigcannons.forge.network.CBCNetworkForge;

@Mod(CreateBigCannons.MOD_ID)
public class CreateBigCannonsForge {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CreateBigCannons.MOD_ID);

    public CreateBigCannonsForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext mlContext = ModLoadingContext.get();

        CreateBigCannons.REGISTRATE.registerEventListeners(modEventBus);
        CreateBigCannons.init();
        CBCParticleTypes.register();
        CBCConfigs.registerConfigs(mlContext::registerConfig);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onNewRegistry);
        modEventBus.addListener(this::onLoadConfig);
        modEventBus.addListener(this::onReloadConfig);

        forgeEventBus.addListener(this::onAddReloadListeners);
        forgeEventBus.addListener(this::onDatapackSync);
        CBCCommonForgeEvents.register(forgeEventBus);

        this.registerSerializers();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CBCClientForge.prepareClient(modEventBus, forgeEventBus));
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        CBCNetworkForge.init();
        FluidBlob.registerDefaultBlobEffects();
    }

    private void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(BlockRecipeFinder.LISTENER);
        event.addListener(BlockRecipesManager.ReloadListener.INSTANCE);
        event.addListener(BlockHardnessHandler.ReloadListener.INSTANCE);
    }

    private void onDatapackSync(OnDatapackSyncEvent event) {
        ServerPlayer player = event.getPlayer();
        if (player == null) {
            BlockRecipesManager.syncToAll(event.getPlayerList().getServer());
        } else {
            BlockRecipesManager.syncTo(player);
        }
    }

    private void registerSerializers() {
        EntityDataSerializers.registerSerializer(FluidBlob.FLUID_STACK_SERIALIZER);
    }

    private void onNewRegistry(NewRegistryEvent evt) {
        CBCRegistries.init();
    }

    private void onLoadConfig(ModConfigEvent.Loading evt) { CBCConfigs.onLoad(evt.getConfig()); }
    private void onReloadConfig(ModConfigEvent.Reloading evt) { CBCConfigs.onReload(evt.getConfig()); }

}
