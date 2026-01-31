package rbasamoyai.createbigcannons.effects.particles.splashes;

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

public record ProjectileSplashParticleData(float r, float g, float b, float light) implements ParticleOptions, ICustomParticleData<ProjectileSplashParticleData> {

	private static final MapCodec<ProjectileSplashParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.FLOAT.fieldOf("r")
			.forGetter(data -> data.r),
			Codec.FLOAT.fieldOf("g")
				.forGetter(data -> data.g),
			Codec.FLOAT.fieldOf("b")
				.forGetter(data -> data.b),
			Codec.FLOAT.fieldOf("light")
				.forGetter(data -> data.light))
		.apply(i, ProjectileSplashParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, ProjectileSplashParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, p -> p.r,
        ByteBufCodecs.FLOAT, p -> p.g,
        ByteBufCodecs.FLOAT, p -> p.b,
        ByteBufCodecs.FLOAT, p -> p.light,
        ProjectileSplashParticleData::new
    );

	public ProjectileSplashParticleData() { this(1, 1, 1, 1); }

	@Override public MapCodec<ProjectileSplashParticleData> getCodec(ParticleType<ProjectileSplashParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, ProjectileSplashParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<ProjectileSplashParticleData> getFactory() {
		return new ProjectileSplashParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.PROJECTILE_SPLASH.get(); }

}
