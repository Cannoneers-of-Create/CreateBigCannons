package rbasamoyai.createbigcannons.effects.particles.smoke;

import org.joml.Vector3f;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record GasCloudParticleData(float scale, Vector3f color) implements ParticleOptions, ICustomParticleDataWithSprite<GasCloudParticleData> {

	public static final Codec<GasCloudParticleData> CODEC = RecordCodecBuilder.create(i -> i.group(
		Codec.FLOAT.fieldOf("scale").forGetter(GasCloudParticleData::scale),
		ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(GasCloudParticleData::color)
	).apply(i, GasCloudParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<GasCloudParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public GasCloudParticleData fromNetwork(ParticleType<GasCloudParticleData> type, FriendlyByteBuf buf) {
            return new GasCloudParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public GasCloudParticleData fromCommand(ParticleType<GasCloudParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float scale = reader.readFloat();
			reader.expect(' ');
			float r = reader.readFloat();
			reader.expect(' ');
			float g = reader.readFloat();
			reader.expect(' ');
			float b = reader.readFloat();
            return new GasCloudParticleData(scale, r, g, b);
        }
    };

	public GasCloudParticleData(float scale, float r, float g, float b) { this(scale, new Vector3f(r, g, b)); }
	public GasCloudParticleData() { this(0, new Vector3f()); }

	@Override public ParticleType<?> getType() { return CBCParticleTypes.GAS_CLOUD.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale)
			.writeFloat(this.color.x())
			.writeFloat(this.color.y())
			.writeFloat(this.color.z());
	}

	@Override
	public String writeToString() {
		return String.format("%f %f %f %f", this.scale, this.color.x(), this.color.y(), this.color.z());
	}

	@Override public Deserializer<GasCloudParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<GasCloudParticleData> getCodec(ParticleType<GasCloudParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public SpriteParticleRegistration<GasCloudParticleData> getMetaFactory() {
		return GasCloudParticle.Provider::new;
	}

}

