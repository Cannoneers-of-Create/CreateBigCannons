package rbasamoyai.createbigcannons.mixin;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;

@Mixin(HumanoidModel.class)
public abstract class HumanoidPoseMixin extends AgeableListModel {

    @Inject(method = "Lnet/minecraft/client/model/HumanoidModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    public <T extends LivingEntity> void createbigcannons$setupAnimHead(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        Entity vehicle = livingEntity.getVehicle();
        if (vehicle instanceof CannonCarriageEntity carriage && !carriage.isCannonRider())
            this.riding = false;
    }

    @Inject(method = "Lnet/minecraft/client/model/HumanoidModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public <T extends LivingEntity> void createbigcannons$setupAnimTail(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        HumanoidModel<?> self = (HumanoidModel<?>) (Object) this;
        if (CBCEntityTypes.PITCH_ORIENTED_CONTRAPTION.is(livingEntity.getVehicle())) {
            self.head.xRot = 0;
            self.leftArm.xRot = -1.25f;
            self.leftArm.yRot = 0.25f;
            self.leftArm.zRot = 0;

            self.rightArm.xRot = -1.25f;
            self.rightArm.yRot = -0.25f;
            self.rightArm.zRot = 0;
        }
    }


}
