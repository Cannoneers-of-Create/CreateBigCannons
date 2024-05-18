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

public class CannonPlumeParticleData implements ParticleOptions, ICustomParticleData<CannonPlumeParticleData> {

	public static final Codec<CannonPlumeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, CannonPlumeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<CannonPlumeParticleData> DESERIALIZER = new Deserializer<>() {
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

	public CannonPlumeParticleData() {
		this(0);
	}

	public float scale() {
		return this.scale;
	}


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

	@Override
	public Deserializer<CannonPlumeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<CannonPlumeParticleData> getCodec(ParticleType<CannonPlumeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<CannonPlumeParticleData> getFactory() {
		return new CannonPlumeParticle.Provider();
	}

}
