package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record LeafBurstParticleData(BlockState blockState, int count) implements ParticleOptions,
	ICustomParticleData<LeafBurstParticleData> {

	private static final MapCodec<LeafBurstParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(BlockState.CODEC.fieldOf("blockState")
			.forGetter(data -> data.blockState),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, LeafBurstParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, LeafBurstParticleData> STREAM_CODEC = null; //fixme!

	public LeafBurstParticleData() { this(Blocks.AIR.defaultBlockState(), 0); }

	@Override public MapCodec<LeafBurstParticleData> getCodec(ParticleType<LeafBurstParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, LeafBurstParticleData> getStreamCodec() { return STREAM_CODEC; }

    @Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<LeafBurstParticleData> getFactory() {
		return new LeafBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.LEAF_BURST.get(); }

}
