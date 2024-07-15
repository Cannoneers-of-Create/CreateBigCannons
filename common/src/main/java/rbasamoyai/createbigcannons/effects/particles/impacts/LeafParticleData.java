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
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record LeafParticleData(BlockState state) implements ParticleOptions, ICustomParticleDataWithSprite<LeafParticleData> {

	private static final Deserializer<LeafParticleData> DESERIALIZER = new Deserializer<>() {
        public LeafParticleData fromCommand(ParticleType<LeafParticleData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new LeafParticleData(CBCUtils.parseBlockState(reader));
        }

        public LeafParticleData fromNetwork(ParticleType<LeafParticleData> particleType, FriendlyByteBuf buffer) {
            return new LeafParticleData(Block.stateById(buffer.readVarInt()));
        }
    };

	private static final Codec<LeafParticleData> CODEC = BlockState.CODEC.xmap(LeafParticleData::new, arg -> arg.state);

	public LeafParticleData() { this(Blocks.AIR.defaultBlockState()); }

	@Override public Deserializer<LeafParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<LeafParticleData> getCodec(ParticleType<LeafParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<LeafParticleData> getMetaFactory() {
		return LeafParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.LEAF.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeVarInt(Block.getId(this.state));
	}

	@Override
	public String writeToString() {
		return BlockStateParser.serialize(this.state);
	}

}
