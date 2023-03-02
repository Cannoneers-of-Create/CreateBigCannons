package rbasamoyai.createbigcannons;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rbasamoyai.createbigcannons.base.CBCCommonEvents;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.base.PartialBlockDamageManager;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.BlockRecipeFinder;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlob;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;
import rbasamoyai.createbigcannons.network.CBCNetwork;

@Mod(CreateBigCannons.MOD_ID)
public class CreateBigCannons {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "createbigcannons";
	
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public static final PartialBlockDamageManager BLOCK_DAMAGE = new PartialBlockDamageManager();
	
	public CreateBigCannons() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
		ModLoadingContext mlContext = ModLoadingContext.get();

		REGISTRATE.registerEventListeners(modEventBus);

		CBCRegistries.init();
		
		ModGroup.register();
		CBCBlocks.register();
		CBCItems.register();
		CBCBlockEntities.register();
		CBCEntityTypes.register();
		CBCMenuTypes.register();
		CBCFluids.register();
		CBCRecipeTypes.register(modEventBus);
		
		CannonCastShape.CANNON_CAST_SHAPES.register(modEventBus);
		CBCContraptionTypes.prepare();
		CBCChecks.register();
		BlockRecipeSerializer.register();
		BlockRecipeType.register();
		
		CBCParticleTypes.PARTICLE_TYPES.register(modEventBus);
		
		CBCTags.register();
		
		modEventBus.addListener(this::onCommonSetup);
		
		forgeEventBus.addListener(this::onAddReloadListeners);
		forgeEventBus.addListener(this::onDatapackSync);
		CBCCommonEvents.register(forgeEventBus);
		
		CBCConfigs.registerConfigs(mlContext);
		
		this.registerSerializers();
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateBigCannonsClient.prepareClient(modEventBus, forgeEventBus));
	}
	
	private void onCommonSetup(FMLCommonSetupEvent event) {
		CBCNetwork.init();
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
			BlockRecipesManager.syncToAll();
		} else {
			BlockRecipesManager.syncTo(player);
		}
	}
	
	public static ResourceLocation resource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
	private void registerSerializers() {
		EntityDataSerializers.registerSerializer(FluidBlob.FLUID_STACK_SERIALIZER);
	}
	
}
