package rbasamoyai.createbigcannons.effects.particles.plumes;

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

public class BigCannonPlumeParticleData implements ParticleOptions, ICustomParticleData<BigCannonPlumeParticleData> {

	public static final MapCodec<BigCannonPlumeParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.FLOAT.fieldOf("size")
			.forGetter(data -> data.size),
		Codec.FLOAT.fieldOf("power")
			.forGetter(data -> data.power),
		Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime))
		.apply(i, BigCannonPlumeParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, BigCannonPlumeParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, p -> p.size,
        ByteBufCodecs.FLOAT, p -> p.power,
        ByteBufCodecs.VAR_INT, p -> p.lifetime,
        BigCannonPlumeParticleData::new
    );

	private final float size;
	private final float power;
	private final int lifetime;

	public BigCannonPlumeParticleData(float size, float power, int lifetime) {
		this.size = size;
		this.power = power;
		this.lifetime = lifetime;
	}

	public BigCannonPlumeParticleData(float size) { this(size, size, 10); }

	public BigCannonPlumeParticleData() { this(0, 0, 1); }

	public float size() { return this.size; }
	public float power() { return this.power; }
	public float lifetime() { return this.lifetime; }

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.BIG_CANNON_PLUME.get();
	}

	@Override
	public MapCodec<BigCannonPlumeParticleData> getCodec(ParticleType<BigCannonPlumeParticleData> type) {
		return CODEC;
	}

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, BigCannonPlumeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<BigCannonPlumeParticleData> getFactory() {
		return new BigCannonPlumeParticle.Provider();
	}

}
