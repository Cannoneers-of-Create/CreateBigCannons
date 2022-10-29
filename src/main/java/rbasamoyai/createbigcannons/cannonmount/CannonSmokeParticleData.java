package rbasamoyai.createbigcannons.cannonmount;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.CBCParticleTypes;

public class CannonSmokeParticleData implements ParticleOptions {

	public static final Codec<CannonSmokeParticleData> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.FLOAT.fieldOf("scale").forGetter(data -> data.scale),
				Vector3f.CODEC.fieldOf("startColor").forGetter(data -> data.startColor),
				Vector3f.CODEC.fieldOf("endColor").forGetter(data -> data.endColor),
				Codec.INT.fieldOf("shiftTime").forGetter(data -> data.shiftTime))
				.apply(instance, CannonSmokeParticleData::new);
	});
	
	@SuppressWarnings("deprecation")
	public static final ParticleOptions.Deserializer<CannonSmokeParticleData> DESERIALIZER = new Deserializer<CannonSmokeParticleData>() {
		@Override
		public CannonSmokeParticleData fromNetwork(ParticleType<CannonSmokeParticleData> type, FriendlyByteBuf buf) {
			float scale = buf.readFloat();
			Vector3f startColor = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
			Vector3f endColor = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
			int shiftTime = buf.readVarInt();
			return new CannonSmokeParticleData(scale, startColor, endColor, shiftTime);
		}
		
		@Override
		public CannonSmokeParticleData fromCommand(ParticleType<CannonSmokeParticleData> type, StringReader reader) throws CommandSyntaxException {
			reader.expect(' '); float scale = reader.readFloat();
			reader.expect(' '); float rs = reader.readFloat();
			reader.expect(' '); float gs = reader.readFloat();
			reader.expect(' '); float bs = reader.readFloat();
			reader.expect(' '); float rf = reader.readFloat();
			reader.expect(' '); float gf = reader.readFloat();
			reader.expect(' '); float bf = reader.readFloat();
			reader.expect(' '); int shiftTime = reader.readInt();
			return new CannonSmokeParticleData(scale, new Vector3f(rs, gs, bs), new Vector3f(rf, gf, bf), shiftTime);
		}
	};
	
	private final float scale;
	private final Vector3f startColor;
	private final Vector3f endColor;
	private final int shiftTime;
	
	public CannonSmokeParticleData(float scale, Vector3f startColor, Vector3f endColor, int shiftTime) {
		this.scale = scale;
		this.startColor = startColor;
		this.endColor = endColor;
		this.shiftTime = shiftTime;
	}
	
	public float scale() { return this.scale; }
	public Vector3f startColor() { return this.startColor; }
	public Vector3f endColor() { return this.endColor; }
	public int shiftTime() { return this.shiftTime; }
	
	@Override public ParticleType<?> getType() { return CBCParticleTypes.CANNON_SMOKE.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale);
		
		buf.writeFloat(this.startColor.x());
		buf.writeFloat(this.startColor.y());
		buf.writeFloat(this.startColor.z());
		
		buf.writeFloat(this.endColor.x());
		buf.writeFloat(this.endColor.y());
		buf.writeFloat(this.endColor.z());
		
		buf.writeVarInt(this.shiftTime);
	}

	@Override
	public String writeToString() {
		return String.format("%d %f %f %f %f %f %f %d", this.scale, this.startColor.x(), this.startColor.y(), this.startColor.z(), this.endColor.x(), this.endColor.y(), this.endColor.z(), this.shiftTime);
	}

}

