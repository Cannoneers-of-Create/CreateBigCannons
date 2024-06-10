package rbasamoyai.createbigcannons.effects;

import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ModScreenShakeHandler;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ScreenShakeEffect;

public class CBCScreenShakeHandler extends ModScreenShakeHandler.Impl {

	private static final ScreenShakeEffect ZERO_SHAKE = new ScreenShakeEffect(1, 0, 1);

	@Override
	public ScreenShakeEffect modifyScreenShake(ScreenShakeEffect effect) {
		float shakeScale = CBCConfigs.CLIENT.cannonScreenShakeIntensity.getF();
		if (shakeScale == 0)
			return ZERO_SHAKE;
		return shakeScale == 1 ? effect : effect.copyWithProgressAndDuration(
			effect.yawMagnitude * shakeScale,
			effect.pitchMagnitude * shakeScale,
			effect.rollMagnitude * shakeScale,
			effect.yawJitter,
			effect.pitchJitter,
			effect.rollJitter);
	}

	@Override
	protected void applyConstraints() {
		super.applyConstraints();
		double maxRotation = 45 * CBCConfigs.CLIENT.cannonScreenShakeIntensity.getF();
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

	@Override protected double getRestitution() { return CBCConfigs.CLIENT.cannonScreenShakeSpringiness.getF(); }
	@Override protected double getDrag() { return CBCConfigs.CLIENT.cannonScreenShakeDecay.getF(); }

}
