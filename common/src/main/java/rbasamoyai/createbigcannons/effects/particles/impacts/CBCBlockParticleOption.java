package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
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

public record CBCBlockParticleOption(BlockState state) implements ParticleOptions, ICustomParticleData<CBCBlockParticleOption> {

	private static final ParticleOptions.Deserializer<CBCBlockParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public CBCBlockParticleOption fromCommand(ParticleType<CBCBlockParticleOption> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new CBCBlockParticleOption(new BlockStateParser(reader, false).parse(false).getState());
        }

        public CBCBlockParticleOption fromNetwork(ParticleType<CBCBlockParticleOption> particleType, FriendlyByteBuf buffer) {
            return new CBCBlockParticleOption(Block.stateById(buffer.readVarInt()));
        }
    };

	private static final Codec<CBCBlockParticleOption> CODEC = BlockState.CODEC.xmap(CBCBlockParticleOption::new, arg -> arg.state);

	public CBCBlockParticleOption() { this(Blocks.AIR.defaultBlockState()); }

	@Override public Deserializer<CBCBlockParticleOption> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<CBCBlockParticleOption> getCodec(ParticleType<CBCBlockParticleOption> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<CBCBlockParticleOption> getFactory() {
		return new CBCBlockParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.BLOCK.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeVarInt(Block.getId(this.state));
	}

	@Override
	public String writeToString() {
		return BlockStateParser.serialize(this.state);
	}

}
