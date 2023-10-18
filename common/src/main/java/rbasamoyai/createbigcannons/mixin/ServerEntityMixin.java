package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundPreciseRotationSyncPacket;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@Shadow @Final private Entity entity;

	@Inject(method = "sendChanges", at = @At("HEAD"))
	private void createbigcannons$sendChanges1(CallbackInfo ci) {
		if (this.entity instanceof PitchOrientedContraptionEntity) {
			Vec3 pos = this.entity.position();
			NetworkPlatform.sendToClientTracking(new ClientboundPreciseRotationSyncPacket(this.entity.getId(), this.entity.getYRot(), this.entity.getXRot()), this.entity);
			this.entity.hasImpulse = false;
		}
	}

}
