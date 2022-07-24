package rbasamoyai.createbigcannons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rbasamoyai.createbigcannons.cannonmount.CannonPlumeParticle;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;

public class CreateBigCannonsClient {

	public static void prepareClient(IEventBus modEventBus) {
		CBCBlockPartials.init();
		modEventBus.addListener(CreateBigCannonsClient::onClientSetup);
		modEventBus.addListener(CreateBigCannonsClient::onRegisterParticleFactories);
	}
	
	public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event) {
		@SuppressWarnings("resource")
		ParticleEngine engine = Minecraft.getInstance().particleEngine;
		
		engine.register(CBCParticleTypes.CANNON_PLUME.get(), new CannonPlumeParticle.Provider());
	}
	
	public static void onClientSetup(FMLClientSetupEvent event) {
		CBCPonderIndex.register();
		CBCPonderIndex.registerTags();
	}
	
}
