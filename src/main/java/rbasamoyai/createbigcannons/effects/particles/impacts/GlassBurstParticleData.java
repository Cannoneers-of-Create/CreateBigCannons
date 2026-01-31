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

public record GlassBurstParticleData(BlockState blockState, int count) implements ParticleOptions,
	ICustomParticleData<GlassBurstParticleData> {

	private static final MapCodec<GlassBurstParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(BlockState.CODEC.fieldOf("block_state")
			.forGetter(data -> data.blockState),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, GlassBurstParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, GlassBurstParticleData> STREAM_CODEC = StreamCodec.composite(
        CatnipStreamCodecs.BLOCK_STATE, GlassBurstParticleData::blockState,
        ByteBufCodecs.VAR_INT, GlassBurstParticleData::count,
        GlassBurstParticleData::new);

	public GlassBurstParticleData() { this(Blocks.AIR.defaultBlockState(), 0); }

	@Override public MapCodec<GlassBurstParticleData> getCodec(ParticleType<GlassBurstParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, GlassBurstParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<GlassBurstParticleData> getFactory() {
		return new GlassBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.GLASS_BURST.get(); }

}
