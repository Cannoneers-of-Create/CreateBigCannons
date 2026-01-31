package rbasamoyai.createbigcannons.effects.particles.smoke;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class FallbackCannonSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<FallbackCannonSmokeParticleData> {

	public static final MapCodec<FallbackCannonSmokeParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.FLOAT.fieldOf("power")
				.forGetter(data -> data.power),
			Codec.FLOAT.fieldOf("size")
				.forGetter(data -> data.size),
			Codec.INT.fieldOf("lifetime")
				.forGetter(data -> data.lifetime),
			Codec.FLOAT.fieldOf("friction")
				.forGetter(data -> data.friction))
		.apply(i, FallbackCannonSmokeParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, FallbackCannonSmokeParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, p -> p.power,
        ByteBufCodecs.FLOAT, p -> p.size,
        ByteBufCodecs.VAR_INT, p -> p.lifetime,
        ByteBufCodecs.FLOAT, p -> p.friction,
        FallbackCannonSmokeParticleData::new
    );

	private final float power;
	private final float size;
	private final int lifetime;
	private final float friction;

	public FallbackCannonSmokeParticleData(float power, float size, int lifetime, float friction) {
		this.power = power;
		this.size = size;
		this.lifetime = lifetime;
        this.friction = friction;
    }

	public FallbackCannonSmokeParticleData() {
		this(0, 1, 1, 1);
	}

	public FallbackCannonSmokeParticleData(CannonSmokeParticleData source) {
		this(source.power(), source.size(), source.lifetime(), source.friction());
	}

	public float power() { return this.power; }
	public float size() { return this.size; }
	public int lifetime() { return this.lifetime; }
	public float friction() { return this.friction; }

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.CANNON_SMOKE_FALLBACK.get();
	}

	@Override
	public MapCodec<FallbackCannonSmokeParticleData> getCodec(ParticleType<FallbackCannonSmokeParticleData> type) {
		return CODEC;
	}

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, FallbackCannonSmokeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public SpriteParticleRegistration<FallbackCannonSmokeParticleData> getMetaFactory() {
		return FallbackCannonSmokeParticle.Provider::new;
	}

}
