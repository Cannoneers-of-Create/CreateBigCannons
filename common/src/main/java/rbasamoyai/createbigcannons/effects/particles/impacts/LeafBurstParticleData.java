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
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record LeafBurstParticleData(BlockState blockState, int count) implements ParticleOptions,
	ICustomParticleData<LeafBurstParticleData> {

	private static final Deserializer<LeafBurstParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public LeafBurstParticleData fromCommand(ParticleType<LeafBurstParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			BlockState blockState = CBCUtils.parseBlockState(reader);
			reader.expect(' ');
			int count = reader.readInt();
            return new LeafBurstParticleData(blockState, count);
        }

        @Override
        public LeafBurstParticleData fromNetwork(ParticleType<LeafBurstParticleData> particleType, FriendlyByteBuf buffer) {
            return new LeafBurstParticleData(Block.stateById(buffer.readVarInt()), buffer.readVarInt());
        }
    };

	private static final Codec<LeafBurstParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(BlockState.CODEC.fieldOf("blockState")
			.forGetter(data -> data.blockState),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, LeafBurstParticleData::new));

	public LeafBurstParticleData() { this(Blocks.AIR.defaultBlockState(), 0); }

	@Override public Deserializer<LeafBurstParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<LeafBurstParticleData> getCodec(ParticleType<LeafBurstParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<LeafBurstParticleData> getFactory() {
		return new LeafBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.LEAF_BURST.get(); }

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
