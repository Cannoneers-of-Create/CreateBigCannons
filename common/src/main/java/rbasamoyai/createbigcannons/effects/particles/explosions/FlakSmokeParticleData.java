package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record FlakSmokeParticleData(int lifetime) implements ParticleOptions, ICustomParticleDataWithSprite<FlakSmokeParticleData> {

	private static final Deserializer<FlakSmokeParticleData> DESERIALIZER = new Deserializer<>() {
		public FlakSmokeParticleData fromCommand(ParticleType<FlakSmokeParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new FlakSmokeParticleData(reader.readInt());
		}

		public FlakSmokeParticleData fromNetwork(ParticleType<FlakSmokeParticleData> particleType, FriendlyByteBuf buffer) {
			return new FlakSmokeParticleData(buffer.readVarInt());
		}
	};
	private static final Codec<FlakSmokeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime))
		.apply(i, FlakSmokeParticleData::new));

	public FlakSmokeParticleData() { this(60); }

	@Override
	public Deserializer<FlakSmokeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<FlakSmokeParticleData> getCodec(ParticleType<FlakSmokeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<FlakSmokeParticleData> getMetaFactory() {
		return FlakSmokeParticle.Provider::new;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.FLAK_SMOKE.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeVarInt(this.lifetime);
	}

	@Override
	public String writeToString() {
		return String.format("%d", this.lifetime);
	}

}
