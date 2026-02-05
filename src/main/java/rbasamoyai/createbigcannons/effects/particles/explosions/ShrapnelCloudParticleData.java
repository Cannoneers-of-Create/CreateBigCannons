package rbasamoyai.createbigcannons.effects.particles.explosions;

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

public class ShrapnelCloudParticleData implements ParticleOptions, ICustomParticleData<ShrapnelCloudParticleData> {

    private static final ShrapnelCloudParticleData INSTANCE = new ShrapnelCloudParticleData();

    private ShrapnelCloudParticleData() {}

    public static ShrapnelCloudParticleData instance() { return INSTANCE; }

    private static final MapCodec<ShrapnelCloudParticleData> CODEC = MapCodec.unit(ShrapnelCloudParticleData::instance);

    private static final StreamCodec<RegistryFriendlyByteBuf, ShrapnelCloudParticleData> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	@Override public MapCodec<ShrapnelCloudParticleData> getCodec(ParticleType<ShrapnelCloudParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ShrapnelCloudParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<ShrapnelCloudParticleData> getFactory() {
		return new ShrapnelCloudParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SHRAPNEL_CLOUD.get(); }

}
