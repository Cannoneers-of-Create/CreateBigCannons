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
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record GlassBurstParticleData(BlockState blockState, int count) implements ParticleOptions,
	ICustomParticleData<GlassBurstParticleData> {

	private static final MapCodec<GlassBurstParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(BlockState.CODEC.fieldOf("blockState")
			.forGetter(data -> data.blockState),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, GlassBurstParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, GlassBurstParticleData> STREAM_CODEC = null; //fixme!

	public GlassBurstParticleData() { this(Blocks.AIR.defaultBlockState(), 0); }

	@Override public MapCodec<GlassBurstParticleData> getCodec(ParticleType<GlassBurstParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, GlassBurstParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<GlassBurstParticleData> getFactory() {
		return new GlassBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.GLASS_BURST.get(); }

}
