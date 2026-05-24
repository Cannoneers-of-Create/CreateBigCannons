package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;

@Mixin(HumanoidModel.class)
public abstract class HumanoidPoseMixin extends AgeableListModel {

    @WrapMethod(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    public <T extends LivingEntity> void createbigcannons$setupAnimHead(T entity, float limbSwing, float limbSwingAmount,
                                                                        float ageInTicks, float netHeadYaw, float headPitch,
                                                                        Operation<Void> original) {
        Entity vehicle = entity.getVehicle();
        if (vehicle instanceof CannonCarriageEntity carriage && !carriage.isCannonRider())
            this.riding = false;

        original.call(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        HumanoidModel<?> self = (HumanoidModel<?>) (Object) this;
        if (CBCEntityTypes.PITCH_ORIENTED_CONTRAPTION.is(entity.getVehicle())) {
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
