package rbasamoyai.createbigcannons.neoforge;

import com.simibubi.create.foundation.utility.DistExecutor;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
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
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;*/
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorInspectionToolItem;
import rbasamoyai.createbigcannons.cannon_control.config.DefaultCannonMountPropertiesSerializers;
import rbasamoyai.createbigcannons.compat.copycats.CopycatsCompat;
import rbasamoyai.createbigcannons.compat.create.DefaultCreateCompat;
import rbasamoyai.createbigcannons.compat.curios.CBCCuriosIntegration;
import rbasamoyai.createbigcannons.compat.framedblocks.FramedBlocksCompat;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskItem;
import rbasamoyai.createbigcannons.neoforge.network.CBCNetworkForge;
import rbasamoyai.createbigcannons.index.CBCArmInteractionPointTypes;
import rbasamoyai.createbigcannons.index.CBCContraptionTypes;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.DefaultFluidCompat;

@Mod(CreateBigCannons.MOD_ID)
public class CreateBigCannonsForge {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_REGISTER = DeferredRegister.create(Registries.PARTICLE_TYPE, CreateBigCannons.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreateBigCannons.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, CreateBigCannons.MOD_ID);

    public CreateBigCannonsForge(IEventBus modEventBus) {
        IEventBus forgeEventBus = NeoForge.EVENT_BUS;
        ModContainer mlContext = ModLoadingContext.get().getActiveContainer();

        RECIPE_SERIALIZER_REGISTER.register(modEventBus);
        RECIPE_TYPE_REGISTER.register(modEventBus);

        CreateBigCannons.REGISTRATE.registerEventListeners(modEventBus);
        CreateBigCannons.init();
		ModGroupImpl.registerForge(modEventBus);
        CBCParticleTypes.register();
        CBCConfigs.register(mlContext::registerConfig);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onNewRegistry);
        modEventBus.addListener(this::onLoadConfig);
        modEventBus.addListener(this::onReloadConfig);
        modEventBus.addListener(this::onRegisterSounds);
		modEventBus.addListener(this::onRegister);

        CBCCommonForgeEvents.register(forgeEventBus);

		CBCModsForge.CURIOS.executeIfInstalled(() -> () -> CBCCuriosIntegration.init(modEventBus, forgeEventBus));

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CBCClientForge.prepareClient(modEventBus, forgeEventBus));
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        CBCNetworkForge.init();
		BlockArmorInspectionToolItem.registerDefaultHandlers();
		GasMaskItem.registerDefaultHandlers();
        DefaultFluidCompat.registerMinecraftBlobEffects();
        DefaultFluidCompat.registerCreateBlobEffects();

		CreateBigCannons.onCommonSetup();
		DefaultCreateCompat.init();
		DefaultCannonMountPropertiesSerializers.init();
		CBCModsForge.COPYCATS.executeIfInstalled(() -> () -> CopycatsCompat.init(CBCModsForge.COPYCATS::getBlock));
		CBCModsForge.FRAMEDBLOCKS.executeIfInstalled(() -> () -> FramedBlocksCompat.init());
    }

    private void onNewRegistry(NewRegistryEvent evt) {
        evt.create(new RegistryBuilder<>(CBCRegistries.BLOCK_RECIPE_SERIALIZERS)
			/*.hasTags() fixme
			.allowModification()*/
			.defaultKey(CreateBigCannons.resource("cannon_casting")));

		evt.create(new RegistryBuilder<>(CBCRegistries.BLOCK_RECIPE_TYPES)
			/*.hasTags()
			.allowModification()*/
			.defaultKey(CreateBigCannons.resource("cannon_casting")));

		evt.create(new RegistryBuilder<>(CBCRegistries.CANNON_CAST_SHAPES)
			/*.hasTags()
			.allowModification()*/
			.defaultKey(CreateBigCannons.resource("very_small")));
    }

	private void onRegister(RegisterEvent evt) {
		ResourceKey<? extends Registry<?>> key = evt.getRegistryKey();
		if (CBCRegistries.BLOCK_RECIPE_SERIALIZERS.equals(key)) {
			BlockRecipeSerializer.register();
		} else if (CBCRegistries.BLOCK_RECIPE_TYPES.equals(key)) {
			BlockRecipeType.register();
		} else if (CBCRegistries.CANNON_CAST_SHAPES.equals(key)) {
			CannonCastShape.register();
		}
        CBCContraptionTypes.init();
        CBCArmInteractionPointTypes.init();
		FMLJavaModLoadingContext.get().getModEventBus().post(new CBCForgeRegisterEvent<>(CannonCastShape.class, CBCRegistries.cannonCastShapes()));
	}

    private void onRegisterSounds(RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, helper -> CBCSoundEvents.register(soundEntry -> soundEntry.register(helper)));
    }

    private void onLoadConfig(ModConfigEvent.Loading evt) {
        CBCConfigs.onLoad(evt.getConfig());
    }

    private void onReloadConfig(ModConfigEvent.Reloading evt) {
        CBCConfigs.onReload(evt.getConfig());
    }

}
