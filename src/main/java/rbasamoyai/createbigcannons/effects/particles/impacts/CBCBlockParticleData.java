package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record CBCBlockParticleData(BlockState state) implements ParticleOptions, ICustomParticleData<CBCBlockParticleData> {

    private static final MapCodec<CBCBlockParticleData> CODEC = BlockState.CODEC.xmap(CBCBlockParticleData::new, CBCBlockParticleData::state).fieldOf("block_state");
    private static final StreamCodec<? super RegistryFriendlyByteBuf, CBCBlockParticleData> STREAM_CODEC =
        CatnipStreamCodecs.BLOCK_STATE.map(CBCBlockParticleData::new, CBCBlockParticleData::state);

	public CBCBlockParticleData() { this(Blocks.AIR.defaultBlockState()); }

	@Override public MapCodec<CBCBlockParticleData> getCodec(ParticleType<CBCBlockParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, CBCBlockParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<CBCBlockParticleData> getFactory() {
		return new CBCBlockParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.BLOCK.get(); }
}
