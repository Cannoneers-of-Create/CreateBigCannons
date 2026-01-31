package rbasamoyai.createbigcannons.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ModScreenShakeHandler;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ScreenShakeEffect;

public class CBCScreenShakeHandler extends ModScreenShakeHandler.Impl {

	@Override
	public void addEffect(ScreenShakeEffect effect) {
		Minecraft mc = Minecraft.getInstance();
		if (effect.duration == -1) {
			int ticks = 0;
			if (!CBCConfigs.client().isInstantaneousBlastEffect() && mc.player != null) {
				double distSqr = mc.player.distanceToSqr(effect.posX, effect. posY, effect.posZ);
				double timeInSec = Math.sqrt(distSqr) / CBCConfigs.client().blastEffectDelaySpeed.getF();
				ticks = Mth.floor(timeInSec * 20);
			}
			effect = new ScreenShakeEffect(ticks, effect.yawMagnitude, effect.pitchMagnitude, effect.rollMagnitude,
				effect.yawJitter, effect.pitchJitter, effect.rollJitter, effect.posX, effect.posY, effect.posZ);
		}
		super.addEffect(effect);
	}

	@Override
	public ScreenShakeEffect modifyScreenShake(ScreenShakeEffect effect) {
		float shakeScale = CBCConfigs.client().cannonScreenShakeIntensity.getF();
		return shakeScale == 1 ? effect : effect.copyWithProgressAndDuration(
			effect.yawMagnitude * shakeScale,
			effect.pitchMagnitude * shakeScale,
			effect.rollMagnitude * shakeScale,
			effect.yawJitter,
			effect.pitchJitter,
			effect.rollJitter,
			effect.posX,
			effect.posY,
			effect.posZ);
	}

	@Override
	protected void applyConstraints() {
		super.applyConstraints();
		double maxRotation = 45 * CBCConfigs.client().cannonScreenShakeIntensity.getF();
		if (Math.abs(this.displacement.x) > maxRotation) {
			this.displacement = new Vec3(Math.copySign(maxRotation, this.displacement.x), this.displacement.y, this.displacement.z);
			this.velocity = this.velocity.multiply(0, 1, 1);
		}
		if (Math.abs(this.displacement.y) > maxRotation) {
			this.displacement = new Vec3(this.displacement.x, Math.copySign(maxRotation, this.displacement.y), this.displacement.z);
			this.velocity = this.velocity.multiply(1, 0, 1);
		}
		if (Math.abs(this.displacement.y) > maxRotation) {
			this.displacement = new Vec3(this.displacement.x, this.displacement.y, Math.copySign(maxRotation, this.displacement.z));
			this.velocity = this.velocity.multiply(1, 1, 0);
		}
	}

	@Override protected double getRestitution() { return CBCConfigs.client().cannonScreenShakeSpringiness.getF(); }
	@Override protected double getDrag() { return CBCConfigs.client().cannonScreenShakeDecay.getF(); }

}
