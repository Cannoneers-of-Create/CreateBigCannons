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

public class FlakCloudParticleData implements ParticleOptions, ICustomParticleData<FlakCloudParticleData> {

	private static final Deserializer<FlakCloudParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public FlakCloudParticleData fromCommand(ParticleType<FlakCloudParticleData> particleType, StringReader reader) {
            return new FlakCloudParticleData();
        }

        @Override
        public FlakCloudParticleData fromNetwork(ParticleType<FlakCloudParticleData> particleType, FriendlyByteBuf buffer) {
            return new FlakCloudParticleData();
        }
    };

	private static final Codec<FlakCloudParticleData> CODEC = Codec.unit(FlakCloudParticleData::new);

	@Override public Deserializer<FlakCloudParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<FlakCloudParticleData> getCodec(ParticleType<FlakCloudParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<FlakCloudParticleData> getFactory() {
		return new FlakCloudParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.FLAK_CLOUD.get(); }

	@Override public void writeToNetwork(FriendlyByteBuf buffer) {}
	@Override public String writeToString() { return ""; }

}
