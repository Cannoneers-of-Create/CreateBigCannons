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

public record FlakSmokeParticleData(int lifetime, float scale) implements ParticleOptions, ICustomParticleDataWithSprite<FlakSmokeParticleData> {

	private static final MapCodec<FlakSmokeParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime),
		Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, FlakSmokeParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, FlakSmokeParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, p -> p.lifetime,
        ByteBufCodecs.FLOAT, p -> p.scale,
        FlakSmokeParticleData::new
    );

	public FlakSmokeParticleData() { this(60, 3); }

	@Override public MapCodec<FlakSmokeParticleData> getCodec(ParticleType<FlakSmokeParticleData> type) {
		return CODEC;
	}

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, FlakSmokeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<FlakSmokeParticleData> getMetaFactory() {
		return FlakSmokeParticle.Provider::new;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.FLAK_SMOKE.get();
	}

}
