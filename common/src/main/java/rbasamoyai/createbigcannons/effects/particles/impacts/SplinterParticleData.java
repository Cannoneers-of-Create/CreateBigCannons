package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record SplinterParticleData(BlockState state) implements ParticleOptions, ICustomParticleDataWithSprite<SplinterParticleData> {

	private static final Deserializer<SplinterParticleData> DESERIALIZER = new Deserializer<>() {
        public SplinterParticleData fromCommand(ParticleType<SplinterParticleData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new SplinterParticleData(new BlockStateParser(reader, false).parse(false).getState());
        }

        public SplinterParticleData fromNetwork(ParticleType<SplinterParticleData> particleType, FriendlyByteBuf buffer) {
            return new SplinterParticleData(Block.stateById(buffer.readVarInt()));
        }
    };

	private static final Codec<SplinterParticleData> CODEC = BlockState.CODEC.xmap(SplinterParticleData::new, arg -> arg.state);

	public SplinterParticleData() { this(Blocks.AIR.defaultBlockState()); }

	@Override public Deserializer<SplinterParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<SplinterParticleData> getCodec(ParticleType<SplinterParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<SplinterParticleData> getMetaFactory() {
		return SplinterParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPLINTER.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeVarInt(Block.getId(this.state));
	}

	@Override
	public String writeToString() {
		return BlockStateParser.serialize(this.state);
	}

}
