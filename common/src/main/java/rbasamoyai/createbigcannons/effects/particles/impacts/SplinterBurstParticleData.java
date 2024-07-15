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
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public record SplinterBurstParticleData(BlockState blockState, int count) implements ParticleOptions,
	ICustomParticleData<SplinterBurstParticleData> {

	private static final Deserializer<SplinterBurstParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public SplinterBurstParticleData fromCommand(ParticleType<SplinterBurstParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			BlockState blockState = BlockStateParser.parseForBlock(CBCRegistryUtils.getBlockRegistry(), reader, false).blockState();
			reader.expect(' ');
			int count = reader.readInt();
            return new SplinterBurstParticleData(blockState, count);
        }

        @Override
        public SplinterBurstParticleData fromNetwork(ParticleType<SplinterBurstParticleData> particleType, FriendlyByteBuf buffer) {
            return new SplinterBurstParticleData(Block.stateById(buffer.readVarInt()), buffer.readVarInt());
        }
    };

	private static final Codec<SplinterBurstParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(BlockState.CODEC.fieldOf("blockState")
			.forGetter(data -> data.blockState),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, SplinterBurstParticleData::new));

	public SplinterBurstParticleData() { this(Blocks.AIR.defaultBlockState(), 0); }

	@Override public Deserializer<SplinterBurstParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<SplinterBurstParticleData> getCodec(ParticleType<SplinterBurstParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<SplinterBurstParticleData> getFactory() {
		return new SplinterBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPLINTER_BURST.get(); }

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
