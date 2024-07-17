package rbasamoyai.createbigcannons.effects.particles.impacts;

import org.joml.Vector3f;

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
import net.minecraft.util.ExtraCodecs;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record SparkBurstParticleData(Vector3f color, boolean deflect, int count) implements ParticleOptions,
	ICustomParticleData<SparkBurstParticleData> {

	private static final Deserializer<SparkBurstParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public SparkBurstParticleData fromCommand(ParticleType<SparkBurstParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			float r = reader.readFloat();
			reader.expect(' ');
			float g = reader.readFloat();
			reader.expect(' ');
			float b = reader.readFloat();
			reader.expect(' ');
			boolean deflect = reader.readBoolean();
			reader.expect(' ');
			int count = reader.readInt();
            return new SparkBurstParticleData(r, g, b, deflect, count);
        }

        @Override
        public SparkBurstParticleData fromNetwork(ParticleType<SparkBurstParticleData> particleType, FriendlyByteBuf buffer) {
            return new SparkBurstParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), buffer.readVarInt());
        }
    };

	private static final Codec<SparkBurstParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(ExtraCodecs.VECTOR3F.fieldOf("color")
			.forGetter(data -> data.color),
		Codec.BOOL.fieldOf("deflect")
			.forGetter(data -> data.deflect),
		Codec.INT.fieldOf("count")
			.forGetter(data -> data.count))
		.apply(i, SparkBurstParticleData::new));

	public SparkBurstParticleData(float r, float g, float b, boolean deflect, int count) { this(new Vector3f(r, g, b), deflect, count); }
	public SparkBurstParticleData() { this(0, 0, 0, false, 0); }

	@Override public Deserializer<SparkBurstParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<SparkBurstParticleData> getCodec(ParticleType<SparkBurstParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<SparkBurstParticleData> getFactory() {
		return new SparkBurstParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPARK_BURST.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeFloat(this.color.x())
			.writeFloat(this.color.y())
			.writeFloat(this.color.z())
			.writeBoolean(this.deflect);
		buffer.writeVarInt(this.count);
	}

	@Override
	public String writeToString() {
		return String.format("%f %f %f %b %d", this.color.x(), this.color.y(), this.color.z(), this.deflect, this.count);
	}

}
