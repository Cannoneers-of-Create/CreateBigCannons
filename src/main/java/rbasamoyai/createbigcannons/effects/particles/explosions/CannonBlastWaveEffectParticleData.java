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

public class CannonBlastWaveEffectParticleData extends AbstractBlastWaveEffectParticleData<CannonBlastWaveEffectParticleData> {

	private static final MapCodec<CannonBlastWaveEffectParticleData> CODEC = createMapCodec(CannonBlastWaveEffectParticleData::new);
    private static final StreamCodec<RegistryFriendlyByteBuf, CannonBlastWaveEffectParticleData> STREAM_CODEC = createStreamCodec(CannonBlastWaveEffectParticleData::new);

	public CannonBlastWaveEffectParticleData(double blastRAdius, Holder<SoundEvent> soundEvent, SoundSource soundSource,
											 float volume, float pitch, float airAbsorption, float power) {
		super(blastRAdius, soundEvent, soundSource, volume, pitch, airAbsorption, power);
	}

	public CannonBlastWaveEffectParticleData() { super(); }

	@Override public MapCodec<CannonBlastWaveEffectParticleData> getCodec(ParticleType<CannonBlastWaveEffectParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, CannonBlastWaveEffectParticleData> getStreamCodec() { return STREAM_CODEC; }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<CannonBlastWaveEffectParticleData> getFactory() {
		return new BlastWaveEffectParticle.CannonBlastProvider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.CANNON_BLAST_WAVE.get(); }

}
