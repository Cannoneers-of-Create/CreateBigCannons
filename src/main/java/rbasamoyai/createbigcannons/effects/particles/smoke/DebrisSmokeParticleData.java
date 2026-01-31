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

public record DebrisSmokeParticleData(float scale) implements ParticleOptions, ICustomParticleDataWithSprite<DebrisSmokeParticleData> {

	private static final MapCodec<DebrisSmokeParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, DebrisSmokeParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, DebrisSmokeParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, p -> p.scale,
        DebrisSmokeParticleData::new
    );

	public DebrisSmokeParticleData() { this(0); }

	@Override public MapCodec<DebrisSmokeParticleData> getCodec(ParticleType<DebrisSmokeParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, DebrisSmokeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<DebrisSmokeParticleData> getMetaFactory() {
		return DebrisSmokeParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.DEBRIS_SMOKE.get(); }

}
