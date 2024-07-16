package rbasamoyai.createbigcannons.effects.particles.splashes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record ProjectileSplashParticleData(float r, float g, float b, float light) implements ParticleOptions, ICustomParticleData<ProjectileSplashParticleData> {

	private static final Deserializer<ProjectileSplashParticleData> DESERIALIZER = new Deserializer<>() {
		@Override
		public ProjectileSplashParticleData fromCommand(ParticleType<ProjectileSplashParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			float r = reader.readFloat();
			reader.expect(' ');
			float g = reader.readFloat();
			reader.expect(' ');
			float b = reader.readFloat();
			reader.expect(' ');
			float light = reader.readFloat();
			return new ProjectileSplashParticleData(r, g, b, light);
		}

		@Override
		public ProjectileSplashParticleData fromNetwork(ParticleType<ProjectileSplashParticleData> particleType, FriendlyByteBuf buffer) {
			return new ProjectileSplashParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
		}
	};

	private static final Codec<ProjectileSplashParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("r")
			.forGetter(data -> data.r),
			Codec.FLOAT.fieldOf("g")
				.forGetter(data -> data.g),
			Codec.FLOAT.fieldOf("b")
				.forGetter(data -> data.b),
			Codec.FLOAT.fieldOf("light")
				.forGetter(data -> data.light))
		.apply(i, ProjectileSplashParticleData::new));

	public ProjectileSplashParticleData() { this(1, 1, 1, 1); }

	@Override public Deserializer<ProjectileSplashParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<ProjectileSplashParticleData> getCodec(ParticleType<ProjectileSplashParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<ProjectileSplashParticleData> getFactory() {
		return new ProjectileSplashParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.PROJECTILE_SPLASH.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeFloat(this.r)
			.writeFloat(this.g)
			.writeFloat(this.b)
			.writeFloat(this.light);
	}

	@Override
	public String writeToString() {
		return String.format("%f %f %f %f", this.r, this.g, this.b, this.light);
	}

}
