package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class ShellBlastWaveEffectParticleData extends AbstractBlastWaveEffectParticleData<ShellBlastWaveEffectParticleData> {

	private static final MapCodec<ShellBlastWaveEffectParticleData> CODEC = createMapCodec(ShellBlastWaveEffectParticleData::new);
    private static final StreamCodec<RegistryFriendlyByteBuf, ShellBlastWaveEffectParticleData> STREAM_CODEC = createStreamCodec(ShellBlastWaveEffectParticleData::new);

	public ShellBlastWaveEffectParticleData(double blastRadius, Holder<SoundEvent> soundEvent, SoundSource soundSource,
											float volume, float pitch, float airAbsorption, float power) {
		super(blastRadius, soundEvent, soundSource, volume, pitch, airAbsorption, power);
	}

	public ShellBlastWaveEffectParticleData() { super(); }

	@Override public MapCodec<ShellBlastWaveEffectParticleData> getCodec(ParticleType<ShellBlastWaveEffectParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, ShellBlastWaveEffectParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<ShellBlastWaveEffectParticleData> getFactory() {
		return new BlastWaveEffectParticle.ShellBlastProvider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SHELL_BLAST_WAVE.get(); }

}
