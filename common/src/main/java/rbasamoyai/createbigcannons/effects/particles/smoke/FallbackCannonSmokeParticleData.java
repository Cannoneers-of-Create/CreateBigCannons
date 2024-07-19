package rbasamoyai.createbigcannons.effects.particles.smoke;

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
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class FallbackCannonSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<FallbackCannonSmokeParticleData> {

	public static final Codec<FallbackCannonSmokeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("power")
				.forGetter(data -> data.power),
			Codec.FLOAT.fieldOf("size")
				.forGetter(data -> data.size),
			Codec.INT.fieldOf("lifetime")
				.forGetter(data -> data.lifetime),
			Codec.FLOAT.fieldOf("friction")
				.forGetter(data -> data.friction))
		.apply(i, FallbackCannonSmokeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<FallbackCannonSmokeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public FallbackCannonSmokeParticleData fromNetwork(ParticleType<FallbackCannonSmokeParticleData> type, FriendlyByteBuf buf) {
            float power = buf.readFloat();
			float size = buf.readFloat();
            int lifetime = buf.readVarInt();
			float friction = buf.readFloat();
            return new FallbackCannonSmokeParticleData(power, size, lifetime, friction);
        }

        @Override
        public FallbackCannonSmokeParticleData fromCommand(ParticleType<FallbackCannonSmokeParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float power = reader.readFloat();
			reader.expect(' ');
			float size = reader.readFloat();
			reader.expect(' ');
            int lifetime = reader.readInt();
			reader.expect(' ');
			float friction = reader.readFloat();
            return new FallbackCannonSmokeParticleData(power, size, lifetime, friction);
        }
    };

	private final float power;
	private final float size;
	private final int lifetime;
	private final float friction;

	public FallbackCannonSmokeParticleData(float power, float size, int lifetime, float friction) {
		this.power = power;
		this.size = size;
		this.lifetime = lifetime;
        this.friction = friction;
    }

	public FallbackCannonSmokeParticleData() {
		this(0, 1, 1, 1);
	}

	public FallbackCannonSmokeParticleData(CannonSmokeParticleData source) {
		this(source.power(), source.size(), source.lifetime(), source.friction());
	}

	public float power() { return this.power; }
	public float size() { return this.size; }
	public int lifetime() { return this.lifetime; }
	public float friction() { return this.friction; }

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.CANNON_SMOKE_FALLBACK.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.power)
			.writeFloat(this.size);
		buf.writeVarInt(this.lifetime)
			.writeFloat(this.friction);
	}

	@Override
	public String writeToString() {
		return String.format("%f %f %d %f", this.power, this.size, this.lifetime, this.friction);
	}

	@Override
	public Deserializer<FallbackCannonSmokeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<FallbackCannonSmokeParticleData> getCodec(ParticleType<FallbackCannonSmokeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public SpriteParticleRegistration<FallbackCannonSmokeParticleData> getMetaFactory() {
		return FallbackCannonSmokeParticle.Provider::new;
	}

}
