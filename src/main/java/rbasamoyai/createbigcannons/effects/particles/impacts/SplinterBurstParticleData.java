package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record SplinterBurstParticleData(BlockState blockState, int count) implements ParticleOptions,
	ICustomParticleData<SplinterBurstParticleData> {

	private static final MapCodec<SplinterBurstParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(BlockState.CODEC.fieldOf("block_state")
			.forGetter(data -> data.blockState),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, SplinterBurstParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, SplinterBurstParticleData> STREAM_CODEC = StreamCodec.composite(
        CatnipStreamCodecs.BLOCK_STATE, SplinterBurstParticleData::blockState,
        ByteBufCodecs.VAR_INT, SplinterBurstParticleData::count,
        SplinterBurstParticleData::new);

	public SplinterBurstParticleData() { this(Blocks.AIR.defaultBlockState(), 0); }

	@Override public MapCodec<SplinterBurstParticleData> getCodec(ParticleType<SplinterBurstParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, SplinterBurstParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<SplinterBurstParticleData> getFactory() {
		return new SplinterBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPLINTER_BURST.get(); }

}
