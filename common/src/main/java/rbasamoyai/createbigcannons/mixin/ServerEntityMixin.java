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
import rbasamoyai.createbigcannons.base.PreciseProjectile;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundPreciseMotionSyncPacket;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@Shadow @Final private Entity entity;

    @Inject(method = "sendChanges", at = @At("HEAD"))
    private void createbigcannons$sendChanges1(CallbackInfo ci) {
        if (this.entity instanceof PreciseProjectile) {
            Vec3 pos = this.entity.position();
            Vec3 vel = this.entity.getDeltaMovement();
            NetworkPlatform.sendToClientTracking(new ClientboundPreciseMotionSyncPacket(this.entity.getId(), pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, this.entity.getYRot(), this.entity.getXRot(), this.entity.isOnGround()), this.entity);
            this.entity.hasImpulse = false;
        }
    }

}
