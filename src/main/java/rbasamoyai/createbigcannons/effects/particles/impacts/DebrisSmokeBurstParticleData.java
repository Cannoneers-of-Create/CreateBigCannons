package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class DebrisSmokeBurstParticleData implements ParticleOptions, ICustomParticleData<DebrisSmokeBurstParticleData> {

    private static final DebrisSmokeBurstParticleData INSTANCE = new DebrisSmokeBurstParticleData();

    private DebrisSmokeBurstParticleData() {}

    public static DebrisSmokeBurstParticleData instance() { return INSTANCE; }

	private static final MapCodec<DebrisSmokeBurstParticleData> CODEC = MapCodec.unit(DebrisSmokeBurstParticleData::instance);

    private static final StreamCodec<RegistryFriendlyByteBuf, DebrisSmokeBurstParticleData> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	@Override public MapCodec<DebrisSmokeBurstParticleData> getCodec(ParticleType<DebrisSmokeBurstParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, DebrisSmokeBurstParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<DebrisSmokeBurstParticleData> getFactory() {
		return new DebrisSmokeBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.DEBRIS_SMOKE_BURST.get(); }

}
