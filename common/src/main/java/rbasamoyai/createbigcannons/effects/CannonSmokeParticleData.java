package rbasamoyai.createbigcannons.effects;

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
		.group(Codec.FLOAT.fieldOf("scale")
				.forGetter(data -> data.scale),
			Codec.INT.fieldOf("shiftTime")
				.forGetter(data -> data.shiftTime),
			Codec.INT.fieldOf("startShiftTime")
				.forGetter(data -> data.startShiftTime),
			Codec.INT.fieldOf("lifetime")
				.forGetter(data -> data.lifetime),
			Codec.FLOAT.fieldOf("friction")
				.forGetter(data -> data.friction))
		.apply(i, CannonSmokeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<CannonSmokeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public CannonSmokeParticleData fromNetwork(ParticleType<CannonSmokeParticleData> type, FriendlyByteBuf buf) {
            float scale = buf.readFloat();
            int shiftTime = buf.readVarInt();
			int startShiftTime = buf.readVarInt();
            int lifetime = buf.readVarInt();
			float friction = buf.readFloat();
            return new CannonSmokeParticleData(scale, shiftTime, startShiftTime, lifetime, friction);
        }

        @Override
        public CannonSmokeParticleData fromCommand(ParticleType<CannonSmokeParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float scale = reader.readFloat();
            reader.expect(' ');
            int shiftTime = reader.readInt();
			reader.expect(' ');
			int startShiftTime = reader.readInt();
			reader.expect(' ');
            int lifetime = reader.readInt();
			reader.expect(' ');
			float friction = reader.readFloat();
            return new CannonSmokeParticleData(scale, shiftTime, startShiftTime, lifetime, friction);
        }
    };

	private final float scale;
	private final int shiftTime;
	private final int startShiftTime;
	private final int lifetime;
	private final float friction;

	public CannonSmokeParticleData(float scale, int shiftTime, int startShiftTime, int lifetime, float friction) {
		this.scale = scale;
		this.shiftTime = shiftTime;
		this.startShiftTime = startShiftTime;
		this.lifetime = lifetime;
        this.friction = friction;
    }

	public CannonSmokeParticleData() {
		this(0, 1, 1, 0, 1);
	}

	public float scale() { return this.scale; }
	public int shiftTime() { return this.shiftTime; }
	public int startShiftTime() { return this.startShiftTime; }
	public int lifetime() { return this.lifetime; }
	public float friction() { return this.friction; }

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.CANNON_SMOKE.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale);
		buf.writeVarInt(this.shiftTime)
			.writeVarInt(this.startShiftTime)
			.writeVarInt(this.lifetime)
			.writeFloat(this.friction);
	}

	@Override
	public String writeToString() {
		return String.format("%f %d %d %d %f", this.scale, this.shiftTime, this.startShiftTime, this.lifetime, this.friction);
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
