package rbasamoyai.createbigcannons.effects.particles.smoke;

import org.joml.Vector3f;

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
import net.minecraft.util.ExtraCodecs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record GasCloudParticleData(float scale, Vector3f color) implements ParticleOptions, ICustomParticleDataWithSprite<GasCloudParticleData> {

	public static final MapCodec<GasCloudParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		Codec.FLOAT.fieldOf("scale").forGetter(GasCloudParticleData::scale),
		ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(GasCloudParticleData::color)
	).apply(i, GasCloudParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, GasCloudParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, p -> p.scale,
        ByteBufCodecs.VECTOR3F, p -> p.color,
        GasCloudParticleData::new
    );

	public GasCloudParticleData(float scale, float r, float g, float b) { this(scale, new Vector3f(r, g, b)); }
	public GasCloudParticleData() { this(0, new Vector3f()); }

	@Override public ParticleType<?> getType() { return CBCParticleTypes.GAS_CLOUD.get(); }

	@Override public MapCodec<GasCloudParticleData> getCodec(ParticleType<GasCloudParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, GasCloudParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public SpriteParticleRegistration<GasCloudParticleData> getMetaFactory() {
		return GasCloudParticle.Provider::new;
	}

}

