package rbasamoyai.createbigcannons.effects.particles.smoke;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class FallbackCannonSmokeParticle extends CannonSmokeParticle {

	FallbackCannonSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, dx, dy, dz, sprites, 0);
	}

	@Override public ParticleRenderType getRenderType() { return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }

	@Override
	public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
		Vec3 vec3 = renderInfo.getPosition();
		float f = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
		float g = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
		float h = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
		Quaternion quaternion;
		if (this.roll == 0.0F) {
			quaternion = renderInfo.rotation();
		} else {
			quaternion = new Quaternion(renderInfo.rotation());
			float i = Mth.lerp(partialTicks, this.oRoll, this.roll);
			quaternion.mul(Vector3f.ZP.rotation(i));
		}

		Vector3f[] vector3fs = new Vector3f[]{
			new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
		};
		float j = this.getQuadSize(partialTicks);

		for(int k = 0; k < 4; ++k) {
			Vector3f vector3f2 = vector3fs[k];
			vector3f2.transform(quaternion);
			vector3f2.mul(j);
			vector3f2.add(f, g, h);
		}

		float l = this.getU0();
		float m = this.getU1();
		float n = this.getV0();
		float o = this.getV1();
		int p = this.getLightColor(partialTicks);
		buffer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z())
			.uv(m, o)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.endVertex();
		buffer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z())
			.uv(m, n)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.endVertex();
		buffer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z())
			.uv(l, n)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.endVertex();
		buffer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z())
			.uv(l, o)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.endVertex();
	}

	public static class Provider implements ParticleProvider<FallbackCannonSmokeParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(FallbackCannonSmokeParticleData data, ClientLevel level, double x, double y, double z,
									   double dx, double dy, double dz) {
			FallbackCannonSmokeParticle particle = new FallbackCannonSmokeParticle(level, x, y, z, dx, dy, dz, this.sprites);
			particle.quadSize = data.size();
			particle.friction = data.friction();
			particle.setLifetime(data.lifetime());
			return particle;
		}
	}

}
