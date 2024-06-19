package rbasamoyai.createbigcannons.effects.particles.splashes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record SplashSprayParticleData(float r, float g, float b, float size, float light, int lifetime) implements ParticleOptions,
	ICustomParticleDataWithSprite<SplashSprayParticleData> {

	private static final Deserializer<SplashSprayParticleData> DESERIALIZER = new Deserializer<>() {
		@Override
		public SplashSprayParticleData fromCommand(ParticleType<SplashSprayParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			float r = reader.readFloat();
			reader.expect(' ');
			float g = reader.readFloat();
			reader.expect(' ');
			float b = reader.readFloat();
			reader.expect(' ');
			float size = reader.readFloat();
			reader.expect(' ');
			float light = reader.readFloat();
			reader.expect(' ');
			int lifetime = reader.readInt();
			return new SplashSprayParticleData(r, g, b, size, light, lifetime);
		}

		@Override
		public SplashSprayParticleData fromNetwork(ParticleType<SplashSprayParticleData> particleType, FriendlyByteBuf buffer) {
			return new SplashSprayParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
				buffer.readFloat(), buffer.readVarInt());
		}
	};

	private static final Codec<SplashSprayParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("r")
			.forGetter(data -> data.r),
		Codec.FLOAT.fieldOf("g")
			.forGetter(data -> data.g),
		Codec.FLOAT.fieldOf("b")
			.forGetter(data -> data.b),
		Codec.FLOAT.fieldOf("size")
			.forGetter(data -> data.size),
		Codec.FLOAT.fieldOf("light")
			.forGetter(data -> data.light),
		Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime))
		.apply(i, SplashSprayParticleData::new));

	public SplashSprayParticleData() { this(1, 1, 1, 1, 1, 1); }

	@Override public Deserializer<SplashSprayParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<SplashSprayParticleData> getCodec(ParticleType<SplashSprayParticleData> type) { return CODEC; }

	@Override
	public ParticleEngine.SpriteParticleRegistration<SplashSprayParticleData> getMetaFactory() {
		return SplashSprayParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPLASH_SPRAY.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeFloat(this.r)
			.writeFloat(this.g)
			.writeFloat(this.b)
			.writeFloat(this.size)
			.writeFloat(this.light);
		buffer.writeVarInt(this.lifetime);
	}

	@Override
	public String writeToString() {
		return String.format("%f %f %f %f %f %d", this.r, this.g, this.b, this.size, this.light, this.lifetime);
	}

}
