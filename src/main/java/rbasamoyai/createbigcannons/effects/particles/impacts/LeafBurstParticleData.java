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

public record LeafBurstParticleData(BlockState blockState, int count) implements ParticleOptions,
	ICustomParticleData<LeafBurstParticleData> {

	private static final MapCodec<LeafBurstParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(BlockState.CODEC.fieldOf("block_state")
			.forGetter(data -> data.blockState),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, LeafBurstParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, LeafBurstParticleData> STREAM_CODEC = StreamCodec.composite(
        CatnipStreamCodecs.BLOCK_STATE, LeafBurstParticleData::blockState,
        ByteBufCodecs.VAR_INT, LeafBurstParticleData::count,
        LeafBurstParticleData::new);

	public LeafBurstParticleData() { this(Blocks.AIR.defaultBlockState(), 0); }

	@Override public MapCodec<LeafBurstParticleData> getCodec(ParticleType<LeafBurstParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, LeafBurstParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<LeafBurstParticleData> getFactory() {
		return new LeafBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.LEAF_BURST.get(); }

}
