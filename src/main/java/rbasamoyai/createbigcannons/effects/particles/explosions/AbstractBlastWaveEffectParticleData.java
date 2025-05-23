package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Function7;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public abstract class AbstractBlastWaveEffectParticleData<DATA extends AbstractBlastWaveEffectParticleData<DATA>>
	implements ParticleOptions, ICustomParticleData<DATA> {

	protected static <DATA extends AbstractBlastWaveEffectParticleData<DATA>> MapCodec<DATA> createMapCodec(Constructor<DATA> cons) {
		return RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.DOUBLE.fieldOf("blastRadius").forGetter(AbstractBlastWaveEffectParticleData::blastRadius),
			SoundEvent.CODEC.fieldOf("soundEvent").forGetter(AbstractBlastWaveEffectParticleData::soundEvent),
			CBCUtils.SOUND_SOURCE_CODEC.fieldOf("soundSource").forGetter(AbstractBlastWaveEffectParticleData::soundSource),
			Codec.FLOAT.fieldOf("volume").forGetter(AbstractBlastWaveEffectParticleData::volume),
			Codec.FLOAT.fieldOf("pitch").forGetter(AbstractBlastWaveEffectParticleData::pitch),
			Codec.FLOAT.fieldOf("airAbsorption").forGetter(AbstractBlastWaveEffectParticleData::airAbsorption),
			Codec.FLOAT.fieldOf("power").forGetter(AbstractBlastWaveEffectParticleData::power)
		).apply(i, cons));
	}

    protected static <DATA extends AbstractBlastWaveEffectParticleData<DATA>> StreamCodec<RegistryFriendlyByteBuf, DATA> createStreamCodec(Constructor<DATA> cons) {
        /*return StreamCodec.composite( fixme
            ByteBufCodecs.DOUBLE, AbstractBlastWaveEffectParticleData::blastRadius,
            SoundEvent.STREAM_CODEC, AbstractBlastWaveEffectParticleData::soundEvent,
            CBCUtils.SOUND_SOURCE_CODEC, AbstractBlastWaveEffectParticleData::soundSource,
            ByteBufCodecs.FLOAT, AbstractBlastWaveEffectParticleData::volume,
            ByteBufCodecs.FLOAT, AbstractBlastWaveEffectParticleData::pitch,
            ByteBufCodecs.FLOAT, AbstractBlastWaveEffectParticleData::airAbsorption,
            ByteBufCodecs.FLOAT, AbstractBlastWaveEffectParticleData::power,
            cons
        );*/
        return null;
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

	protected AbstractBlastWaveEffectParticleData() { this(0, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.GENERIC_EXPLODE.value()), SoundSource.BLOCKS, 1, 1, 0, 0); }

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
