package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow private Entity entity;

    @Shadow
    private float partialTickTime;

    @WrapOperation(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V"))
    private void createbigcannons$setup(Camera instance, double x, double y, double z, Operation<Void> original) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || this.entity != player || !(player.getVehicle() instanceof PitchOrientedContraptionEntity poce)
            || poce.getSeatPos(player) == null || mc.options.getCameraType() != CameraType.FIRST_PERSON) {
            original.call(instance, x, y, z);
            return;
        }

        Direction orientation = poce.getInitialOrientation();
        Direction up;
        if (orientation.getAxis().isHorizontal()) {
            up = Direction.UP;
        } else if (orientation == Direction.DOWN) {
            up = Direction.NORTH;
        } else { // orientation == Direction.UP
            up = Direction.SOUTH;
        }

        Vec3 upNormal = new Vec3(up.step());
        Vec3 localPos = Vec3.atCenterOf(poce.getSeatPos(player));
        localPos = localPos.add(upNormal.scale(0.35));
        Vec3 rotationOffset = VecHelper.getCenterOf(BlockPos.ZERO);
        Vec3 anchor = poce.getPrevAnchorVec().lerp(poce.getAnchorVec(), this.partialTickTime);

        Vec3 camPos = localPos.subtract(rotationOffset);
        camPos = poce.applyRotation(camPos, this.partialTickTime);
        camPos = camPos.add(anchor).add(rotationOffset);
        original.call(instance, camPos.x, camPos.y, camPos.z);
    }

}
