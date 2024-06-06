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

public class ShrapnelSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<ShrapnelSmokeParticleData> {

	private static final Deserializer<ShrapnelSmokeParticleData> DESERIALIZER = new Deserializer<>() {
		public ShrapnelSmokeParticleData fromCommand(ParticleType<ShrapnelSmokeParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new ShrapnelSmokeParticleData(reader.readInt());
		}

		public ShrapnelSmokeParticleData fromNetwork(ParticleType<ShrapnelSmokeParticleData> particleType, FriendlyByteBuf buffer) {
			return new ShrapnelSmokeParticleData(buffer.readVarInt());
		}
	};
	private static final Codec<ShrapnelSmokeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime))
		.apply(i, ShrapnelSmokeParticleData::new));

	private final int lifetime;

	public ShrapnelSmokeParticleData() { this(60); }

	public ShrapnelSmokeParticleData(int lifetime) {
		this.lifetime = lifetime;
	}

	public int lifetime() {
		return this.lifetime;
	}

	@Override
	public Deserializer<ShrapnelSmokeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<ShrapnelSmokeParticleData> getCodec(ParticleType<ShrapnelSmokeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<ShrapnelSmokeParticleData> getMetaFactory() {
		return ShrapnelSmokeParticle.Provider::new;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.SHRAPNEL_SMOKE.get();
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
