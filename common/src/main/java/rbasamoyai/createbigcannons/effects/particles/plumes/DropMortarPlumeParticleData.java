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

public class DropMortarPlumeParticleData implements ParticleOptions, ICustomParticleData<DropMortarPlumeParticleData> {

	public static final Codec<DropMortarPlumeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, DropMortarPlumeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<DropMortarPlumeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public DropMortarPlumeParticleData fromNetwork(ParticleType<DropMortarPlumeParticleData> type, FriendlyByteBuf buf) {
            return new DropMortarPlumeParticleData(buf.readFloat());
        }

        @Override
        public DropMortarPlumeParticleData fromCommand(ParticleType<DropMortarPlumeParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new DropMortarPlumeParticleData(reader.readFloat());
        }
    };

	private final float scale;

	public DropMortarPlumeParticleData(float scale) {
		this.scale = scale;
	}

	public DropMortarPlumeParticleData() {
		this(0);
	}

	public float scale() {
		return this.scale;
	}


	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.DROP_MORTAR_PLUME.get();
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
	public Deserializer<DropMortarPlumeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<DropMortarPlumeParticleData> getCodec(ParticleType<DropMortarPlumeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<DropMortarPlumeParticleData> getFactory() {
		return new DropMortarPlumeParticle.Provider();
	}

}
