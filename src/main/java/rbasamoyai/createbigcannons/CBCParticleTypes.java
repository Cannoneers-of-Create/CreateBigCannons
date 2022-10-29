package rbasamoyai.createbigcannons;

import com.mojang.serialization.Codec;

import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rbasamoyai.createbigcannons.cannonmount.CannonPlumeParticleData;
import rbasamoyai.createbigcannons.cannonmount.CannonSmokeParticleData;
import rbasamoyai.createbigcannons.munitions.fluidshell.FluidBlobParticleData;

public class CBCParticleTypes {

	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CreateBigCannons.MOD_ID);
	
	public static final RegistryObject<ParticleType<CannonPlumeParticleData>> CANNON_PLUME = PARTICLE_TYPES.register("cannon_plume",
			() -> new ParticleType<CannonPlumeParticleData>(false, CannonPlumeParticleData.DESERIALIZER) {
				@Override
				public Codec<CannonPlumeParticleData> codec() {
					return CannonPlumeParticleData.CODEC;
				}
			});

	public static final RegistryObject<ParticleType<FluidBlobParticleData>> FLUID_BLOB = PARTICLE_TYPES.register("fluid_blob",
			() -> new ParticleType<FluidBlobParticleData>(false, FluidBlobParticleData.DESERIALIZER) {
				@Override
				public Codec<FluidBlobParticleData> codec() {
					return FluidBlobParticleData.CODEC;
				}
			});
	
	public static final RegistryObject<ParticleType<CannonSmokeParticleData>> CANNON_SMOKE = PARTICLE_TYPES.register("cannon_smoke",
			() -> new ParticleType<CannonSmokeParticleData>(false, CannonSmokeParticleData.DESERIALIZER) {
				@Override
				public Codec<CannonSmokeParticleData> codec() {
					return CannonSmokeParticleData.CODEC;
				}
			});
	
}
