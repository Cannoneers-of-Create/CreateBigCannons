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

public record LeafParticleData(BlockState state) implements ParticleOptions, ICustomParticleDataWithSprite<LeafParticleData> {

	private static final MapCodec<LeafParticleData> CODEC = BlockState.CODEC.xmap(LeafParticleData::new, arg -> arg.state).fieldOf("block_state");

    private static final StreamCodec<? super RegistryFriendlyByteBuf, LeafParticleData> STREAM_CODEC =
        ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).map(LeafParticleData::new, LeafParticleData::state);

	public LeafParticleData() { this(Blocks.AIR.defaultBlockState()); }

	@Override public MapCodec<LeafParticleData> getCodec(ParticleType<LeafParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, LeafParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<LeafParticleData> getMetaFactory() {
		return LeafParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.LEAF.get(); }


}
