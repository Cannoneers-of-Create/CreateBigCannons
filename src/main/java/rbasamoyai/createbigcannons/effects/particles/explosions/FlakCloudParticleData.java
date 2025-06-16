package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.createmod.catnip.annotations.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class FlakCloudParticleData implements ParticleOptions, ICustomParticleData<FlakCloudParticleData> {

	private static final MapCodec<FlakCloudParticleData> CODEC = MapCodec.unit(FlakCloudParticleData::new);

    private static final StreamCodec<RegistryFriendlyByteBuf, FlakCloudParticleData> STREAM_CODEC = StreamCodec.unit(new FlakCloudParticleData());

	@Override public MapCodec<FlakCloudParticleData> getCodec(ParticleType<FlakCloudParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, FlakCloudParticleData> getStreamCodec() { return STREAM_CODEC; }

    @Environment(Environment.EnvType.CLIENT)
	@Override
	public ParticleProvider<FlakCloudParticleData> getFactory() {
		return new FlakCloudParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.FLAK_CLOUD.get(); }

}
