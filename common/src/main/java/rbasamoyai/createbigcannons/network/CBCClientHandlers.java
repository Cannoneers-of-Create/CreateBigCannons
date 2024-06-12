package rbasamoyai.createbigcannons.network;

import java.util.Map;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.utility.Components;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeExplosion;

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
				if (be == null || info.nbt == null) continue;
				CompoundTag copy = info.nbt.copy();
				copy.putInt("x", info.pos.getX());
				copy.putInt("y", info.pos.getY());
				copy.putInt("z", info.pos.getZ());
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

}
