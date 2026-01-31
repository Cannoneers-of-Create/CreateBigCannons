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

public class AutocannonPlumeParticleData implements ParticleOptions, ICustomParticleData<AutocannonPlumeParticleData> {

	public static final MapCodec<AutocannonPlumeParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, AutocannonPlumeParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, AutocannonPlumeParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, p -> p.scale,
        AutocannonPlumeParticleData::new
    );

	private final float scale;

	public AutocannonPlumeParticleData(float scale) {
		this.scale = scale;
	}

	public AutocannonPlumeParticleData() {
		this(0);
	}

	public float scale() {
		return this.scale;
	}


	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.AUTOCANNON_PLUME.get();
	}

	@Override
	public MapCodec<AutocannonPlumeParticleData> getCodec(ParticleType<AutocannonPlumeParticleData> type) {
		return CODEC;
	}

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, AutocannonPlumeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<AutocannonPlumeParticleData> getFactory() {
		return new AutocannonPlumeParticle.Provider();
	}

}
