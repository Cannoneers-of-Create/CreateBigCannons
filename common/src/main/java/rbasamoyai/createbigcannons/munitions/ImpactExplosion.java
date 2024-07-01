package rbasamoyai.createbigcannons.munitions;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_hit_effects.BlockImpactTransformationHandler;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;
import rbasamoyai.createbigcannons.remix.CustomExplosion;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ScreenShakeEffect;

public class ImpactExplosion extends CustomExplosion.Impl {

	private final Set<BlockPos> changedBlocks = new HashSet<>();

	public ImpactExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, double toBlowX,
						   double toBlowY, double toBlowZ, float radius, BlockInteraction interaction) {
		super(level, source, damageSource, null, toBlowX, toBlowY, toBlowZ, radius, false, interaction);
	}

	public ImpactExplosion(Level level, ClientboundCBCExplodePacket packet) {
		super(level, packet);
	}

	@Override
	public void editBlock(Level level, BlockPos pos, BlockState blockState, FluidState fluidState, float power) {
		if (!CBCConfigs.SERVER.munitions.projectilesChangeSurroundings.get() || this.changedBlocks.contains(pos))
			return;
		BlockState transformed = BlockImpactTransformationHandler.transformBlock(blockState);
		level.setBlock(pos, transformed, 11);
		this.changedBlocks.add(pos);
	}

	@Override
	public void sendExplosionToClient(ServerPlayer player) {
		double distSqr = player.distanceToSqr(this.x, this.y, this.z);
		if (distSqr < 10000.0d) {
			float f = Math.max(1f - (float) distSqr / 100f, 0);
			float f1 = this.size * f;
			float shake = Math.min(45, f1 * 4f);
			CreateBigCannons.shakePlayerScreen(player, new ScreenShakeEffect(0, shake, shake * 0.5f, shake * 0.5f, 1, 1, 1, this.x, this.y, this.z));
			Vec3 knockback = this.getHitPlayers().getOrDefault(player, Vec3.ZERO);
			NetworkPlatform.sendToClientPlayer(new ClientboundCBCExplodePacket(this.x, this.y, this.z, this.size, this.getToBlow(),
				(float) knockback.x, (float) knockback.y, (float) knockback.z, ClientboundCBCExplodePacket.ExplosionType.IMPACT), player);
		}
	}

}
