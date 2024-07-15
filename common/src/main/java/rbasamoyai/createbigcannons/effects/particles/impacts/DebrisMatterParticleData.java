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

public record DebrisMatterParticleData(boolean deflect, boolean forceDisplay, BlockState blockState) implements ParticleOptions,
	ICustomParticleData<DebrisMatterParticleData> {

	private static final Deserializer<DebrisMatterParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public DebrisMatterParticleData fromCommand(ParticleType<DebrisMatterParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			boolean deflect = reader.readBoolean();
			reader.expect(' ');
			boolean forceDisplay = reader.readBoolean();
			reader.expect(' ');
            return new DebrisMatterParticleData(deflect, forceDisplay, new BlockStateParser(reader, false).parse(false).getState());
        }

        @Override
        public DebrisMatterParticleData fromNetwork(ParticleType<DebrisMatterParticleData> particleType, FriendlyByteBuf buffer) {
            return new DebrisMatterParticleData(buffer.readBoolean(), buffer.readBoolean(), Block.stateById(buffer.readVarInt()));
        }
    };

	private static final Codec<DebrisMatterParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.BOOL.fieldOf("deflect")
			.forGetter(data -> data.deflect),
		Codec.BOOL.fieldOf("forceDisplay")
			.forGetter(data -> data.forceDisplay),
		BlockState.CODEC.fieldOf("blockState")
			.forGetter(data -> data.blockState))
		.apply(i, DebrisMatterParticleData::new));

	public DebrisMatterParticleData() { this(false, false, Blocks.AIR.defaultBlockState()); }

	@Override public Deserializer<DebrisMatterParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<DebrisMatterParticleData> getCodec(ParticleType<DebrisMatterParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<DebrisMatterParticleData> getFactory() {
		return new DebrisMatterParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.DEBRIS_MATTER.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeBoolean(this.deflect)
			.writeBoolean(this.forceDisplay);
		buffer.writeVarInt(Block.getId(this.blockState));
	}

	@Override
	public String writeToString() {
		return String.format("%b %b %s", this.deflect, this.forceDisplay, BlockStateParser.serialize(this.blockState));
	}

}
