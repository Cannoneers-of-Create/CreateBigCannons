package rbasamoyai.createbigcannons.mixin;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.cannon_control.carriage.AbstractCannonCarriageEntity;

@Mixin(HumanoidModel.class)
public abstract class HumanoidPoseMixin extends AgeableListModel {

    @Shadow @Final public ModelPart hat;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    public <T extends LivingEntity> void createbigcannons$setupAnimHead(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        Entity vehicle = livingEntity.getVehicle();
        if (vehicle instanceof AbstractCannonCarriageEntity carriage && !carriage.isCannonRider())
            this.riding = false;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public <T extends LivingEntity> void createbigcannons$setupAnimNearTail(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        HumanoidModel<?> self = (HumanoidModel<?>) (Object) this;
        if (CBCEntityTypes.PITCH_ORIENTED_CONTRAPTION.is(livingEntity.getVehicle())) {
            self.head.xRot = 0;
            self.hat.copyFrom(self.head);

            self.leftArm.xRot = -1.25f;
            self.leftArm.yRot = 0.25f;
            self.leftArm.zRot = 0;

            self.rightArm.xRot = -1.25f;
            self.rightArm.yRot = -0.25f;
            self.rightArm.zRot = 0;
        }
    }


}
