package rbasamoyai.createbigcannons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.CannonCastShape;
import rbasamoyai.createbigcannons.network.CBCNetwork;

@Mod(CreateBigCannons.MOD_ID)
public class CreateBigCannons {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "createbigcannons";
	
	private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(MOD_ID);
	
	public CreateBigCannons() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
		ModLoadingContext mlContext = ModLoadingContext.get();
		
		ModGroup.register();
		CBCBlocks.register();
		CBCItems.register();
		CBCBlockEntities.register();
		CBCEntityTypes.register();
		CBCMenuTypes.register();
		CBCFluids.register();
		
		CannonCastShape.register();
		CBCContraptionTypes.prepare();
		CBCChecks.register();
		
		CBCParticleTypes.PARTICLE_TYPES.register(modEventBus);		
		
		CBCTags.register();
		
		modEventBus.addListener(this::onCommonSetup);
		
		CBCConfigs.registerConfigs(mlContext);
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateBigCannonsClient.prepareClient(modEventBus, forgeEventBus));
	}
	
	public void onCommonSetup(FMLCommonSetupEvent event) {
		CBCNetwork.init();
	}
	
	public static CreateRegistrate registrate() {
		return REGISTRATE.get();
	}
	
	public static ResourceLocation resource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
}
