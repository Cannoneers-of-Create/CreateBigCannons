package rbasamoyai.createbigcannons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rbasamoyai.createbigcannons.datagen.LangGen;

@Mod(CreateBigCannons.MOD_ID)
public class CreateBigCannons {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "createbigcannons";
	
	private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(MOD_ID);
	
	public CreateBigCannons() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		LangGen.prepare();
		
		ModGroup.register();
		CBCBlocks.register();
		CBCBlockEntities.register();
		CBCEntityTypes.register();
		CBCContraptionTypes.prepare();
		
		CBCParticleTypes.PARTICLE_TYPES.register(modEventBus);		
		
		CBCTags.register();
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.prepareClient(modEventBus));
	}
	
	private void prepareClient(IEventBus modEventBus) {
		CBCBlockPartials.init();
		modEventBus.addListener(CBCParticles::onRegisterParticleFactories);
	}
	
	public static CreateRegistrate registrate() {
		return REGISTRATE.get();
	}
	
	public static ResourceLocation resource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
}
