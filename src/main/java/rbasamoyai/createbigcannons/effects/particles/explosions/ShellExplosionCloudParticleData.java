package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record ShellExplosionCloudParticleData(float scale, boolean isPlume) implements ParticleOptions,
	ICustomParticleData<ShellExplosionCloudParticleData> {

	private static final MapCodec<ShellExplosionCloudParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		Codec.FLOAT.fieldOf("scale").forGetter(ShellExplosionCloudParticleData::scale),
		Codec.BOOL.fieldOf("is_plume").forGetter(ShellExplosionCloudParticleData::isPlume)
	).apply(i, ShellExplosionCloudParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, ShellExplosionCloudParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, ShellExplosionCloudParticleData::scale,
        ByteBufCodecs.BOOL, ShellExplosionCloudParticleData::isPlume,
        ShellExplosionCloudParticleData::new
    );

	public ShellExplosionCloudParticleData() { this(0, false); }

	@Override public MapCodec<ShellExplosionCloudParticleData> getCodec(ParticleType<ShellExplosionCloudParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ShellExplosionCloudParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<ShellExplosionCloudParticleData> getFactory() {
		return new ShellExplosionCloudParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SHELL_EXPLOSION_CLOUD.get(); }

}
