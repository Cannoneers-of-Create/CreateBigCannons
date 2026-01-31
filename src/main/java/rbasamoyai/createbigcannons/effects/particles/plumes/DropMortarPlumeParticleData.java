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

public class DropMortarPlumeParticleData implements ParticleOptions, ICustomParticleData<DropMortarPlumeParticleData> {

	public static final MapCodec<DropMortarPlumeParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, DropMortarPlumeParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, DropMortarPlumeParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, p -> p.scale,
        DropMortarPlumeParticleData::new
    );

	private final float scale;

	public DropMortarPlumeParticleData(float scale) {
		this.scale = scale;
	}

	public DropMortarPlumeParticleData() {
		this(0);
	}

	public float scale() {
		return this.scale;
	}


	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.DROP_MORTAR_PLUME.get();
	}

	@Override
	public MapCodec<DropMortarPlumeParticleData> getCodec(ParticleType<DropMortarPlumeParticleData> type) {
		return CODEC;
	}

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, DropMortarPlumeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<DropMortarPlumeParticleData> getFactory() {
		return new DropMortarPlumeParticle.Provider();
	}

}
