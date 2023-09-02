package rbasamoyai.createbigcannons.cannon_control.effects;

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
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class CannonSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<CannonSmokeParticleData> {

	public static final Codec<CannonSmokeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
				.forGetter(data -> data.scale),
			Vec3.CODEC.fieldOf("startColor")
				.forGetter(data -> data.startColor),
			Vec3.CODEC.fieldOf("endColor")
				.forGetter(data -> data.endColor),
			Codec.INT.fieldOf("shiftTime")
				.forGetter(data -> data.shiftTime))
		.apply(i, CannonSmokeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<CannonSmokeParticleData> DESERIALIZER = new Deserializer<CannonSmokeParticleData>() {
		@Override
		public CannonSmokeParticleData fromNetwork(ParticleType<CannonSmokeParticleData> type, FriendlyByteBuf buf) {
			float scale = buf.readFloat();
			Vec3 startColor = new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
			Vec3 endColor = new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
			int shiftTime = buf.readVarInt();
			return new CannonSmokeParticleData(scale, startColor, endColor, shiftTime);
		}

		@Override
		public CannonSmokeParticleData fromCommand(ParticleType<CannonSmokeParticleData> type, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			float scale = reader.readFloat();
			reader.expect(' ');
			float rs = reader.readFloat();
			reader.expect(' ');
			float gs = reader.readFloat();
			reader.expect(' ');
			float bs = reader.readFloat();
			reader.expect(' ');
			float rf = reader.readFloat();
			reader.expect(' ');
			float gf = reader.readFloat();
			reader.expect(' ');
			float bf = reader.readFloat();
			reader.expect(' ');
			int shiftTime = reader.readInt();
			return new CannonSmokeParticleData(scale, new Vec3(rs, gs, bs), new Vec3(rf, gf, bf), shiftTime);
		}
	};

	private final float scale;
	private final Vec3 startColor;
	private final Vec3 endColor;
	private final int shiftTime;

	public CannonSmokeParticleData(float scale, Vec3 startColor, Vec3 endColor, int shiftTime) {
		this.scale = scale;
		this.startColor = startColor;
		this.endColor = endColor;
		this.shiftTime = shiftTime;
	}

	public CannonSmokeParticleData() {
		this(0, Vec3.ZERO, Vec3.ZERO, 1);
	}

	public float scale() {
		return this.scale;
	}

	public Vec3 startColor() {
		return this.startColor;
	}

	public Vec3 endColor() {
		return this.endColor;
	}

	public int shiftTime() {
		return this.shiftTime;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.CANNON_SMOKE.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale);

		buf.writeFloat((float) this.startColor.x());
		buf.writeFloat((float) this.startColor.y());
		buf.writeFloat((float) this.startColor.z());

		buf.writeFloat((float) this.endColor.x());
		buf.writeFloat((float) this.endColor.y());
		buf.writeFloat((float) this.endColor.z());

		buf.writeVarInt(this.shiftTime);
	}

	@Override
	public String writeToString() {
		return String.format("%d %f %f %f %f %f %f %d", this.scale, this.startColor.x(), this.startColor.y(), this.startColor.z(), this.endColor.x(), this.endColor.y(), this.endColor.z(), this.shiftTime);
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

