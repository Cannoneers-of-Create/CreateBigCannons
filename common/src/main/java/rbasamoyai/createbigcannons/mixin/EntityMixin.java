package rbasamoyai.createbigcannons.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;

import static rbasamoyai.createbigcannons.crafting.foundry.MoltenMetalLiquidBlock.MOLTEN_METAL;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow public float fallDistance;
	@Shadow protected boolean firstTick;

	@Inject(method = "turn", at = @At("HEAD"), cancellable = true)
	public void createbigcannons$turn(double yaw, double pitch, CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		if (self.getVehicle() instanceof AbstractPitchOrientedContraptionEntity poce) {
			float crot = poce.getRotationCoefficient();
			float dxr = (float) pitch * crot;
			float dyr = (float) yaw * crot;
			self.setXRot(self.getXRot() + dxr);
			self.setYRot(self.getYRot() + dyr);

			float e = poce.maximumDepression();
			float d = -poce.maximumElevation();
			self.setXRot(Mth.clamp(self.getXRot(), d, e));
			self.xRotO += dxr;
			self.yRotO += dyr;
			self.xRotO = Mth.clamp(self.xRotO, d, e);

			self.getVehicle().onPassengerTurned(self);
			if (ci.isCancellable()) ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "makeBoundingBox", cancellable = true)
	private void createbigcannons$makeBoundingBox(CallbackInfoReturnable<AABB> cir) {
		Entity self = (Entity) (Object) this;
		if (self.getVehicle() instanceof AbstractPitchOrientedContraptionEntity poce && poce.getSeatPos(self) != null) {
			Vec3 v = poce.toGlobalVector(Vec3.atCenterOf(poce.getSeatPos(self)), 1.0f);
			float w = self.getBbWidth() * 0.5f;
			cir.setReturnValue(new AABB(v.x - w, v.y - w, v.z - w, v.x + w, v.y + w, v.z + w));
		}
	}

	@Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInLava()Z", ordinal = 1))
	private void createbigcannons$baseTick(CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		self.updateFluidHeightAndDoFluidPushing(CBCTags.FluidCBC.MOLTEN_METAL, 0.0023333333333333335);
		if (this.inMoltenMetal() && !self.fireImmune()) {
			self.setSecondsOnFire(15);
			if (self.hurt(MOLTEN_METAL, 4.0F)) {
				self.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + self.getLevel().random.nextFloat() * 0.4F);
			}
			this.fallDistance *= 0.5;
		}
	}

	@Unique
	private boolean inMoltenMetal() {
		Entity self = (Entity) (Object) this;
		return !this.firstTick && self.getFluidHeight(CBCTags.FluidCBC.MOLTEN_METAL) > 0.0;
	}

}
