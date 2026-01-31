package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

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

public class FluidBlobParticleData implements ParticleOptions, ICustomParticleData<FluidBlobParticleData> {

	public static final MapCodec<FluidBlobParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.FLOAT.fieldOf("scale").forGetter(FluidBlobParticleData::scale),
			EndFluidStack.CODEC.fieldOf("fluid").forGetter(FluidBlobParticleData::fluid))
		.apply(i, FluidBlobParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, FluidBlobParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, FluidBlobParticleData::scale,
        EndFluidStack.STREAM_CODEC, FluidBlobParticleData::fluid,
        FluidBlobParticleData::new);

	private final float scale;
	private final EndFluidStack fluid;

	public FluidBlobParticleData(float scale, EndFluidStack fluid) {
		this.scale = scale;
		this.fluid = fluid;
	}

	public FluidBlobParticleData() {
		this(0, EndFluidStack.EMPTY);
	}

	public float scale() {
		return this.scale;
	}

	public EndFluidStack fluid() {
		return this.fluid;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.FLUID_BLOB.get();
	}

	@Override
	public MapCodec<FluidBlobParticleData> getCodec(ParticleType<FluidBlobParticleData> type) {
		return CODEC;
	}

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FluidBlobParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<FluidBlobParticleData> getFactory() {
		return new FluidBlobParticle.Provider();
	}

}
