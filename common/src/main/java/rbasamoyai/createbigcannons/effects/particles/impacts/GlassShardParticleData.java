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

public record GlassShardParticleData(BlockState state) implements ParticleOptions, ICustomParticleDataWithSprite<GlassShardParticleData> {

	private static final Deserializer<GlassShardParticleData> DESERIALIZER = new Deserializer<>() {
        public GlassShardParticleData fromCommand(ParticleType<GlassShardParticleData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new GlassShardParticleData(new BlockStateParser(reader, false).parse(false).getState());
        }

        public GlassShardParticleData fromNetwork(ParticleType<GlassShardParticleData> particleType, FriendlyByteBuf buffer) {
            return new GlassShardParticleData(Block.stateById(buffer.readVarInt()));
        }
    };

	private static final Codec<GlassShardParticleData> CODEC = BlockState.CODEC.xmap(GlassShardParticleData::new, arg -> arg.state);

	public GlassShardParticleData() { this(Blocks.AIR.defaultBlockState()); }

	@Override public Deserializer<GlassShardParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<GlassShardParticleData> getCodec(ParticleType<GlassShardParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<GlassShardParticleData> getMetaFactory() {
		return GlassShardParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.GLASS_SHARD.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeVarInt(Block.getId(this.state));
	}

	@Override
	public String writeToString() {
		return BlockStateParser.serialize(this.state);
	}

}
