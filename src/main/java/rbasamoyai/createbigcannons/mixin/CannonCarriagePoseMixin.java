package rbasamoyai.createbigcannons.mixin;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageEntity;

@Mixin(HumanoidModel.class)
public abstract class CannonCarriagePoseMixin extends AgeableListModel {

    @Inject(method = "Lnet/minecraft/client/model/HumanoidModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    public <T extends LivingEntity> void setupAnim(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (livingEntity.getVehicle() instanceof CannonCarriageEntity carriage && !carriage.isCannonRider()) this.riding = false;
    }

}
