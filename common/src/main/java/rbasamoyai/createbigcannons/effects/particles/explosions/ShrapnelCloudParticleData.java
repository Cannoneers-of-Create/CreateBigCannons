package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class ShrapnelCloudParticleData implements ParticleOptions, ICustomParticleData<ShrapnelCloudParticleData> {

	private static final Deserializer<ShrapnelCloudParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public ShrapnelCloudParticleData fromCommand(ParticleType<ShrapnelCloudParticleData> particleType, StringReader reader) {
            return new ShrapnelCloudParticleData();
        }

        @Override
        public ShrapnelCloudParticleData fromNetwork(ParticleType<ShrapnelCloudParticleData> particleType, FriendlyByteBuf buffer) {
            return new ShrapnelCloudParticleData();
        }
    };

	private static final Codec<ShrapnelCloudParticleData> CODEC = Codec.unit(ShrapnelCloudParticleData::new);

	@Override public Deserializer<ShrapnelCloudParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<ShrapnelCloudParticleData> getCodec(ParticleType<ShrapnelCloudParticleData> type) { return CODEC; }

	@Override
	public ParticleProvider<ShrapnelCloudParticleData> getFactory() {
		return new ShrapnelCloudParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SHRAPNEL_CLOUD.get(); }

	@Override public void writeToNetwork(FriendlyByteBuf buffer) {}
	@Override public String writeToString() { return ""; }

}
