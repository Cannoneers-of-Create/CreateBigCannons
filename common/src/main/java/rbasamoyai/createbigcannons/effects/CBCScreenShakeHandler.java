package rbasamoyai.createbigcannons.effects;

import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ModScreenShakeHandler;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ScreenShakeEffect;

public class CBCScreenShakeHandler extends ModScreenShakeHandler.Impl {

	private static final ScreenShakeEffect ZERO_SHAKE = new ScreenShakeEffect(1, 0);

	@Override
	public ScreenShakeEffect modifyScreenShake(ScreenShakeEffect effect) {
		float scale = CBCConfigs.CLIENT.cannonScreenShakeIntensity.getF();
		if (scale == 0)
			return ZERO_SHAKE;
		return scale == 1 ? effect : effect.copyWithProgressAndDuration(effect.yawMagnitude * scale,
			effect.pitchMagnitude * scale, effect.rollMagnitude * scale);
	}

}
