package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class FluidCloudParticleData implements ParticleOptions, ICustomParticleData<FluidCloudParticleData> {

	private static final Deserializer<FluidCloudParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public FluidCloudParticleData fromCommand(ParticleType<FluidCloudParticleData> particleType, StringReader reader) {
            return new FluidCloudParticleData();
        }

        @Override
        public FluidCloudParticleData fromNetwork(ParticleType<FluidCloudParticleData> particleType, FriendlyByteBuf buffer) {
            return new FluidCloudParticleData();
        }
    };

	private static final Codec<FluidCloudParticleData> CODEC = Codec.unit(FluidCloudParticleData::new);

	@Override public Deserializer<FluidCloudParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<FluidCloudParticleData> getCodec(ParticleType<FluidCloudParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<FluidCloudParticleData> getFactory() {
		return new FluidCloudParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.FLUID_CLOUD.get(); }

	@Override public void writeToNetwork(FriendlyByteBuf buffer) {}
	@Override public String writeToString() { return ""; }

}
