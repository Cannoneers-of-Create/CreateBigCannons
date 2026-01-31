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

public class SmokeShellSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<SmokeShellSmokeParticleData> {

	public static final MapCodec<SmokeShellSmokeParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, SmokeShellSmokeParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, SmokeShellSmokeParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, p -> p.scale,
        SmokeShellSmokeParticleData::new
    );

	private final float scale;

	public SmokeShellSmokeParticleData(float scale) {
		this.scale = scale;
	}

	public SmokeShellSmokeParticleData() {
		this(0);
	}

	public float scale() {
		return this.scale;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.SMOKE_SHELL_SMOKE.get();
	}

	@Override
	public MapCodec<SmokeShellSmokeParticleData> getCodec(ParticleType<SmokeShellSmokeParticleData> type) {
		return CODEC;
	}

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, SmokeShellSmokeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public SpriteParticleRegistration<SmokeShellSmokeParticleData> getMetaFactory() {
		return SmokeShellSmokeParticle.Provider::new;
	}

}

