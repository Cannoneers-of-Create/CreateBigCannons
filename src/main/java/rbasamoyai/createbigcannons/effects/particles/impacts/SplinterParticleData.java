package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record SplinterParticleData(BlockState state) implements ParticleOptions, ICustomParticleDataWithSprite<SplinterParticleData> {

	private static final MapCodec<SplinterParticleData> CODEC = BlockState.CODEC.xmap(SplinterParticleData::new, arg -> arg.state).fieldOf("block_state");

    private static final StreamCodec<? super RegistryFriendlyByteBuf, SplinterParticleData> STREAM_CODEC =
        ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).map(SplinterParticleData::new, SplinterParticleData::state);

	public SplinterParticleData() { this(Blocks.AIR.defaultBlockState()); }

	@Override public MapCodec<SplinterParticleData> getCodec(ParticleType<SplinterParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, SplinterParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<SplinterParticleData> getMetaFactory() {
		return SplinterParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPLINTER.get(); }


}
