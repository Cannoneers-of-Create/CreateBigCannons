package rbasamoyai.createbigcannons.mixin.client;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.Lighting;

import rbasamoyai.createbigcannons.remix.LightingRemix;

@Mixin(Lighting.class)
public class LightingMixin {

	@Inject(method = "setupLevel", at = @At("HEAD"))
	private static void createbigcannons$setupLevel(Matrix4f lighting, CallbackInfo ci) {
		LightingRemix.cacheLevelLightingMatrix(lighting);
	}

	@Inject(method = "setupNetherLevel", at = @At("HEAD"))
	private static void createbigcannons$setupNetherLevel(Matrix4f lighting, CallbackInfo ci) {
		LightingRemix.cacheLevelLightingMatrix(lighting);
	}

}
