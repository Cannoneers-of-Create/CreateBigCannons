package rbasamoyai.createbigcannons.effects;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public class ShakeEffect {

	public final PerlinSimplexNoise yawNoise;
	public final PerlinSimplexNoise pitchNoise;
	public final PerlinSimplexNoise rollNoise;
	public final float magnitude;
	private final int startTime;
	private float timer;

	public ShakeEffect(int seed, int startTime, float magnitude) {
		this.yawNoise = new PerlinSimplexNoise(new LegacyRandomSource(seed + 1), ImmutableList.of(-2, -1, 0));
		this.pitchNoise = new PerlinSimplexNoise(new LegacyRandomSource(seed), ImmutableList.of(-2, -1, 0));
		this.rollNoise = new PerlinSimplexNoise(new LegacyRandomSource(seed + 2), ImmutableList.of(-2, -1, 0));
		this.magnitude = magnitude;
		this.startTime = startTime;
		this.timer = this.startTime;
	}

	public boolean tick() {
		if (this.startTime < 1) return true;
		this.timer--;
		return this.timer < 1;
	}

	public float getProgress(float partialTicks) { return this.timer - partialTicks; }
	public float getProgressNormalized(float partialTicks) { return this.startTime < 1 ? 0 : this.getProgress(partialTicks) / this.startTime; }

}
