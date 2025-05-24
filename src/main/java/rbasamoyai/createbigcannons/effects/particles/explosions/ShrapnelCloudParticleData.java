package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class ShrapnelCloudParticleData implements ParticleOptions, ICustomParticleData<ShrapnelCloudParticleData> {

    private static final MapCodec<ShrapnelCloudParticleData> CODEC = MapCodec.unit(ShrapnelCloudParticleData::new);

    private static final StreamCodec<RegistryFriendlyByteBuf, ShrapnelCloudParticleData> STREAM_CODEC = StreamCodec.unit(new ShrapnelCloudParticleData());

	@Override public MapCodec<ShrapnelCloudParticleData> getCodec(ParticleType<ShrapnelCloudParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ShrapnelCloudParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<ShrapnelCloudParticleData> getFactory() {
		return new ShrapnelCloudParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SHRAPNEL_CLOUD.get(); }

}
