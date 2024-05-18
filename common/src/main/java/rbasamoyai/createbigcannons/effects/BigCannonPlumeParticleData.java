package rbasamoyai.createbigcannons.effects;

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

public class BigCannonPlumeParticleData implements ParticleOptions, ICustomParticleData<BigCannonPlumeParticleData> {

	public static final Codec<BigCannonPlumeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, BigCannonPlumeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<BigCannonPlumeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public BigCannonPlumeParticleData fromNetwork(ParticleType<BigCannonPlumeParticleData> type, FriendlyByteBuf buf) {
            return new BigCannonPlumeParticleData(buf.readFloat());
        }

        @Override
        public BigCannonPlumeParticleData fromCommand(ParticleType<BigCannonPlumeParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new BigCannonPlumeParticleData(reader.readFloat());
        }
    };

	private final float scale;

	public BigCannonPlumeParticleData(float scale) {
		this.scale = scale;
	}

	public BigCannonPlumeParticleData() {
		this(0);
	}

	public float scale() {
		return this.scale;
	}


	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.BIG_CANNON_PLUME.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale);
	}

	@Override
	public String writeToString() {
		return String.format("%f", this.scale);
	}

	@Override
	public Deserializer<BigCannonPlumeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<BigCannonPlumeParticleData> getCodec(ParticleType<BigCannonPlumeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<BigCannonPlumeParticleData> getFactory() {
		return new BigCannonPlumeParticle.Provider();
	}

}
