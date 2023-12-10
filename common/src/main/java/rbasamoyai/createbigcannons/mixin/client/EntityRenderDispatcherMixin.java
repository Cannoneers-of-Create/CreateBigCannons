package rbasamoyai.createbigcannons.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.util.Mth;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z", ordinal = 0))
	private boolean createbigcannons$render$isInvisible(Entity instance) {
		return instance.isInvisible() || instance.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(instance) != null;
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;renderHitbox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;F)V"))
	private void createbigcannons$render$renderHitbox(Entity entity, double x, double y, double z, float rotationYaw,
													  float partialTicks, PoseStack matrixStack, MultiBufferSource buffer,
													  int packedLight, CallbackInfo ci) {
		if (entity.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(entity) != null) {
			float yr = (-Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 90) * Mth.DEG_TO_RAD;
			Vector3f vec3 = new Vector3f(Mth.sin(yr), 0, Mth.cos(yr));
			float xr = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
			Quaternion q = vec3.rotationDegrees(xr);
			q.conj();

			matrixStack.translate(0, 1.25, 0);
			matrixStack.mulPose(q);
		}
	}

}
