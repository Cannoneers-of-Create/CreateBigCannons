package rbasamoyai.createbigcannons.index;

import java.util.function.Supplier;

import com.simibubi.create.foundation.particle.ICustomParticleData;
import com.simibubi.create.foundation.utility.Lang;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import rbasamoyai.createbigcannons.effects.particles.explosions.FlakCloudParticleData;
import rbasamoyai.createbigcannons.effects.particles.explosions.FluidCloudParticleData;
import rbasamoyai.createbigcannons.effects.particles.explosions.ShrapnelCloudParticleData;
import rbasamoyai.createbigcannons.effects.particles.plumes.AutocannonPlumeParticleData;
import rbasamoyai.createbigcannons.effects.particles.plumes.BigCannonPlumeParticleData;
import rbasamoyai.createbigcannons.effects.particles.plumes.DropMortarPlumeParticleData;
import rbasamoyai.createbigcannons.effects.particles.smoke.CannonSmokeParticleData;
import rbasamoyai.createbigcannons.effects.particles.smoke.FlakSmokeParticleData;
import rbasamoyai.createbigcannons.effects.particles.smoke.QuickFiringBreechSmokeParticleData;
import rbasamoyai.createbigcannons.effects.particles.smoke.ShrapnelSmokeParticleData;
import rbasamoyai.createbigcannons.effects.particles.smoke.SmokeShellSmokeParticleData;
import rbasamoyai.createbigcannons.effects.particles.smoke.TrailSmokeParticleData;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobParticleData;

public enum CBCParticleTypes {

	AUTOCANNON_PLUME(AutocannonPlumeParticleData::new),
	BIG_CANNON_PLUME(BigCannonPlumeParticleData::new),
	CANNON_SMOKE(CannonSmokeParticleData::new),
	DROP_MORTAR_PLUME(DropMortarPlumeParticleData::new),
	FLAK_CLOUD(FlakCloudParticleData::new),
	FLAK_SMOKE(FlakSmokeParticleData::new),
	FLUID_BLOB(FluidBlobParticleData::new),
	FLUID_CLOUD(FluidCloudParticleData::new),
	QUICK_FIRING_BREECH_SMOKE(QuickFiringBreechSmokeParticleData::new),
	SHRAPNEL_CLOUD(ShrapnelCloudParticleData::new),
	SHRAPNEL_SMOKE(ShrapnelSmokeParticleData::new),
	SMOKE_SHELL_SMOKE(SmokeShellSmokeParticleData::new),
	TRAIL_SMOKE(TrailSmokeParticleData::new);

	private final ParticleEntry<?> entry;

	<D extends ParticleOptions> CBCParticleTypes(Supplier<ICustomParticleData<D>> typeFactory) {
		String name = Lang.asId(name());
		this.entry = new ParticleEntry<>(name, typeFactory);
	}

	public static void register() {
		IndexPlatform.registerDeferredParticles();
	}

	@Environment(EnvType.CLIENT)
	public static void registerFactories() {
		ParticleEngine particles = Minecraft.getInstance().particleEngine;
		for (CBCParticleTypes particle : values())
			particle.entry.registerFactory(particles);
	}

	public ParticleType<?> get() {
		return this.entry.object;
	}

	public String parameter() {
		return this.entry.name;
	}

	private static class ParticleEntry<D extends ParticleOptions> {
		private final String name;
		private final Supplier<? extends ICustomParticleData<D>> typeFactory;
		private final ParticleType<D> object;

		public ParticleEntry(String name, Supplier<? extends ICustomParticleData<D>> typeFactory) {
			this.name = name;
			this.typeFactory = typeFactory;

			this.object = this.typeFactory.get().createType();
			IndexPlatform.registerDeferredParticleType(this.name, this.object);
		}

		@Environment(EnvType.CLIENT)
		public void registerFactory(ParticleEngine particles) {
			this.typeFactory.get().register(this.object, particles);
		}
	}

}
