package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.createmod.catnip.annotations.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record DebrisSmokeBurstParticleData() implements ParticleOptions, ICustomParticleData<DebrisSmokeBurstParticleData> {

	private static final MapCodec<DebrisSmokeBurstParticleData> CODEC = MapCodec.unit(DebrisSmokeBurstParticleData::new);

    private static final StreamCodec<RegistryFriendlyByteBuf, DebrisSmokeBurstParticleData> STREAM_CODEC = StreamCodec.unit(new DebrisSmokeBurstParticleData());

	@Override public MapCodec<DebrisSmokeBurstParticleData> getCodec(ParticleType<DebrisSmokeBurstParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, DebrisSmokeBurstParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @Environment(Environment.EnvType.CLIENT)
	@Override
	public ParticleProvider<DebrisSmokeBurstParticleData> getFactory() {
		return new DebrisSmokeBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.DEBRIS_SMOKE_BURST.get(); }

}
