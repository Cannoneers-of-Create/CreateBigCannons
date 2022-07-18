package rbasamoyai.createbigcannons.cannonmount;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.CBCParticleTypes;

public class CannonPlumeParticleData implements ParticleOptions {

	public static final Codec<CannonPlumeParticleData> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.FLOAT.fieldOf("scale").forGetter(data -> data.scale))
				.apply(instance, CannonPlumeParticleData::new);
	});
	
	@SuppressWarnings("deprecation")
	public static final ParticleOptions.Deserializer<CannonPlumeParticleData> DESERIALIZER = new Deserializer<CannonPlumeParticleData>() {
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
	
	public static final float MAX_SCALE = 6.0f;
	
	private final float scale;
	
	public CannonPlumeParticleData(float scale) {
		this.scale = scale;
	}
	
	public float scale() { return this.scale; }
	
	@Override public ParticleType<?> getType() { return CBCParticleTypes.CANNON_PLUME.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale);
	}

	@Override
	public String writeToString() {
		return String.format("%d", this.scale);
	}

}
