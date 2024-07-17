package rbasamoyai.createbigcannons.effects.particles.smoke;

import org.joml.Vector3f;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.phys.Vec3;

public class GasCloudParticle extends SmokeShellSmokeParticle {

	GasCloudParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float size, Vector3f color, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz, size, sprites);
		this.rCol = color.x();
		this.gCol = color.y();
		this.bCol = color.z();
	}

	@Override protected void tickColors() {}

	@Override protected Vec3 createWind() { return Vec3.ZERO; }

	public static class Provider implements ParticleProvider<GasCloudParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(GasCloudParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new GasCloudParticle(level, x, y, z, dx, dy, dz, data.scale(), data.color(), this.sprites);
		}
	}

}
