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

public class ShrapnelSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<ShrapnelSmokeParticleData> {

	private static final MapCodec<ShrapnelSmokeParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime))
		.apply(i, ShrapnelSmokeParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, ShrapnelSmokeParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, p -> p.lifetime,
        ShrapnelSmokeParticleData::new
    );

	private final int lifetime;

	public ShrapnelSmokeParticleData() { this(60); }

	public ShrapnelSmokeParticleData(int lifetime) {
		this.lifetime = lifetime;
	}

	public int lifetime() {
		return this.lifetime;
	}

	@Override
	public MapCodec<ShrapnelSmokeParticleData> getCodec(ParticleType<ShrapnelSmokeParticleData> type) {
		return CODEC;
	}

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, ShrapnelSmokeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<ShrapnelSmokeParticleData> getMetaFactory() {
		return ShrapnelSmokeParticle.Provider::new;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.SHRAPNEL_SMOKE.get();
	}

}
