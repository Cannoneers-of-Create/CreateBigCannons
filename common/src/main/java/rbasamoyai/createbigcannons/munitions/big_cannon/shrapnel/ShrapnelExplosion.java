package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.effects.particles.explosions.FlakCloudParticleData;
import rbasamoyai.createbigcannons.effects.particles.explosions.ShrapnelCloudParticleData;
import rbasamoyai.createbigcannons.remix.CustomExplosion;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;

public class ShrapnelExplosion extends CustomExplosion.Impl {


	public ShrapnelExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, double toBlowX,
							 double toBlowY, double toBlowZ, float radius, BlockInteraction interaction) {
		super(level, source, damageSource, null, toBlowX, toBlowY, toBlowZ, radius, false, interaction);
	}

	public ShrapnelExplosion(Level level, ClientboundCBCExplodePacket packet) {
		super(level, packet);
	}

	@Override
	public void playLocalSound(Level level, double x, double y, double z) {
		float pitch = 0.7f + this.level.random.nextFloat(-0.2f, 0.2f);
		CBCSoundEvents.SHRAPNEL_SHELL_EXPLOSION.playAt(this.level, this.x, this.y, this.z, 6.0f, pitch, false);
	}

	@Override
	public void sendExplosionToClient(ServerPlayer player) {
		if (player.distanceToSqr(this.x, this.y, this.z) < 250000.0d) {
			Vec3 knockback = this.getHitPlayers().getOrDefault(player, Vec3.ZERO);
			NetworkPlatform.sendToClientPlayer(new ClientboundCBCExplodePacket(this.x, this.y, this.z, this.size, this.getToBlow(),
				(float) knockback.x, (float) knockback.y, (float) knockback.z, ClientboundCBCExplodePacket.ExplosionType.SHRAPNEL), player);
		}
	}

	@Override
	protected void spawnParticles() {
		this.level.addParticle(new ShrapnelCloudParticleData(), true, this.x, this.y, this.z, 0, 0, 0);
	}

}
