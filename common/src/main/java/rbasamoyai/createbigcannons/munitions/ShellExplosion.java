package rbasamoyai.createbigcannons.munitions;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.block_hit_effects.BlockImpactTransformationHandler;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.explosions.ShellBlastWaveEffectParticleData;
import rbasamoyai.createbigcannons.effects.particles.explosions.ShellExplosionCloudParticleData;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;
import rbasamoyai.createbigcannons.remix.CustomExplosion;

public class ShellExplosion extends CustomExplosion.Impl {

	private final Set<BlockPos> changedBlocks = new HashSet<>();
	private final boolean isPlume;
	private final boolean noEffects;

	public ShellExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, double toBlowX,
						  double toBlowY, double toBlowZ, float radius, boolean fire,  Level.ExplosionInteraction  interaction, boolean noEffects) {
		super(level, source, damageSource, null, toBlowX, toBlowY, toBlowZ, radius, fire, interaction);
		BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
		this.isPlume = this.level.getBlockState(pos.above()).isAir() && !this.level.getBlockState(pos.below()).isAir();
		this.noEffects = noEffects;
	}

	public ShellExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, double toBlowX,
						  double toBlowY, double toBlowZ, float radius, boolean fire, Level.ExplosionInteraction interaction) {
		this(level, source, damageSource, toBlowX, toBlowY, toBlowZ, radius, fire, interaction, false);
	}

	public ShellExplosion(Level level, ClientboundCBCExplodePacket packet) {
		super(level, packet);
		BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
		this.isPlume = this.level.getBlockState(pos.above()).isAir() && !this.level.getBlockState(pos.below()).isAir();
		this.noEffects = packet.explosionType() == ClientboundCBCExplodePacket.ExplosionType.SHELL_NO_EFFECTS;
	}

	@Override
	protected void spawnParticles() {
		if (this.noEffects)
			return;
		ShellBlastWaveEffectParticleData blastWave = new ShellBlastWaveEffectParticleData(this.size * 12,
			BuiltInRegistries.SOUND_EVENT.wrapAsHolder(CBCSoundEvents.SHELL_EXPLOSION.getMainEvent()), SoundSource.BLOCKS,
			Math.max(this.size * 2, 16), 0.8f + level.random.nextFloat() * 0.4f, 2f, this.size);
		ShellExplosionCloudParticleData explosionCloud = new ShellExplosionCloudParticleData(this.size, this.isPlume);
		this.level.addParticle(blastWave, true, this.x, this.y, this.z, 0, 0, 0);
		this.level.addParticle(explosionCloud, true, this.x, this.y, this.z, 0, 0, 0);
	}

	@Override
	public void editBlock(Level level, BlockPos pos, BlockState blockState, FluidState fluidState, float power) {
		if (this.noEffects || !CBCConfigs.SERVER.munitions.projectilesChangeSurroundings.get() || this.changedBlocks.contains(pos))
			return;
		BlockState transformed = BlockImpactTransformationHandler.transformBlock(blockState);
		level.setBlock(pos, transformed, 11);
		this.changedBlocks.add(pos);
	}

	@Override
	public void sendExplosionToClient(ServerPlayer player) {
		double distSqr = player.distanceToSqr(this.x, this.y, this.z);
		if (distSqr < 263000d) {
			Vec3 knockback = this.getHitPlayers().getOrDefault(player, Vec3.ZERO);
			ClientboundCBCExplodePacket.ExplosionType type = this.noEffects
				? ClientboundCBCExplodePacket.ExplosionType.SHELL_NO_EFFECTS
				: ClientboundCBCExplodePacket.ExplosionType.SHELL;
			NetworkPlatform.sendToClientPlayer(new ClientboundCBCExplodePacket(this.x, this.y, this.z, this.size, this.getToBlow(),
				(float) knockback.x, (float) knockback.y, (float) knockback.z, type), player);
		}
	}

}
