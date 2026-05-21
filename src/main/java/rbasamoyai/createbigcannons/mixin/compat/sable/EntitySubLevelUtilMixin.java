package rbasamoyai.createbigcannons.mixin.compat.sable;

import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.entity.EntitySubLevelUtil;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

@Mixin(EntitySubLevelUtil.class)
public class EntitySubLevelUtilMixin {

    @WrapOperation(method = "kickEntity", at = @At(value = "INVOKE", target = "Lorg/joml/Vector3d;mul(D)Lorg/joml/Vector3d;"))
    private static Vector3d createbigcannons$kickEntity$mul(Vector3d instance, double scalar, Operation<Vector3d> original,
                                                            @Local(argsOnly = true) Entity entity) {
        if (entity instanceof AbstractCannonProjectile && CBCConfigs.server().compat.sableProjectilesInheritPhysicsObjectVelocity.get())
            Sable.HELPER.getVelocity(entity.level(), JOMLConversion.toJOML(entity.position()), instance);
        return original.call(instance, scalar);
    }

    @WrapMethod(method = "kickEntity")
    private static void createbigcannons$kickEntity(SubLevel subLevel, Entity entity, Operation<Void> original) {
        original.call(subLevel, entity);
        if (entity instanceof AbstractCannonProjectile) {
            final Vec3 deltaMovement = entity.getDeltaMovement();
            final double horizontal = deltaMovement.horizontalDistance();
            entity.setYRot((float) (Mth.atan2(deltaMovement.x, deltaMovement.z) * 180.0 / (float) Math.PI));
            entity.setXRot((float) (Mth.atan2(deltaMovement.y, horizontal) * 180.0 / (float) Math.PI));
        }
    }

}
