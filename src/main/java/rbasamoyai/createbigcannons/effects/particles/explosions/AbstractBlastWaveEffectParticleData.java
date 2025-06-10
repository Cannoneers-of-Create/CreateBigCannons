package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.datafixers.util.Function7;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCStreamCodecs;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public abstract class AbstractBlastWaveEffectParticleData<DATA extends AbstractBlastWaveEffectParticleData<DATA>>
	implements ParticleOptions, ICustomParticleData<DATA> {

	protected static <DATA extends AbstractBlastWaveEffectParticleData<DATA>> MapCodec<DATA> createMapCodec(Constructor<DATA> cons) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.DOUBLE.fieldOf("blast_radius").forGetter(AbstractBlastWaveEffectParticleData::blastRadius),
			SoundEvent.CODEC.fieldOf("sound_event").forGetter(AbstractBlastWaveEffectParticleData::soundEvent),
			CBCUtils.SOUND_SOURCE_CODEC.fieldOf("sound_source").forGetter(AbstractBlastWaveEffectParticleData::soundSource),
			Codec.FLOAT.fieldOf("volume").forGetter(AbstractBlastWaveEffectParticleData::volume),
			Codec.FLOAT.fieldOf("pitch").forGetter(AbstractBlastWaveEffectParticleData::pitch),
			Codec.FLOAT.fieldOf("air_absorption").forGetter(AbstractBlastWaveEffectParticleData::airAbsorption),
			Codec.FLOAT.fieldOf("power").forGetter(AbstractBlastWaveEffectParticleData::power)
		).apply(i, cons));
	}

    protected static <DATA extends AbstractBlastWaveEffectParticleData<DATA>> StreamCodec<RegistryFriendlyByteBuf, DATA> createStreamCodec(Constructor<DATA> cons) {
        return CBCStreamCodecs.composite(
            ByteBufCodecs.DOUBLE, AbstractBlastWaveEffectParticleData::blastRadius,
            SoundEvent.STREAM_CODEC, AbstractBlastWaveEffectParticleData::soundEvent,
            CBCStreamCodecs.SOUND_SOURCE, AbstractBlastWaveEffectParticleData::soundSource,
            ByteBufCodecs.FLOAT, AbstractBlastWaveEffectParticleData::volume,
            ByteBufCodecs.FLOAT, AbstractBlastWaveEffectParticleData::pitch,
            ByteBufCodecs.FLOAT, AbstractBlastWaveEffectParticleData::airAbsorption,
            ByteBufCodecs.FLOAT, AbstractBlastWaveEffectParticleData::power,
            cons);
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

	protected AbstractBlastWaveEffectParticleData() { this(0, CBCRegistryUtils.getSoundEventRegistry().wrapAsHolder(SoundEvents.GENERIC_EXPLODE.value()), SoundSource.BLOCKS, 1, 1, 0, 0); }

	public double blastRadius() { return this.blastRadius; }
	public Holder<SoundEvent> soundEvent() { return this.soundEvent; }
	public SoundSource soundSource() { return this.soundSource; }
	public float volume() { return this.volume; }
	public float pitch() { return this.pitch; }
	public float airAbsorption() { return this.airAbsorption; }
	public float power() { return this.power; }

	public interface Constructor<DATA extends AbstractBlastWaveEffectParticleData<DATA>>
		extends Function7<Double, Holder<SoundEvent>, SoundSource, Float, Float, Float, Float, DATA> {
	}

}
