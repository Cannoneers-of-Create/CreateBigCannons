package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.serialization.Codec;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class CannonBlastWaveEffectParticleData extends AbstractBlastWaveEffectParticleData<CannonBlastWaveEffectParticleData> {

	private static final Deserializer<CannonBlastWaveEffectParticleData> DESERIALIZER = createDeserializer(CannonBlastWaveEffectParticleData::new);
	private static final Codec<CannonBlastWaveEffectParticleData> CODEC = createCodec(CannonBlastWaveEffectParticleData::new);

	public CannonBlastWaveEffectParticleData(double radius, SoundEvent soundEvent, SoundSource soundSource, float volume,
											 float pitch, float airAbsorption, float power) {
		super(radius, soundEvent, soundSource, volume, pitch, airAbsorption, power);
	}

	public CannonBlastWaveEffectParticleData() { super(); }

	@Override public Deserializer<CannonBlastWaveEffectParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<CannonBlastWaveEffectParticleData> getCodec(ParticleType<CannonBlastWaveEffectParticleData> type) { return CODEC; }

	@Override
	public ParticleProvider<CannonBlastWaveEffectParticleData> getFactory() {
		return new BlastWaveEffectParticle.CannonBlastProvider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.CANNON_BLAST_WAVE.get(); }

}
