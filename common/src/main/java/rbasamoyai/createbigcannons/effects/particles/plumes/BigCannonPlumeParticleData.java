package rbasamoyai.createbigcannons.effects.particles.plumes;

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
		.group(Codec.FLOAT.fieldOf("size")
			.forGetter(data -> data.size),
		Codec.FLOAT.fieldOf("power")
			.forGetter(data -> data.power),
		Codec.INT.fieldOf("lifetime")
			.forGetter(data -> data.lifetime))
		.apply(i, BigCannonPlumeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<BigCannonPlumeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public BigCannonPlumeParticleData fromNetwork(ParticleType<BigCannonPlumeParticleData> type, FriendlyByteBuf buf) {
            return new BigCannonPlumeParticleData(buf.readFloat(), buf.readFloat(), buf.readVarInt());
        }

        @Override
        public BigCannonPlumeParticleData fromCommand(ParticleType<BigCannonPlumeParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
			float size = reader.readFloat();
            reader.expect(' ');
			float power = reader.readFloat();
			reader.expect(' ');
			int lifetime = reader.readInt();
            return new BigCannonPlumeParticleData(size, power, lifetime);
        }
    };

	private final float size;
	private final float power;
	private final int lifetime;

	public BigCannonPlumeParticleData(float size, float power, int lifetime) {
		this.size = size;
		this.power = power;
		this.lifetime = lifetime;
	}

	public BigCannonPlumeParticleData(float size) { this(size, size, 10); }

	public BigCannonPlumeParticleData() { this(0, 0, 1); }

	public float size() { return this.size; }
	public float power() { return this.power; }
	public float lifetime() { return this.lifetime; }

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.BIG_CANNON_PLUME.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.size)
			.writeFloat(this.power);
		buf.writeVarInt(this.lifetime);
	}

	@Override
	public String writeToString() {
		return String.format("%f %f %d", this.size, this.power, this.lifetime);
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
