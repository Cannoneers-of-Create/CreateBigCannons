package rbasamoyai.createbigcannons.effects.particles.splashes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record SplashSprayParticleData(float r, float g, float b, float size, float light, int lifetime) implements ParticleOptions,
	ICustomParticleDataWithSprite<SplashSprayParticleData> {

	private static final MapCodec<SplashSprayParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.FLOAT.fieldOf("r")
			.forGetter(data -> data.r),
		Codec.FLOAT.fieldOf("g")
			.forGetter(data -> data.g),
		Codec.FLOAT.fieldOf("b")
			.forGetter(data -> data.b),
		Codec.FLOAT.fieldOf("size")
			.forGetter(data -> data.size),
		Codec.FLOAT.fieldOf("light")
			.forGetter(data -> data.light),
		Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime))
		.apply(i, SplashSprayParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, SplashSprayParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, p -> p.r,
        ByteBufCodecs.FLOAT, p -> p.g,
        ByteBufCodecs.FLOAT, p -> p.b,
        ByteBufCodecs.FLOAT, p -> p.size,
        ByteBufCodecs.FLOAT, p -> p.light,
        ByteBufCodecs.VAR_INT, p -> p.lifetime,
        SplashSprayParticleData::new
    );

	public SplashSprayParticleData() { this(1, 1, 1, 1, 1, 1); }

	@Override public MapCodec<SplashSprayParticleData> getCodec(ParticleType<SplashSprayParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, SplashSprayParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<SplashSprayParticleData> getMetaFactory() {
		return SplashSprayParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPLASH_SPRAY.get(); }

}
