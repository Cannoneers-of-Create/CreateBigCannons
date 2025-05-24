package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record GlassShardParticleData(BlockState state) implements ParticleOptions, ICustomParticleDataWithSprite<GlassShardParticleData> {

	private static final MapCodec<GlassShardParticleData> CODEC = BlockState.CODEC.xmap(GlassShardParticleData::new, GlassShardParticleData::state).fieldOf("block_state");

    private static final StreamCodec<? super RegistryFriendlyByteBuf, GlassShardParticleData> STREAM_CODEC =
        ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).map(GlassShardParticleData::new, GlassShardParticleData::state);

	public GlassShardParticleData() { this(Blocks.AIR.defaultBlockState()); }

	@Override public MapCodec<GlassShardParticleData> getCodec(ParticleType<GlassShardParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, GlassShardParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<GlassShardParticleData> getMetaFactory() {
		return GlassShardParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.GLASS_SHARD.get(); }


}
