package rbasamoyai.createbigcannons.effects.particles.explosions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.sounds.AirAbsorptionWrapper;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ScreenShakeEffect;

public class BlastWaveEffectParticle extends NoRenderParticle {

	private final double blastRadius;
	private final SoundEvent soundEvent;
	private final SoundSource soundSource;
	private final float volume;
	private final float pitch;
	private final float airAbsorption;
	private final float shakePower;
	private final float shakeLimit;

	private final double functionalRadius;
	private double currentRadius;

	BlastWaveEffectParticle(ClientLevel level, double x, double y, double z, double blastRadius, SoundEvent soundEvent, SoundSource soundSource,
							float volume, float pitch, float airAbsorption, float shakePower, float shakeLimit) {
		super(level, x, y, z);
        this.blastRadius = blastRadius;
        this.soundEvent = soundEvent;
        this.soundSource = soundSource;
		this.volume = volume;
        this.pitch = pitch;
        this.airAbsorption = airAbsorption;
		this.shakePower = shakePower;
		this.shakeLimit = shakeLimit;

		this.functionalRadius = Math.max(this.blastRadius, this.volume * 16);

        this.xd = 0;
		this.yd = 0;
		this.zd = 0;
		this.gravity = 0;
	}

	@Override
	public void tick() {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null) {
			this.remove();
			return;
		}

		boolean instant = CBCConfigs.CLIENT.isInstantaneousBlastEffect();
		if (instant)
			this.currentRadius = 0;
		double speed = CBCConfigs.CLIENT.blastEffectDelaySpeed.getF() * 0.05d;
		double newRadius = instant ? this.functionalRadius : Math.min(this.currentRadius + speed, this.functionalRadius);

		double dist = Math.sqrt(minecraft.player.distanceToSqr(this.x, this.y, this.z));
		if (this.currentRadius <= dist && dist <= newRadius) {
			if (dist < this.blastRadius && this.blastRadius > 0.1) {
				float f = 1 - (float)(dist / this.blastRadius);
				float f2 = f * f;
				float shake = Math.min(this.shakeLimit, this.shakePower * f2);
				CBCClientCommon.shakeScreenOnClient(new ScreenShakeEffect(0, shake, shake * 0.5f, shake * 0.5f, 1, 1, 1, this.x, this.y, this.z));
			}
			double volumeDist = this.volume * 16;
			if (dist < volumeDist) {
				SoundInstance sound = new SimpleSoundInstance(this.soundEvent, this.soundSource, this.volume, this.pitch, this.x, this.y, this.z);
				if (CBCConfigs.CLIENT.blastSoundAirAbsorption.get())
					sound = new AirAbsorptionWrapper(sound, this.airAbsorption);
				minecraft.getSoundManager().play(sound);
			}
			this.remove();
			return;
		}

		if (instant || newRadius >= this.functionalRadius) {
			this.remove();
		} else {
			this.currentRadius = newRadius;
			super.tick();
		}
	}

	public static class CannonBlastProvider implements ParticleProvider<CannonBlastWaveEffectParticleData> {
		@Override
		public Particle createParticle(CannonBlastWaveEffectParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			float power = type.power() * CBCConfigs.CLIENT.bigCannonShakePowerMultiplier.getF();
			BlastWaveEffectParticle particle = new BlastWaveEffectParticle(level, x, y, z, type.blastRadius(), type.soundEvent(),
				type.soundSource(), type.volume(), type.pitch(), type.airAbsorption(), power, CBCConfigs.CLIENT.bigCannonShakePowerLimit.getF());
			particle.setLifetime(100); // Timeout
			return particle;
		}
	}

	public static class ShellBlastProvider implements ParticleProvider<ShellBlastWaveEffectParticleData> {
		@Override
		public Particle createParticle(ShellBlastWaveEffectParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			float power = type.power() * CBCConfigs.CLIENT.shellShakePowerMultiplier.getF();
			BlastWaveEffectParticle particle = new BlastWaveEffectParticle(level, x, y, z, type.blastRadius(), type.soundEvent(),
				type.soundSource(), type.volume(), type.pitch(), type.airAbsorption(), power, CBCConfigs.CLIENT.shellShakePowerLimit.getF());
			particle.setLifetime(100); // Timeout
			return particle;
		}
	}

}
