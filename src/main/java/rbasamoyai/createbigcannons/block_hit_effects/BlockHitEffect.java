package rbasamoyai.createbigcannons.block_hit_effects;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record BlockHitEffect(List<ParticleOptions> impactParticles, List<ParticleOptions> deflectParticles, HitSound impactSound, HitSound deflectSound) {

    public static final Codec<BlockHitEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ParticleTypes.CODEC.listOf().fieldOf("impact_particles").forGetter(BlockHitEffect::impactParticles),
        ParticleTypes.CODEC.listOf().fieldOf("deflect_particles").forGetter(BlockHitEffect::deflectParticles),
        HitSound.CODEC.fieldOf("impact_sound").forGetter(BlockHitEffect::impactSound),
        HitSound.CODEC.fieldOf("deflect_sound").forGetter(BlockHitEffect::deflectSound)
    ).apply(instance, BlockHitEffect::new));

	public void playEffect(Level level, boolean deflect, boolean forceDisplay, double x, double y, double z, double dx,
						   double dy, double dz, EntityType<?> entityType, BlockState blockState, ProjectileHitEffect projectileEffect) {
		List<ParticleOptions> particles = deflect ? this.deflectParticles : this.impactParticles;
		for (ParticleOptions option : particles) {
			option = ProjectileParticleEffectModifiers.applyEffects(option, entityType, blockState, projectileEffect);
			level.addParticle(option, forceDisplay, x, y, z, dx, dy, dz);
		}
		BlockHitEffect.HitSound sound = deflect ? this.deflectSound : this.impactSound;
		sound.playSound(level, x, y, z, dx, dy, dz, projectileEffect);
	}

	public record HitSound(ResourceLocation location, SoundSource source, float basePitch, float pitchVariation) {
        public static final Codec<HitSound> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("sound").forGetter(HitSound::location),
            CBCUtils.SOUND_SOURCE_CODEC.optionalFieldOf("source", SoundSource.BLOCKS)
                .validate(s -> s == null ? DataResult.error(() -> "Invalid sound type") : DataResult.success(s))
                .forGetter(HitSound::source),
            Codec.FLOAT.fieldOf("pitch").forGetter(HitSound::basePitch),
            Codec.FLOAT.fieldOf("pitch_variation").forGetter(HitSound::pitchVariation)
        ).apply(instance, HitSound::new));

		public void playSound(Level level, double x, double y, double z, double dx, double dy, double dz, ProjectileHitEffect projectileEffect) {
			EnvExecute.executeOnClient(() -> () -> CBCClientCommon.playCustomSound(this, level, x, y, z, dx, dy, dz, projectileEffect));
		}
	}

}
