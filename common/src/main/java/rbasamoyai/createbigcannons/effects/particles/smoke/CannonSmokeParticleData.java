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

public class CannonSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<CannonSmokeParticleData> {

	public static final Codec<CannonSmokeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("power")
				.forGetter(data -> data.power),
			Codec.FLOAT.fieldOf("size")
				.forGetter(data -> data.size),
			Codec.INT.fieldOf("lifetime")
				.forGetter(data -> data.lifetime),
			Codec.FLOAT.fieldOf("friction")
				.forGetter(data -> data.friction))
		.apply(i, CannonSmokeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<CannonSmokeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public CannonSmokeParticleData fromNetwork(ParticleType<CannonSmokeParticleData> type, FriendlyByteBuf buf) {
            float power = buf.readFloat();
			float size = buf.readFloat();
            int lifetime = buf.readVarInt();
			float friction = buf.readFloat();
            return new CannonSmokeParticleData(power, size, lifetime, friction);
        }

        @Override
        public CannonSmokeParticleData fromCommand(ParticleType<CannonSmokeParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float power = reader.readFloat();
			reader.expect(' ');
			float size = reader.readFloat();
			reader.expect(' ');
            int lifetime = reader.readInt();
			reader.expect(' ');
			float friction = reader.readFloat();
            return new CannonSmokeParticleData(power, size, lifetime, friction);
        }
    };

	private final float power;
	private final float size;
	private final int lifetime;
	private final float friction;

	public CannonSmokeParticleData(float power, float size, int lifetime, float friction) {
		this.power = power;
		this.size = size;
		this.lifetime = lifetime;
        this.friction = friction;
    }

	public CannonSmokeParticleData() {
		this(0, 1, 1, 1);
	}

	public float power() { return this.power; }
	public float size() { return this.size; }
	public int lifetime() { return this.lifetime; }
	public float friction() { return this.friction; }

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.CANNON_SMOKE.get();
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
	public Deserializer<CannonSmokeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<CannonSmokeParticleData> getCodec(ParticleType<CannonSmokeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public SpriteParticleRegistration<CannonSmokeParticleData> getMetaFactory() {
		return CannonSmokeParticle.Provider::new;
	}

}
