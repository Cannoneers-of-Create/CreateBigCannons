package rbasamoyai.createbigcannons.network;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionRenderDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;

public class CBCClientHandlers {

	public static void updateContraption(ClientboundUpdateContraptionPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		Entity entity = mc.level.getEntity(pkt.id());
		if (!(entity instanceof AbstractContraptionEntity ace)) return;
		Contraption contraption = ace.getContraption();
		if (contraption != null) {
			contraption.getBlocks().put(pkt.pos(), pkt.info());
			ContraptionRenderDispatcher.invalidate(contraption);
		}
	}

	public static void animateCannon(ClientboundAnimateCannonContraptionPacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level.getEntity(pkt.id()) instanceof PitchOrientedContraptionEntity poce) poce.handleAnimation();
	}
	
}
