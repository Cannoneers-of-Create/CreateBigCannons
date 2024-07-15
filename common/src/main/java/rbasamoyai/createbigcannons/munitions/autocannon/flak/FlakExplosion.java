package rbasamoyai.createbigcannons.munitions.autocannon.flak;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.remix.CustomExplosion;
import rbasamoyai.createbigcannons.effects.particles.explosions.FlakCloudParticleData;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;

public class FlakExplosion extends CustomExplosion.Impl {


	public FlakExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, double toBlowX,
						 double toBlowY, double toBlowZ, float radius, BlockInteraction interaction) {
		super(level, source, damageSource, null, toBlowX, toBlowY, toBlowZ, radius, false, interaction);
	}

	public FlakExplosion(Level level, ClientboundCBCExplodePacket packet) {
		super(level, packet);
	}

	@Override
	public void playLocalSound(Level level, double x, double y, double z) {
		float pitch = 1.4f + this.level.random.nextFloat(-0.2f, 0.2f);
		CBCSoundEvents.FLAK_ROUND_EXPLOSION.playAt(this.level, this.x, this.y, this.z, 4.0f, pitch, false);
	}

	@Override
	public void sendExplosionToClient(ServerPlayer player) {
		if (player.distanceToSqr(this.x, this.y, this.z) < 250000.0d) {
			Vec3 knockback = this.getHitPlayers().getOrDefault(player, Vec3.ZERO);
			NetworkPlatform.sendToClientPlayer(new ClientboundCBCExplodePacket(this.x, this.y, this.z, this.size, this.getToBlow(),
				(float) knockback.x, (float) knockback.y, (float) knockback.z, ClientboundCBCExplodePacket.ExplosionType.FLAK), player);
		}
	}

	@Override
	protected void spawnParticles() {
		this.level.addParticle(new FlakCloudParticleData(), true, this.x, this.y, this.z, 0, 0, 0);
	}

}
