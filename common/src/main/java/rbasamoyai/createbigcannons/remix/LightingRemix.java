package rbasamoyai.createbigcannons.remix;

import javax.annotation.Nullable;

import org.joml.Matrix4f;

import com.mojang.blaze3d.platform.Lighting;

public class LightingRemix {

	@Nullable
	private static Matrix4f LEVEL_LIGHTING = null;

	public static void cacheLevelLightingMatrix(Matrix4f mat) { LEVEL_LIGHTING = new Matrix4f(mat); }

	public static void reapplyLevelLighting(boolean isNether) {
		if (LEVEL_LIGHTING == null)
			return;
		if (isNether) {
			Lighting.setupNetherLevel(LEVEL_LIGHTING);
		} else {
			Lighting.setupLevel(LEVEL_LIGHTING);
		}
	}

	public static void clearCache() { LEVEL_LIGHTING = null; }

}
