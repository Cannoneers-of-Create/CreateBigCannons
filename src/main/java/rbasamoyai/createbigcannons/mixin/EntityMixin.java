package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract float getEyeHeight();

    @Unique private final Entity self = (Entity) (Object) this;

	@WrapMethod(method = "makeBoundingBox")
	private AABB createbigcannons$makeBoundingBox(Operation<AABB> original) {
		if (this.onAutocannon()) {
            float w = this.self.getBbWidth() / 2;
            return new AABB(-w, -w, -w, w, w, w).move(this.self.position());
        } else {
            return original.call();
        }
    }

    @WrapMethod(method = "getEyeHeight()F")
    private float createbigcannons$getEyeHeight(Operation<Float> original) {
        return this.onAutocannon() ? this.self.getBbWidth() * 0.425f : original.call();
    }

    @WrapMethod(method = "getEyeY")
    private double createbigcannons$getEyeY(Operation<Double> original) {
        return this.onAutocannon() ? this.self.getY() + this.self.getBbWidth() * 0.425d : original.call();
    }

    @WrapMethod(method = "getY(D)D")
    private double createbigcannons$getY(double scale, Operation<Double> original) {
        return this.onAutocannon() ? this.self.getY() + this.self.getBbWidth() * (scale - 1) : original.call(scale);
    }

    @WrapMethod(method = "getEyePosition(F)Lnet/minecraft/world/phys/Vec3;")
    private Vec3 createbigcannons$getEyePosition(float partialTicks, Operation<Vec3> original) {
        Vec3 o = original.call(partialTicks);
        if (!this.onAutocannon() || !(this.self instanceof Player))
            return o;
        PitchOrientedContraptionEntity poce = (PitchOrientedContraptionEntity) this.self.getVehicle();

        //Direction dir = poce.getInitialOrientation();
        //Vec3 normal = new Vec3(dir.step());
        Direction up = Direction.UP; // TODO: up and down cases

        Vec3 upNormal = new Vec3(up.step()).scale(0.35 - this.getEyeHeight()); // Undo eye height
        upNormal = poce.applyRotation(upNormal, 0);
        return o.add(upNormal);
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
