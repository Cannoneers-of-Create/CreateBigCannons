package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record SparkParticleData(Vector3f color) implements ParticleOptions, ICustomParticleData<SparkParticleData> {

	private static final Deserializer<SparkParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public SparkParticleData fromCommand(ParticleType<SparkParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			float r = reader.readFloat();
			reader.expect(' ');
			float g = reader.readFloat();
			reader.expect(' ');
			float b = reader.readFloat();
            return new SparkParticleData(r, g, b);
        }

        @Override
        public SparkParticleData fromNetwork(ParticleType<SparkParticleData> particleType, FriendlyByteBuf buffer) {
            return new SparkParticleData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };

	private static final Codec<SparkParticleData> CODEC = Vector3f.CODEC.xmap(SparkParticleData::new, data -> data.color);

	public SparkParticleData(float r, float g, float b) { this(new Vector3f(r, g, b)); }
	public SparkParticleData() { this(0, 0, 0); }

	@Override public Deserializer<SparkParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<SparkParticleData> getCodec(ParticleType<SparkParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<SparkParticleData> getFactory() {
		return new SparkParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SPARK.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeFloat(this.color.x())
			.writeFloat(this.color.y())
			.writeFloat(this.color.z());
	}

	@Override
	public String writeToString() {
		return String.format("%f %f %f", this.color.x(), this.color.y(), this.color.z());
	}

}
