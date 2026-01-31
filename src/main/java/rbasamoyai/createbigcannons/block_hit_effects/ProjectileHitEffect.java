package rbasamoyai.createbigcannons.block_hit_effects;

import com.google.gson.JsonObject;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.phys.Vec3;

public record ProjectileHitEffect(float effectMultiplier, float maximumEffectScale, float pitchModifier, float volume) {

	public double getMagnitude(Vec3 velocity) { return Math.min(velocity.length() * this.effectMultiplier, this.maximumEffectScale); }

	public float getPitch(float basePitch) { return basePitch + this.pitchModifier; }

	public static ProjectileHitEffect fromJson(JsonObject obj) {
		float effectMultiplier = Math.max(0, GsonHelper.getAsFloat(obj, "effect_multiplier", 1));
		float maximumEffectScale = Math.max(0, GsonHelper.getAsFloat(obj, "maximum_effect_scale", 10));
		float pitchModifier = GsonHelper.getAsFloat(obj, "pitch_modifier", -0.5f);
		float volume = Math.max(0, GsonHelper.getAsFloat(obj, "volume", 3.5f));
		return new ProjectileHitEffect(effectMultiplier, maximumEffectScale, pitchModifier, volume);
	}

}
