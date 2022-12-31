package rbasamoyai.createbigcannons.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow Entity vehicle;

	@Inject(method = "turn", at = @At("HEAD"), cancellable = true)
	public void createbigcannons$turn(double yaw, double pitch, CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		if (this.vehicle instanceof PitchOrientedContraptionEntity poce) {
			float crot = poce.getRotationCoefficient() * 0.15f;
			float clamp = PitchOrientedContraptionEntity.getRotationCap();
			float dxr = Mth.clamp((float) pitch * crot, -clamp, clamp);
			float dyr = Mth.clamp((float) yaw * crot, -clamp, clamp);
			self.setXRot(self.getXRot() + dxr);
			self.setYRot(self.getYRot() + dyr);
			self.setXRot(Mth.clamp(self.getXRot(), -90.0f, 90.0f));
			self.xRotO += dxr;
			self.yRotO += dyr;
			self.xRotO = Mth.clamp(self.xRotO, -90.0f, 90.0f);

			this.vehicle.onPassengerTurned(self);
			if (ci.isCancellable()) ci.cancel();
		}
	}

	@Inject(method = "makeBoundingBox", at = @At("HEAD"), cancellable = true)
	public void createbigcannons$makeBoundingBox(CallbackInfoReturnable<AABB> cir) {
		Entity self = (Entity) (Object) this;
		if (this.vehicle instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(self) != null) {
			Vec3 v = poce.toGlobalVector(Vec3.atCenterOf(poce.getSeatPos(self)), 1.0f);
			float w = this.getBbWidth() * 0.5f;
			cir.setReturnValue(new AABB(v.x - w, v.y - w, v.z - w, v.x + w, v.y + w, v.z + w));
		}
	}

	@Shadow public abstract float getBbWidth();

}