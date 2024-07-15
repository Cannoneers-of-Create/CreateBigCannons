package rbasamoyai.createbigcannons.effects.particles;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class ParticleWindHandler {

	private static PerlinNoise BEARING_NOISE = createNoise(1024);
	private static PerlinNoise SPEED_NOISE = createNoise(1026);
    private static float deltaBearing;
    private static float bearing;
	private static double time;
	private static Vec3 oldWindForce = Vec3.ZERO;
	private static Vec3 windForce = Vec3.ZERO;

	private static PerlinNoise createNoise(long seed) {
		return PerlinNoise.create(new LegacyRandomSource(seed), 6, 1, 1, 0.1, 0.1, 0.1, 0.1);
	}

	/**
	 * Internal use only
	 */
	public static void refreshNoise(long seed) {
		BEARING_NOISE = createNoise(seed);
		SPEED_NOISE = createNoise(seed + 2);
		time = 0;
		updateWind();
        bearing = 0;
		oldWindForce = windForce;
	}

	/**
	 * Internal use only
	 */
	public static void updateWind() {
        bearing = Mth.wrapDegrees(bearing + deltaBearing);
		oldWindForce = windForce;
		time += 0.0005;
		float bearingChange = CBCConfigs.CLIENT.maxWindBearingChangeSpeed.getF();
		deltaBearing = (float) BEARING_NOISE.getValue(time, 0, 0);
		deltaBearing *= bearingChange;
        float speed = (float) SPEED_NOISE.getValue(0, time, 0) * 0.5f + 0.5f;
		double rotationRad = (bearing + deltaBearing) * Mth.DEG_TO_RAD;
		windForce = new Vec3(Math.cos(rotationRad), 0, Math.sin(rotationRad)).scale(speed);
	}

	public static Vec3 getWindForce(double partialTick) {
		double maximumWindSpeed = CBCConfigs.CLIENT.maximumWindSpeed.getF() * 0.05;
		return maximumWindSpeed < 1e-2d ? Vec3.ZERO : oldWindForce.lerp(windForce, partialTick).scale(maximumWindSpeed);
	}

}
