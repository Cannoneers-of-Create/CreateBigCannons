package rbasamoyai.createbigcannons.remix;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTEfx;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.util.Mth;
import rbasamoyai.createbigcannons.effects.sounds.HasAirAbsorptionSound;

public class EffectsRemix {

	public static void applyCustomSoundInstanceEffects(SoundInstance sound, int channelSource) {
		if (sound instanceof HasAirAbsorptionSound aas) {
			float airAbsorption = Mth.clamp(aas.getAirAbsorption(), 0f, 10f);
			AL10.alSourcef(channelSource, EXTEfx.AL_AIR_ABSORPTION_FACTOR, airAbsorption);
		}
	}

}
