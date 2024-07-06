package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.serialization.Codec;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class ShellBlastWaveEffectParticleData extends AbstractBlastWaveEffectParticleData<ShellBlastWaveEffectParticleData> {

	private static final Deserializer<ShellBlastWaveEffectParticleData> DESERIALIZER = createDeserializer(ShellBlastWaveEffectParticleData::new);
	private static final Codec<ShellBlastWaveEffectParticleData> CODEC = createCodec(ShellBlastWaveEffectParticleData::new);

	public ShellBlastWaveEffectParticleData(double blastRadius, SoundEvent soundEvent, SoundSource soundSource,
											float volume, float pitch, float airAbsorption, float power) {
		super(blastRadius, soundEvent, soundSource, volume, pitch, airAbsorption, power);
	}

	public ShellBlastWaveEffectParticleData() { super(); }

	@Override public Deserializer<ShellBlastWaveEffectParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<ShellBlastWaveEffectParticleData> getCodec(ParticleType<ShellBlastWaveEffectParticleData> type) { return CODEC; }

	@Override
	public ParticleProvider<ShellBlastWaveEffectParticleData> getFactory() {
		return new BlastWaveEffectParticle.ShellBlastProvider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SHELL_BLAST_WAVE.get(); }

}
