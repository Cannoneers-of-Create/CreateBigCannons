package rbasamoyai.createbigcannons.effects.particles.smoke;

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

public record ShellExplosionSmokeParticleData(int lifetime, float scale) implements ParticleOptions, ICustomParticleDataWithSprite<ShellExplosionSmokeParticleData> {

	private static final Deserializer<ShellExplosionSmokeParticleData> DESERIALIZER = new Deserializer<>() {
		public ShellExplosionSmokeParticleData fromCommand(ParticleType<ShellExplosionSmokeParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			int lifetime = reader.readInt();
			reader.expect(' ');
			float scale = reader.readFloat();
			return new ShellExplosionSmokeParticleData(lifetime, scale);
		}

		public ShellExplosionSmokeParticleData fromNetwork(ParticleType<ShellExplosionSmokeParticleData> particleType, FriendlyByteBuf buffer) {
			return new ShellExplosionSmokeParticleData(buffer.readVarInt(), buffer.readFloat());
		}
	};
	private static final Codec<ShellExplosionSmokeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime),
		Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, ShellExplosionSmokeParticleData::new));

	public ShellExplosionSmokeParticleData() { this(60, 3); }

	@Override
	public Deserializer<ShellExplosionSmokeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<ShellExplosionSmokeParticleData> getCodec(ParticleType<ShellExplosionSmokeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<ShellExplosionSmokeParticleData> getMetaFactory() {
		return ShellExplosionSmokeParticle.Provider::new;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.SHELL_EXPLOSION_SMOKE.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeVarInt(this.lifetime)
			.writeFloat(this.scale);
	}

	@Override
	public String writeToString() {
		return String.format("%d %f", this.lifetime, this.scale);
	}

}
