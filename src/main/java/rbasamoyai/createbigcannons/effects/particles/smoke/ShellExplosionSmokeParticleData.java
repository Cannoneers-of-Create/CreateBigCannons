package rbasamoyai.createbigcannons.effects.particles.smoke;

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

public record ShellExplosionSmokeParticleData(int lifetime, float scale) implements ParticleOptions, ICustomParticleDataWithSprite<ShellExplosionSmokeParticleData> {

	private static final MapCodec<ShellExplosionSmokeParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime),
		Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, ShellExplosionSmokeParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, ShellExplosionSmokeParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, p -> p.lifetime,
        ByteBufCodecs.FLOAT, p -> p.scale,
        ShellExplosionSmokeParticleData::new
    );

	public ShellExplosionSmokeParticleData() { this(60, 3); }

	@Override public MapCodec<ShellExplosionSmokeParticleData> getCodec(ParticleType<ShellExplosionSmokeParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, ShellExplosionSmokeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<ShellExplosionSmokeParticleData> getMetaFactory() {
		return ShellExplosionSmokeParticle.Provider::new;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.SHELL_EXPLOSION_SMOKE.get();
	}

}
