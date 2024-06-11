package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import javax.annotation.Nullable;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundFluidExplodePacket;
import rbasamoyai.createbigcannons.remix.CustomExplosion;

public class FluidExplosion extends CustomExplosion.Impl {

	private final Fluid fluid;
	private final float radius;

	public FluidExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, double toBlowX,
						  double toBlowY, double toBlowZ, float radius, BlockInteraction interaction, Fluid fluid) {
		super(level, source, damageSource, null, toBlowX, toBlowY, toBlowZ, radius, false, interaction);
		this.fluid = fluid;
		this.radius = radius;
	}

	public FluidExplosion(Level level, ClientboundFluidExplodePacket packet) {
		super(level, packet.asCBCExplodePacket());
		this.fluid = packet.fluid();
		this.radius = packet.power();
	}

	@Override protected void spawnParticles() {} // Unused, particle effects done in registered OnFluidShellExplode in playLocalSound

	@Override
	public void playLocalSound(Level level, double x, double y, double z) {
		if (FluidBlobEffectRegistry.effectOnFluidShellExplode(this.fluid, level, x, y, z, this.radius))
			return;
		level.playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F,
			(1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F, false);
		if (this.radius >= 2.0F && this.interaction != Explosion.BlockInteraction.NONE) {
			level.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0, 0.0, 0.0);
		} else {
			level.addParticle(ParticleTypes.EXPLOSION, x, y, z, 1.0, 0.0, 0.0);
		}
	}

	@Override
	public void sendExplosionToClient(ServerPlayer player) {
		if (player.distanceToSqr(this.x, this.y, this.z) < 16400.0d) {
			Vec3 knockback = this.getHitPlayers().getOrDefault(player, Vec3.ZERO);
			NetworkPlatform.sendToClientPlayer(new ClientboundFluidExplodePacket(this.x, this.y, this.z, this.size, this.getToBlow(),
				(float) knockback.x, (float) knockback.y, (float) knockback.z, this.fluid), player);
		}
	}

}
