package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Unique private final Entity self = (Entity) (Object) this;

	@Inject(method = "turn", at = @At("HEAD"), cancellable = true)
	public void createbigcannons$turn(double yaw, double pitch, CallbackInfo ci) {
		if (this.self.getVehicle() instanceof PitchOrientedContraptionEntity poce) {
			float crot = poce.getRotationCoefficient();
			float dxr = (float) pitch * crot;
			float dyr = (float) yaw * crot;
			this.self.setXRot(this.self.getXRot() + dxr);
			this.self.setYRot(this.self.getYRot() + dyr);

			float e = poce.maximumDepression();
			float d = -poce.maximumElevation();
			this.self.setXRot(Mth.clamp(this.self.getXRot(), d, e));
			this.self.xRotO += dxr;
			this.self.yRotO += dyr;
			this.self.xRotO = Mth.clamp(this.self.xRotO, d, e);

			self.getVehicle().onPassengerTurned(this.self);
			if (ci.isCancellable()) ci.cancel();
		}
	}

	@Inject(method = "makeBoundingBox", at = @At("HEAD"), cancellable = true)
	private void createbigcannons$makeBoundingBox(CallbackInfoReturnable<AABB> cir) {
		if (!this.onAutocannon()) return;
		float w = this.self.getBbWidth() / 2;
		cir.setReturnValue(new AABB(-w, -w, -w, w, w, w).move(this.self.position()));
	}

	@Inject(method = "getEyeHeight()F", at = @At("TAIL"), cancellable = true)
	private void createbigcannons$getEyeHeight(CallbackInfoReturnable<Float> cir) {
		if (this.onAutocannon())
			cir.setReturnValue(this.self.getBbWidth() * 0.425f);
	}

	@Inject(method = "getEyePosition(F)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
	private void createbigcannons$getEyePosition(float partialTicks, CallbackInfoReturnable<Vec3> cir) {
		if (!this.onAutocannon() || !(this.self instanceof Player player)) return;
		PitchOrientedContraptionEntity poce = (PitchOrientedContraptionEntity) this.self.getVehicle();

		Direction dir = poce.getInitialOrientation();
		Vec3 normal = new Vec3(dir.step());
		Direction up = Direction.UP; // TODO: up and down cases

		Vec3 upNormal = new Vec3(up.step());
		Vec3 localPos = Vec3.atCenterOf(poce.getSeatPos(player));
		localPos = localPos.add(upNormal.scale(0.35));
		Vec3 eyePos = poce.toGlobalVector(localPos, partialTicks);
		cir.setReturnValue(eyePos);
	}

	@Unique
	private boolean onAutocannon() {
		return this.self.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(this.self) != null;
	}

//	@Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInLava()Z", ordinal = 1))
//	private void createbigcannons$baseTick(CallbackInfo ci) {
//		Entity self = (Entity) (Object) this;
//		self.updateFluidHeightAndDoFluidPushing(CBCTags.FluidCBC.MOLTEN_METAL, 0.0023333333333333335);
//		if (this.inMoltenMetal() && !self.fireImmune()) {
//			self.setSecondsOnFire(15);
//			if (self.hurt(MOLTEN_METAL, 4.0F)) {
//				self.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + self.getLevel().random.nextFloat() * 0.4F);
//			}
//			this.fallDistance *= 0.5;
//		}
//	}
//
//	@Unique
//	private boolean inMoltenMetal() {
//		Entity self = (Entity) (Object) this;
//		return !this.firstTick && self.getFluidHeight(CBCTags.FluidCBC.MOLTEN_METAL) > 0.0;
//	}

}
