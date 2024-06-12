package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import javax.annotation.Nullable;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.effects.particles.smoke.DebrisSmokeParticleData;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;
import rbasamoyai.createbigcannons.remix.CustomExplosion;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ScreenShakeEffect;

public class MortarStoneExplosion extends CustomExplosion.Impl {

	public MortarStoneExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, double toBlowX,
								double toBlowY, double toBlowZ, float radius, BlockInteraction interaction) {
		super(level, source, damageSource, null, toBlowX, toBlowY, toBlowZ, radius, false, interaction);
	}

	public MortarStoneExplosion(Level level, ClientboundCBCExplodePacket packet) {
		super(level, packet);
	}

	@Override
	public void playLocalSound(Level level, double x, double y, double z) {
		CBCSoundEvents.MORTAR_STONE_EXPLODE.playAt(level, x, y, z, 3f, 0.95f + level.random.nextFloat() * 0.1f, false);
	}

	@Override
	protected void spawnParticles() {
		ParticleOptions options = new DebrisSmokeParticleData(this.size);
		float f1 = 0.15f * this.size;
		for (int i = 0; i < 50; ++i) {
			double rx = this.x + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.1f;
			double ry = this.y + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.1f;
			double rz = this.z + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.1f;
			double dx = (this.level.random.nextDouble() - this.level.random.nextDouble()) * f1;
			double dy = (this.level.random.nextDouble() - this.level.random.nextDouble()) * f1;
			double dz = (this.level.random.nextDouble() - this.level.random.nextDouble()) * f1;
			this.level.addParticle(options, true, rx, ry, rz, dx, dy, dz);
		}
	}

	@Override
	public void sendExplosionToClient(ServerPlayer player) {
		double distSqr = player.distanceToSqr(this.x, this.y, this.z);
		if (distSqr < 10000.0d) {
			float f = Math.max(1f - (float) distSqr / 625f, 0);
			float f1 = this.size * f;
			float shake = Math.min(45, f1 * 2f);
			CreateBigCannons.shakePlayerScreen(player, new ScreenShakeEffect(0, shake, shake * 0.5f, shake * 0.5f, 1, 1, 1));

			Vec3 knockback = this.getHitPlayers().getOrDefault(player, Vec3.ZERO);
			NetworkPlatform.sendToClientPlayer(new ClientboundCBCExplodePacket(this.x, this.y, this.z, this.size, this.getToBlow(),
				(float) knockback.x, (float) knockback.y, (float) knockback.z, ClientboundCBCExplodePacket.ExplosionType.MORTAR_STONE), player);
		}
	}

}
