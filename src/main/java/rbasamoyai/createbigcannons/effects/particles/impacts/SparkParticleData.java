package rbasamoyai.createbigcannons.effects.particles.impacts;

import org.joml.Vector3f;

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

public record SparkParticleData(Vector3f color) implements ParticleOptions, ICustomParticleData<SparkParticleData> {

	private static final MapCodec<SparkParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        ExtraCodecs.VECTOR3F.fieldOf("scale").forGetter(p -> p.color)).apply(i, SparkParticleData::new)
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, SparkParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VECTOR3F, p -> p.color,
        SparkParticleData::new
    );

	public SparkParticleData(float r, float g, float b) { this(new Vector3f(r, g, b)); }
	public SparkParticleData() { this(0, 0, 0); }

	@Override public MapCodec<SparkParticleData> getCodec(ParticleType<SparkParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, SparkParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<SparkParticleData> getFactory() {
		return new SparkParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPARK.get(); }

}
