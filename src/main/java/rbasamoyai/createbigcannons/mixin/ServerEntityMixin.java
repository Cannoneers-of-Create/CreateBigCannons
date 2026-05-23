package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundPreciseRotationSyncPacket;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@Shadow @Final private Entity entity;

	@WrapMethod(method = "sendChanges")
	private void createbigcannons$sendChanges(Operation<Void> original) {
		if (this.entity instanceof PitchOrientedContraptionEntity poce) {
			if (poce.getControllingPassenger() == null) {
				NetworkPlatform.sendToClientTracking(new ClientboundPreciseRotationSyncPacket(this.entity.getId(), this.entity.getYRot(), this.entity.getXRot()), this.entity);
			}
			this.entity.hasImpulse = false;
		}
        original.call();
	}

}
