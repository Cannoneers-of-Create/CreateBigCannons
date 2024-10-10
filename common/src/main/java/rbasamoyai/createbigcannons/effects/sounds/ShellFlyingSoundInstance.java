package rbasamoyai.createbigcannons.effects.sounds;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class ShellFlyingSoundInstance extends AbstractTickableSoundInstance {

	private final LocalPlayer player;
	private final AbstractCannonProjectile entity;
	private double radius;
	private int time;
	private int timeout;
	private float basePitch;

	public ShellFlyingSoundInstance(SoundEvent event, RandomSource randomSource, LocalPlayer player, AbstractCannonProjectile entity, double radius) {
		super(event, SoundSource.NEUTRAL, randomSource);
		this.player = player;
		this.entity = entity;
		this.radius = radius;
		this.looping = true;
		Vec3 pos = this.entity.position();
		Vec3 shellVelocity = this.entity.getDeltaMovement();
		double wakeLength = Math.min(shellVelocity.lengthSqr(), 100);
		Vec3 soundPos = pos.subtract(shellVelocity.normalize().scale(wakeLength));
		this.x = (float) soundPos.x;
		this.y = (float) soundPos.y;
		this.z = (float) soundPos.z;
		this.basePitch = 0.95f + this.entity.level().random.nextFloat() * 0.3f;
	}

	@Override
	public void tick() {
		++this.time;
		int FADE_IN_TIME = 1;
		int FADE_OUT_TIME = 20;
		if (!this.player.isRemoved() && !this.entity.isRemoved() && this.entity.getLocalSoundCooldown() <= 0) {
			Vec3 pos = this.entity.position();
			Vec3 shellVelocity = this.entity.getDeltaMovement();
			Vec3 playerDir = this.player.position().subtract(pos);
			float alignment = (float) shellVelocity.normalize().dot(playerDir.normalize());

			double wakeLength = Math.min(shellVelocity.lengthSqr(), 100);
			Vec3 soundPos = pos.subtract(shellVelocity.normalize().scale(wakeLength));
            this.x = (float) soundPos.x;
			this.y = (float) soundPos.y;
			this.z = (float) soundPos.z;
			float velSqr = (float) shellVelocity.lengthSqr();
			if (velSqr >= 1.0E-4f) {
				this.volume = Mth.clamp(velSqr / 4f, 0.0F, 1.0F);
			} else {
				this.volume = 0.0F;
			}
			if (this.time < FADE_IN_TIME)
				this.volume *= (float) this.time / (float) FADE_IN_TIME;
			double intensity = 1 - this.player.distanceToSqr(soundPos) / this.radius / this.radius;
			if (intensity < 1)
				this.volume *= (float) intensity;
            float pitchShift = Math.min(velSqr / 25f, 1) * alignment;
			this.pitch = Mth.clamp(this.basePitch + pitchShift, 0, 2);
			if (intensity <= 0 || alignment < -0.5) {
				++this.timeout;
			} else if (this.timeout > 0) {
				--this.timeout;
			}
		} else {
			this.timeout = FADE_OUT_TIME;
			this.volume = 0;
		}
		this.volume *= Mth.clamp(1f - (float) this.timeout / (float) FADE_OUT_TIME, 0, 1);
		if (this.timeout >= FADE_OUT_TIME) {
			this.stop();
			this.entity.setLocalSoundCooldown(3);
		}
	}



}
