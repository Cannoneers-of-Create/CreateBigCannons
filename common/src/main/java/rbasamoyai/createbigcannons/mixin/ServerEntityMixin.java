package rbasamoyai.createbigcannons.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import rbasamoyai.createbigcannons.base.PreciseProjectile;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundPreciseMotionSyncPacket;

import java.util.List;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@Shadow @Final private Entity entity;

	@Inject(method = "sendChanges", at = @At(value = "JUMP", opcode = Opcodes.IFNULL, ordinal = 2), locals = LocalCapture.CAPTURE_FAILHARD)
	private void createbigcannons$sendChanges1(CallbackInfo ci, List list, int l, int k1, Vec3 vec3, boolean flag2, Packet packet) {
		if (this.entity instanceof PreciseProjectile) {
			Vec3 pos = this.entity.position();
			Vec3 vel = this.entity.getDeltaMovement();
			NetworkPlatform.sendToClientTracking(new ClientboundPreciseMotionSyncPacket(this.entity.getId(), pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, this.entity.getYRot(), this.entity.getXRot(), this.entity.isOnGround()), this.entity);
			packet = null;
		}
	}

}
