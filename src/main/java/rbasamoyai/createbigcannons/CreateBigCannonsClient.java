
package rbasamoyai.createbigcannons;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rbasamoyai.createbigcannons.cannonmount.CannonPlumeParticle;
import rbasamoyai.createbigcannons.cannonmount.CannonSmokeParticle;
import rbasamoyai.createbigcannons.munitions.fluidshell.FluidBlobParticle;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;

public class CreateBigCannonsClient {

	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		CBCBlockPartials.init();
		modEventBus.addListener(CreateBigCannonsClient::onClientSetup);
		modEventBus.addListener(CreateBigCannonsClient::onRegisterParticleFactories);
		
		forgeEventBus.addListener(CreateBigCannonsClient::getFogColor);
		forgeEventBus.addListener(CreateBigCannonsClient::getFogDensity);
	}
	
	public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		ParticleEngine engine = mc.particleEngine;
		
		engine.register(CBCParticleTypes.CANNON_PLUME.get(), new CannonPlumeParticle.Provider());
		engine.register(CBCParticleTypes.FLUID_BLOB.get(), new FluidBlobParticle.Provider());
		engine.register(CBCParticleTypes.CANNON_SMOKE.get(), CannonSmokeParticle.Provider::new);
	}
	
	public static void onClientSetup(FMLClientSetupEvent event) {
		CBCPonderIndex.register();
		CBCPonderIndex.registerTags();
		CBCBlockPartials.resolveDeferredModels();
	}
	
	public static void getFogColor(FogColors event) {
		Camera info = event.getCamera();
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		BlockPos blockPos = info.getBlockPosition();
		FluidState fluidState = level.getFluidState(blockPos);
		if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

		Fluid fluid = fluidState.getType();

		if (CBCFluids.MOLTEN_CAST_IRON.get().isSame(fluid)) {
			event.setRed(70 / 255f);
			event.setGreen(10 / 255f);
			event.setBlue(11 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_BRONZE.get().isSame(fluid)) {
			event.setRed(99 / 255f);
			event.setGreen(66 / 255f);
			event.setBlue(22 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_STEEL.get().isSame(fluid)) {
			event.setRed(111 / 255f);
			event.setGreen(110 / 255f);
			event.setBlue(106 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_NETHERSTEEL.get().isSame(fluid)) {
			event.setRed(76 / 255f);
			event.setGreen(50 / 255f);
			event.setBlue(58 / 255f);
			return;
		}
	}
	
	public static void getFogDensity(RenderFogEvent event) {
		if (!event.isCancelable()) return;
		
		Camera info = event.getCamera();
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		BlockPos blockPos = info.getBlockPosition();
		FluidState fluidState = level.getFluidState(blockPos);
		if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

		Fluid fluid = fluidState.getType();
		
		List<Fluid> moltenMetals = Arrays.asList(
				CBCFluids.MOLTEN_CAST_IRON.get(),
				CBCFluids.MOLTEN_BRONZE.get(),
				CBCFluids.MOLTEN_STEEL.get(),
				CBCFluids.MOLTEN_NETHERSTEEL.get());
		
		for (Fluid fluid1 : moltenMetals) {
			if (fluid1.isSame(fluid)) {
				event.scaleFarPlaneDistance(1f / 32f);
				event.setCanceled(true);
				return;
			}
		}
	}
	
}
