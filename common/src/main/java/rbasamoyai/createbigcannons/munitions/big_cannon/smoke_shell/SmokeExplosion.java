package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;
import rbasamoyai.createbigcannons.remix.CustomExplosion;

public class SmokeExplosion extends CustomExplosion.Impl {

	public SmokeExplosion(Level level, @Nullable Entity source, double toBlowX, double toBlowY, double toBlowZ,
						  float radius, BlockInteraction interaction) {
		super(level, source, null, null, toBlowX, toBlowY, toBlowZ, radius, false, interaction);
	}

	public SmokeExplosion(Level level, ClientboundCBCExplodePacket packet) {
		super(level, packet);
	}

	@Override
	public void playLocalSound(Level level, double x, double y, double z) {
		CBCSoundEvents.SMOKE_SHELL_DETONATE.playAt(level, x, y, z, 2, 1.5f + level.random.nextFloat(0.1f), false);
	}

	@Override
	public void sendExplosionToClient(ServerPlayer player) {
		if (player.distanceToSqr(this.x, this.y, this.z) < 4096.0d) {
			Vec3 knockback = this.getHitPlayers().getOrDefault(player, Vec3.ZERO);
			NetworkPlatform.sendToClientPlayer(new ClientboundCBCExplodePacket(this.x, this.y, this.z, this.size, this.getToBlow(),
				(float) knockback.x, (float) knockback.y, (float) knockback.z, ClientboundCBCExplodePacket.ExplosionType.SMOKE), player);
		}
	}

	@Override protected void spawnParticles() {}

}
