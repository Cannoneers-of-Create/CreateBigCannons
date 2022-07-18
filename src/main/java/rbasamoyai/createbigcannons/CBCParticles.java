package rbasamoyai.createbigcannons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import rbasamoyai.createbigcannons.cannonmount.CannonPlumeParticle;

public class CBCParticles {

	public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event) {
		@SuppressWarnings("resource")
		ParticleEngine engine = Minecraft.getInstance().particleEngine;
		
		engine.register(CBCParticleTypes.CANNON_PLUME.get(), new CannonPlumeParticle.Provider());
	}
	
}
