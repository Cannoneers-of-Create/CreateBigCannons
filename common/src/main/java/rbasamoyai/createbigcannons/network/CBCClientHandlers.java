package rbasamoyai.createbigcannons.network;

import java.util.Map;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.utility.Components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.SyncsExtraDataOnAdd;
import rbasamoyai.createbigcannons.block_hit_effects.BackupBlockHitEffects;
import rbasamoyai.createbigcannons.block_hit_effects.BlockHitEffect;
import rbasamoyai.createbigcannons.block_hit_effects.BlockHitEffectsHandler;
import rbasamoyai.createbigcannons.block_hit_effects.ProjectileHitEffect;
import rbasamoyai.createbigcannons.block_hit_effects.ProjectileHitEffectsHandler;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.sounds.AirAbsorptionWrapper;
import rbasamoyai.createbigcannons.mixin.client.ClientLevelAccessor;
import rbasamoyai.createbigcannons.munitions.ImpactExplosion;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeExplosion;
import rbasamoyai.createbigcannons.remix.CustomBlockDamageDisplay;

public class CBCClientHandlers {

	public static void updateContraption(ClientboundUpdateContraptionPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		Entity entity = mc.level.getEntity(pkt.id());
		if (!(entity instanceof AbstractContraptionEntity ace)) return;
		Contraption contraption = ace.getContraption();
		if (contraption != null) {
			contraption.getBlocks().putAll(pkt.changes());
			for (Map.Entry<BlockPos, StructureBlockInfo> entry : pkt.changes().entrySet()) {
				BlockEntity be = contraption.presentBlockEntities.get(entry.getKey());
				StructureBlockInfo info = entry.getValue();
				if (be == null || info.nbt() == null) continue;
				CompoundTag copy = info.nbt().copy();
				copy.putInt("x", info.pos().getX());
				copy.putInt("y", info.pos().getY());
				copy.putInt("z", info.pos().getZ());
				be.load(copy);
			}
			contraption.deferInvalidate = true;
		}
	}

	public static void animateCannon(ClientboundAnimateCannonContraptionPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level.getEntity(pkt.id()) instanceof PitchOrientedContraptionEntity poce) poce.handleAnimation();
	}

	public static void checkVersion(ClientboundCheckChannelVersionPacket pkt) {
		if (CBCRootNetwork.VERSION.equals(pkt.serverVersion())) return;
		Minecraft mc = Minecraft.getInstance();
		if (mc.getConnection() != null)
			mc.getConnection().onDisconnect(Components.literal("Create Big Cannons on the client uses a different network format than the server.")
				.append(" Please use a matching format."));
	}

	public static void syncPreciseRotation(ClientboundPreciseRotationSyncPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;
		Entity entity = mc.level.getEntity(pkt.entityId());
		if (entity == null) return;

		entity.lerpTo(entity.getX(), entity.getY(), entity.getZ(), pkt.yRot(), pkt.xRot(), 3, false);
	}

	public static void updateFluidBlob(ClientboundFluidBlobStackSyncPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;
		Entity entity = mc.level.getEntity(pkt.entityId());
		if (entity instanceof FluidBlobBurst blobBurst) {
			blobBurst.setFluidStack(pkt.fstack());
		} else {
			CreateBigCannons.LOGGER.error("Invalid ClientboundFluidBlobStackSyncPacket for non-fluid blob burst entity: " + entity);
		}
	}

	public static void addExplosionFromServer(ClientboundCBCExplodePacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null)
			return;
		Explosion explosion = switch (pkt.explosionType()) {
            case SHRAPNEL -> new ShrapnelExplosion(mc.level, pkt);
            case FLAK -> new FlakExplosion(mc.level, pkt);
			case SMOKE -> new SmokeExplosion(mc.level, pkt);
            case MORTAR_STONE -> new MortarStoneExplosion(mc.level, pkt);
			case IMPACT -> new ImpactExplosion(mc.level, pkt);
			case SHELL, SHELL_NO_EFFECTS -> new ShellExplosion(mc.level, pkt);
        };
		explosion.finalizeExplosion(true);
		mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(pkt.knockbackX(), pkt.knockbackY(), pkt.knockbackZ()));
	}

	public static void addFluidExplosionFromServer(ClientboundFluidExplodePacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null)
			return;
		Explosion explosion = new FluidExplosion(mc.level, pkt);
		explosion.finalizeExplosion(true);
		mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(pkt.knockbackX(), pkt.knockbackY(), pkt.knockbackZ()));
	}

	public static void reloadTagDependentClientResources(ClientboundNotifyTagReloadPacket pkt) {
		BlockHitEffectsHandler.loadTags();
	}

	public static void playBlockHitEffect(ClientboundPlayBlockHitEffectPacket pkt) {
		BlockState blockState = pkt.blockState();
		boolean isLiquid = blockState.getBlock() instanceof LiquidBlock;
		if (isLiquid && !CBCConfigs.CLIENT.showProjectileSplashes.get() || !isLiquid && !CBCConfigs.CLIENT.showProjectileImpacts.get())
			return;
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null)
			return;
		BlockHitEffect blockEffect = isLiquid ? BlockHitEffectsHandler.getFluidProperties(blockState)
			: BlockHitEffectsHandler.getBlockProperties(blockState);
		ProjectileHitEffect projectileEffect = ProjectileHitEffectsHandler.getProperties(pkt.entityType());
		Vec3 vel = new Vec3(pkt.dx(), pkt.dy(), pkt.dz());
		double magnitude = projectileEffect.getMagnitude(vel);
		vel = vel.normalize().scale(magnitude);
		if (blockEffect == null) {
			if (isLiquid) {
				BackupBlockHitEffects.backupFluidEffect(minecraft.level, pkt.blockState(), pkt.deflect(), pkt.forceDisplay(),
					pkt.x(), pkt.y(), pkt.z(), vel.x, vel.y, vel.z, projectileEffect);
			} else {
				BackupBlockHitEffects.backupSolidEffect(minecraft.level, pkt.blockState(), pkt.deflect(), pkt.forceDisplay(),
					pkt.x(), pkt.y(), pkt.z(), vel.x, vel.y, vel.z, projectileEffect);
			}
		} else {
			blockEffect.playEffect(minecraft.level, pkt.deflect(), pkt.forceDisplay(), pkt.x(), pkt.y(), pkt.z(), vel.x,
				vel.y, vel.z, pkt.entityType(), pkt.blockState(), projectileEffect);
		}
	}

	public static void syncExtraEntityData(ClientboundSyncExtraEntityDataPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || !(mc.level.getEntity(pkt.entityId()) instanceof SyncsExtraDataOnAdd round))
			return;
		round.readExtraSyncData(pkt.data());
	}

	public static void setCustomBlockDamage(ClientboundSendCustomBreakProgressPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;
		ClientLevelAccessor clAccessor = (ClientLevelAccessor) mc.level;
		((CustomBlockDamageDisplay) clAccessor.getLevelRenderer()).createbigcannons$trackCustomProgress(pkt.pos(), pkt.damage());
	}

	public static void playBlastSound(ClientboundBlastSoundPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;
		SoundInstance sound = new SimpleSoundInstance(pkt.sound(), pkt.source(), pkt.volume(), pkt.pitch(), mc.level.random, pkt.x(), pkt.y(), pkt.z());
		if (CBCConfigs.CLIENT.blastSoundAirAbsorption.get())
			sound = new AirAbsorptionWrapper(sound, pkt.airAbsorption());
		if (!CBCConfigs.CLIENT.isInstantaneousBlastEffect() && mc.player != null) {
			double distSqr = mc.player.distanceToSqr(pkt.x(), pkt.y(), pkt.z());
			double timeInSec = Math.sqrt(distSqr) / CBCConfigs.CLIENT.blastEffectDelaySpeed.getF();
			mc.getSoundManager().playDelayed(sound, Mth.floor(timeInSec * 20));
		} else {
			mc.getSoundManager().play(sound);
		}
	}

}
