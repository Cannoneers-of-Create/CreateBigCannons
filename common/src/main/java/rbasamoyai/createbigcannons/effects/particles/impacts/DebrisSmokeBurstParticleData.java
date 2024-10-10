package rbasamoyai.createbigcannons.effects.particles.impacts;

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

public record DebrisSmokeBurstParticleData() implements ParticleOptions, ICustomParticleData<DebrisSmokeBurstParticleData> {

	private static final Deserializer<DebrisSmokeBurstParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public DebrisSmokeBurstParticleData fromCommand(ParticleType<DebrisSmokeBurstParticleData> particleType, StringReader reader) {
            return new DebrisSmokeBurstParticleData();
        }

        @Override
        public DebrisSmokeBurstParticleData fromNetwork(ParticleType<DebrisSmokeBurstParticleData> particleType, FriendlyByteBuf buffer) {
            return new DebrisSmokeBurstParticleData();
        }
    };

	private static final Codec<DebrisSmokeBurstParticleData> CODEC = Codec.unit(DebrisSmokeBurstParticleData::new);

	@Override public Deserializer<DebrisSmokeBurstParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<DebrisSmokeBurstParticleData> getCodec(ParticleType<DebrisSmokeBurstParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<DebrisSmokeBurstParticleData> getFactory() {
		return new DebrisSmokeBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.DEBRIS_SMOKE_BURST.get(); }

	@Override public void writeToNetwork(FriendlyByteBuf buffer) {}
	@Override public String writeToString() { return ""; }

}
