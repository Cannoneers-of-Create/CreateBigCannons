package rbasamoyai.createbigcannons.remix;

import com.mojang.blaze3d.platform.Lighting;

public class LightingRemix {

	public static void reapplyLevelLighting(boolean isNether) {
		if (isNether) {
			Lighting.setupNetherLevel();
		} else {
			Lighting.setupLevel();
		}
	}

}
