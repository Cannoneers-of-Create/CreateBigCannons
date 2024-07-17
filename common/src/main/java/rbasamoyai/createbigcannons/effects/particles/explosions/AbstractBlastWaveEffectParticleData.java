package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Function7;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public abstract class AbstractBlastWaveEffectParticleData<DATA extends AbstractBlastWaveEffectParticleData<DATA>>
	implements ParticleOptions, ICustomParticleData<DATA> {

	protected static <DATA extends AbstractBlastWaveEffectParticleData<DATA>> Deserializer<DATA> createDeserializer(Constructor<DATA> cons) {
		return new Deserializer<>() {
			@Override
			public DATA fromCommand(ParticleType<DATA> particleType, StringReader reader) throws CommandSyntaxException {
				reader.expect(' ');
				double blastRadius = reader.readDouble();
				reader.expect(' ');
				String eventStr = reader.readString();
				SoundEvent soundEvent = CBCRegistryUtils.getSoundEvent(CBCUtils.location(eventStr));
				if (soundEvent == null)
					throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
				reader.expect(' ');
				String sourceStr = reader.readString();
				SoundSource soundSource = CBCUtils.soundSourceFromName(sourceStr);
				if (soundSource == null)
					throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
				reader.expect(' ');
				float volume = reader.readFloat();
				reader.expect(' ');
				float pitch = reader.readFloat();
				reader.expect(' ');
				float airAbsorption = reader.readFloat();
				reader.expect(' ');
				float power = reader.readFloat();
				return cons.apply(blastRadius, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent), soundSource, volume, pitch, airAbsorption, power);
			}

			@Override
			public DATA fromNetwork(ParticleType<DATA> particleType, FriendlyByteBuf buffer) {
				double blastRadius = buffer.readDouble();
				SoundEvent soundEvent = CBCRegistryUtils.getSoundEvent(buffer.readVarInt());
				SoundSource soundSource = buffer.readEnum(SoundSource.class);
				float volume = buffer.readFloat();
				float pitch = buffer.readFloat();
				float airAbsorption = buffer.readFloat();
				float power = buffer.readFloat();
				return cons.apply(blastRadius, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent), soundSource, volume, pitch, airAbsorption, power);
			}
		};
	}

	protected static <DATA extends AbstractBlastWaveEffectParticleData<DATA>> Codec<DATA> createCodec(Constructor<DATA> cons) {
		return RecordCodecBuilder.create(i -> i.group(
			Codec.DOUBLE.fieldOf("blastRadius").forGetter(AbstractBlastWaveEffectParticleData::blastRadius),
			SoundEvent.CODEC.fieldOf("soundEvent").forGetter(AbstractBlastWaveEffectParticleData::soundEvent),
			CBCUtils.SOUND_SOURCE_CODEC.fieldOf("soundSource").forGetter(AbstractBlastWaveEffectParticleData::soundSource),
			Codec.FLOAT.fieldOf("volume").forGetter(AbstractBlastWaveEffectParticleData::volume),
			Codec.FLOAT.fieldOf("pitch").forGetter(AbstractBlastWaveEffectParticleData::pitch),
			Codec.FLOAT.fieldOf("airAbsorption").forGetter(AbstractBlastWaveEffectParticleData::airAbsorption),
			Codec.FLOAT.fieldOf("power").forGetter(AbstractBlastWaveEffectParticleData::power)
		).apply(i, cons));
	}

	private final double blastRadius;
	private final Holder<SoundEvent> soundEvent;
	private final SoundSource soundSource;
	private final float volume;
	private final float pitch;
	private final float airAbsorption;
	private final float power;

	protected AbstractBlastWaveEffectParticleData(double blastRadius, Holder<SoundEvent> soundEvent, SoundSource soundSource,
												  float volume, float pitch, float airAbsorption, float power) {
		this.blastRadius = blastRadius;
		this.soundEvent = soundEvent;
		this.soundSource = soundSource;
		this.volume = volume;
		this.pitch = pitch;
		this.airAbsorption = airAbsorption;
		this.power = power;
	}

	protected AbstractBlastWaveEffectParticleData() { this(0, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.GENERIC_EXPLODE), SoundSource.BLOCKS, 1, 1, 0, 0); }

	public double blastRadius() { return this.blastRadius; }
	public Holder<SoundEvent> soundEvent() { return this.soundEvent; }
	public SoundSource soundSource() { return this.soundSource; }
	public float volume() { return this.volume; }
	public float pitch() { return this.pitch; }
	public float airAbsorption() { return this.airAbsorption; }
	public float power() { return this.power; }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeDouble(this.blastRadius);
		buffer.writeVarInt(CBCRegistryUtils.getSoundEventNumericId(this.soundEvent.value()))
			.writeEnum(this.soundSource)
			.writeFloat(this.volume)
			.writeFloat(this.pitch)
			.writeFloat(this.airAbsorption)
			.writeFloat(this.power);
	}

	@Override
	public String writeToString() {
		return String.format("%f %s %s %f %f %f %f", this.blastRadius, CBCRegistryUtils.getSoundEventLocation(this.soundEvent.value()).toString(),
			this.soundSource.getName(), this.volume, this.pitch, this.airAbsorption, this.power);
	}

	public interface Constructor<DATA extends AbstractBlastWaveEffectParticleData<DATA>>
		extends Function7<Double, Holder<SoundEvent>, SoundSource, Float, Float, Float, Float, DATA> {
	}

}
