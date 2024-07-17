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

public class TrailSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<TrailSmokeParticleData> {

	private static final ParticleOptions.Deserializer<TrailSmokeParticleData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
		public TrailSmokeParticleData fromCommand(ParticleType<TrailSmokeParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new TrailSmokeParticleData(reader.readInt());
		}

		public TrailSmokeParticleData fromNetwork(ParticleType<TrailSmokeParticleData> particleType, FriendlyByteBuf buffer) {
			return new TrailSmokeParticleData(buffer.readVarInt());
		}
	};
	private static final Codec<TrailSmokeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime))
		.apply(i, TrailSmokeParticleData::new));

	private final int lifetime;

	public TrailSmokeParticleData() { this(60); }

	public TrailSmokeParticleData(int lifetime) {
		this.lifetime = lifetime;
	}

	public int lifetime() {
		return this.lifetime;
	}

	@Override
	public Deserializer<TrailSmokeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<TrailSmokeParticleData> getCodec(ParticleType<TrailSmokeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<TrailSmokeParticleData> getMetaFactory() {
		return TrailSmokeParticle.Provider::new;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.TRAIL_SMOKE.get();
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
