package rbasamoyai.createbigcannons.network;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;

public class CBCClientHandlers {

	public static void updateContraption(ClientboundUpdateContraptionPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		Entity entity = mc.level.getEntity(pkt.id());
		if (!(entity instanceof AbstractContraptionEntity ace)) return;
		Contraption contraption = ace.getContraption();
		if (contraption != null) {
			contraption.getBlocks().put(pkt.pos(), pkt.info());
			contraption.deferInvalidate = true;
		}
	}

	public static void animateCannon(ClientboundAnimateCannonContraptionPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level.getEntity(pkt.id()) instanceof AbstractPitchOrientedContraptionEntity poce) poce.handleAnimation();
	}

	public static void checkVersion(ClientboundCheckChannelVersionPacket pkt) {
		if (CBCRootNetwork.VERSION.equals(pkt.serverVersion())) return;
		Minecraft mc = Minecraft.getInstance();
		if (mc.getConnection() != null) mc.getConnection().onDisconnect(Components.literal("Create Big Cannons on the client uses a different network format than the server.")
				.append(" Please use a matching format."));
	}

	public static void syncPreciseMotion(ClientboundPreciseMotionSyncPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return;
		Entity entity = mc.level.getEntity(pkt.entityId());
		if (entity == null) return;

		entity.lerpTo(pkt.x(), pkt.y(), pkt.z(), pkt.yRot(), pkt.xRot(), 3, false);
		entity.setDeltaMovement(pkt.dx(), pkt.dy(), pkt.dz());
		entity.setOnGround(pkt.onGround());
	}
	
}
