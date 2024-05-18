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
import rbasamoyai.createbigcannons.effects.AutocannonPlumeParticleData;
import rbasamoyai.createbigcannons.effects.BigCannonPlumeParticleData;
import rbasamoyai.createbigcannons.effects.CannonSmokeParticleData;
import rbasamoyai.createbigcannons.effects.DropMortarPlumeParticleData;
import rbasamoyai.createbigcannons.effects.SmokeShellSmokeParticleData;
import rbasamoyai.createbigcannons.effects.TrailSmokeParticleData;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobParticleData;

public enum CBCParticleTypes {

	AUTOCANNON_PLUME(AutocannonPlumeParticleData::new),
	BIG_CANNON_PLUME(BigCannonPlumeParticleData::new),
	CANNON_SMOKE(CannonSmokeParticleData::new),
	DROP_MORTAR_PLUME(DropMortarPlumeParticleData::new),
	FLUID_BLOB(FluidBlobParticleData::new),
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
