package rbasamoyai.createbigcannons.cannon_control.effects;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.contraptions.particle.ICustomParticleDataWithSprite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class CannonPlumeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<CannonPlumeParticleData> {

	public static final Codec<CannonPlumeParticleData> CODEC = RecordCodecBuilder.create(i -> i
			.group(Codec.FLOAT.fieldOf("scale")
					.forGetter(data -> data.scale))
			.apply(i, CannonPlumeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<CannonPlumeParticleData> DESERIALIZER = new Deserializer<CannonPlumeParticleData>() {
		@Override
		public CannonPlumeParticleData fromNetwork(ParticleType<CannonPlumeParticleData> type, FriendlyByteBuf buf) {
			return new CannonPlumeParticleData(buf.readFloat());
		}

		@Override
		public CannonPlumeParticleData fromCommand(ParticleType<CannonPlumeParticleData> type, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new CannonPlumeParticleData(reader.readFloat());
		}
	};

	private final float scale;

	public CannonPlumeParticleData(float scale) {
		this.scale = scale;
	}

	public CannonPlumeParticleData() { this(0); };


	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.CANNON_PLUME.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale);
	}

	@Override
	public String writeToString() {
		return String.format("%d", this.scale);
	}

	@Override public Deserializer<CannonPlumeParticleData> getDeserializer() { return DESERIALIZER; }

	@Override public Codec<CannonPlumeParticleData> getCodec(ParticleType<CannonPlumeParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<CannonPlumeParticleData> getMetaFactory() {
		return CannonPlumeParticle.Provider::new;
	}

}
