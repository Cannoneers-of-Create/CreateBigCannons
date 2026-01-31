package rbasamoyai.createbigcannons.effects.particles.impacts;

import org.joml.Vector3f;

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
import net.minecraft.util.ExtraCodecs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record SparkBurstParticleData(Vector3f color, boolean deflect, int count) implements ParticleOptions,
	ICustomParticleData<SparkBurstParticleData> {

	private static final MapCodec<SparkBurstParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(ExtraCodecs.VECTOR3F.fieldOf("color")
			.forGetter(data -> data.color),
		Codec.BOOL.fieldOf("deflect")
			.forGetter(data -> data.deflect),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, SparkBurstParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, SparkBurstParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VECTOR3F, p -> p.color,
        ByteBufCodecs.BOOL, p -> p.deflect,
        ByteBufCodecs.VAR_INT, p -> p.count,
        SparkBurstParticleData::new
    );

	public SparkBurstParticleData(float r, float g, float b, boolean deflect, int count) { this(new Vector3f(r, g, b), deflect, count); }
	public SparkBurstParticleData() { this(0, 0, 0, false, 0); }

	@Override public MapCodec<SparkBurstParticleData> getCodec(ParticleType<SparkBurstParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, SparkBurstParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<SparkBurstParticleData> getFactory() {
		return new SparkBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPARK_BURST.get(); }

}
