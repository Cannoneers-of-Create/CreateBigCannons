package rbasamoyai.createbigcannons.mixin.compat.create;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.phys.Vec3;

/**
 * Create 6.0.x continuous OBB collision can leave {@code axis == null} after SAT
 * {@code separate()} passes, then NPE when resolving discrete overlaps.
 * <p>
 * Hook the package-private manifold at {@code getTimeOfImpact()} (after all separates,
 * before axis is consumed) and back-fill {@code axis}/{@code separation}.
 */
@Mixin(targets = "com.simibubi.create.foundation.collision.ContinuousOBBCollider$ContinuousSeparationManifold", remap = false)
public abstract class ContinuousSeparationManifoldMixin {

	/** Same sentinel Create assigns in {@code reset()}. */
	@Unique
	private static final double UNDEFINED_SEPARATION = Double.MAX_VALUE;

	@Shadow
	@Final
    Vec3 stepSeparationAxis;

	@Shadow
	@Nullable
    Vec3 axis;

	@Shadow
	@Nullable
    Vec3 normalAxis;

	@Shadow
    double separation;

	@Shadow
    double normalSeparation;

	@Shadow
    double stepSeparation;

	@Inject(method = "getTimeOfImpact", at = @At("HEAD"))
	private void createbigcannons$fillMissingAxis(CallbackInfoReturnable<Double> cir) {
		if (this.axis != null)
			return;

		boolean hasNormal = this.normalAxis != null;
		boolean hasStep = this.stepSeparation < UNDEFINED_SEPARATION;

		if (hasNormal && hasStep) {
			if (Math.abs(this.normalSeparation) <= Math.abs(this.stepSeparation)) {
				this.axis = this.normalAxis;
				this.separation = this.normalSeparation;
			} else {
				this.axis = this.stepSeparationAxis;
				this.separation = this.stepSeparation;
			}
			return;
		}
		if (hasNormal) {
			this.axis = this.normalAxis;
			this.separation = this.normalSeparation;
			return;
		}
		if (hasStep) {
			this.axis = this.stepSeparationAxis;
			this.separation = this.stepSeparation;
			return;
		}

		// No usable SAT result: keep resolution defined but apply zero push.
		this.axis = this.stepSeparationAxis;
		this.separation = 0.0d;
	}

}
