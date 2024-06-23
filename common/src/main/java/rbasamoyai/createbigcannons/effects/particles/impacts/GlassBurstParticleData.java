package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record GlassBurstParticleData(BlockState blockState, int count) implements ParticleOptions,
	ICustomParticleData<GlassBurstParticleData> {

	private static final Deserializer<GlassBurstParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public GlassBurstParticleData fromCommand(ParticleType<GlassBurstParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			BlockState blockState = new BlockStateParser(reader, false).parse(false).getState();
			reader.expect(' ');
			int count = reader.readInt();
            return new GlassBurstParticleData(blockState, count);
        }

        @Override
        public GlassBurstParticleData fromNetwork(ParticleType<GlassBurstParticleData> particleType, FriendlyByteBuf buffer) {
            return new GlassBurstParticleData(Block.stateById(buffer.readVarInt()), buffer.readVarInt());
        }
    };

	private static final Codec<GlassBurstParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(BlockState.CODEC.fieldOf("blockState")
			.forGetter(data -> data.blockState),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, GlassBurstParticleData::new));

	public GlassBurstParticleData() { this(Blocks.AIR.defaultBlockState(), 0); }

	@Override public Deserializer<GlassBurstParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<GlassBurstParticleData> getCodec(ParticleType<GlassBurstParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<GlassBurstParticleData> getFactory() {
		return new GlassBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.GLASS_BURST.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeVarInt(Block.getId(this.blockState))
			.writeVarInt(this.count);
	}

	@Override
	public String writeToString() {
		return String.format("%s %d", BlockStateParser.serialize(this.blockState), this.count);
	}

}
