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
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record CBCBlockParticleData(BlockState state) implements ParticleOptions, ICustomParticleData<CBCBlockParticleData> {

	private static final ParticleOptions.Deserializer<CBCBlockParticleData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public CBCBlockParticleData fromCommand(ParticleType<CBCBlockParticleData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new CBCBlockParticleData(CBCUtils.parseBlockState(reader));
        }

        public CBCBlockParticleData fromNetwork(ParticleType<CBCBlockParticleData> particleType, FriendlyByteBuf buffer) {
            return new CBCBlockParticleData(Block.stateById(buffer.readVarInt()));
        }
    };

	private static final Codec<CBCBlockParticleData> CODEC = BlockState.CODEC.xmap(CBCBlockParticleData::new, arg -> arg.state);

	public CBCBlockParticleData() { this(Blocks.AIR.defaultBlockState()); }

	@Override public Deserializer<CBCBlockParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<CBCBlockParticleData> getCodec(ParticleType<CBCBlockParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<CBCBlockParticleData> getFactory() {
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
