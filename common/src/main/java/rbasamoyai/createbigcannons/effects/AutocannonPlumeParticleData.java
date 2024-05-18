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

public class AutocannonPlumeParticleData implements ParticleOptions, ICustomParticleData<AutocannonPlumeParticleData> {

	public static final Codec<AutocannonPlumeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, AutocannonPlumeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<AutocannonPlumeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public AutocannonPlumeParticleData fromNetwork(ParticleType<AutocannonPlumeParticleData> type, FriendlyByteBuf buf) {
            return new AutocannonPlumeParticleData(buf.readFloat());
        }

        @Override
        public AutocannonPlumeParticleData fromCommand(ParticleType<AutocannonPlumeParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new AutocannonPlumeParticleData(reader.readFloat());
        }
    };

	private final float scale;

	public AutocannonPlumeParticleData(float scale) {
		this.scale = scale;
	}

	public AutocannonPlumeParticleData() {
		this(0);
	}

	public float scale() {
		return this.scale;
	}


	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.AUTOCANNON_PLUME.get();
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
	public Deserializer<AutocannonPlumeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<AutocannonPlumeParticleData> getCodec(ParticleType<AutocannonPlumeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<AutocannonPlumeParticleData> getFactory() {
		return new AutocannonPlumeParticle.Provider();
	}

}
