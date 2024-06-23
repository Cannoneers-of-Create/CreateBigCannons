package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.base.SyncsExtraDataOnAdd;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundPreciseRotationSyncPacket;
import rbasamoyai.createbigcannons.network.ClientboundSyncExtraEntityDataPacket;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@Shadow @Final private Entity entity;

	@Inject(method = "sendChanges", at = @At("HEAD"))
	private void createbigcannons$sendChanges(CallbackInfo ci) {
		if (this.entity instanceof PitchOrientedContraptionEntity poce) {
			if (poce.getControllingPassenger() == null) {
				NetworkPlatform.sendToClientTracking(new ClientboundPreciseRotationSyncPacket(this.entity.getId(), this.entity.getYRot(), this.entity.getXRot()), this.entity);
			}
			this.entity.hasImpulse = false;
		}
	}

	@Inject(method = "addPairing",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerEntity;sendPairingData(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER))
	private void createbigcannons$addPairing(ServerPlayer player, CallbackInfo ci) {
		if (this.entity instanceof SyncsExtraDataOnAdd sync)
			NetworkPlatform.sendToClientPlayer(new ClientboundSyncExtraEntityDataPacket(this.entity.getId(), sync.addExtraSyncData()), player);
	}

}
