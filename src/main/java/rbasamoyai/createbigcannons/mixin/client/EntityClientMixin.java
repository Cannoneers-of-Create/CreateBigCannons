package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;

@Mixin(Entity.class)
public abstract class EntityClientMixin {

	@Unique private final Entity self = (Entity) (Object) this;

    @WrapMethod(method = "turn")
    public void createbigcannons$turn(double yaw, double pitch, Operation<Void> original) {
        Minecraft mc = Minecraft.getInstance();
        boolean playerIsCamera = mc.getCameraEntity() == mc.player;
        if (playerIsCamera && this.self.getVehicle() instanceof PitchOrientedContraptionEntity poce) {
            if (poce.getController() instanceof FixedCannonMountBlockEntity)
                return;
            float crot = poce.getRotationCoefficient();
            float dxr = (float) pitch * crot;
            float dyr = (float) yaw * crot;
            this.self.setXRot(this.self.getXRot() + dxr);
            this.self.setYRot(this.self.getYRot() + dyr);

            Direction dir = poce.getInitialOrientation();
            boolean isHorizontal = dir.getAxis().isHorizontal();
            float d = isHorizontal ? -poce.maximumElevation() : -poce.maximumDepression();
            float e = isHorizontal ? poce.maximumDepression() : poce.maximumElevation();
            this.self.setXRot(Mth.clamp(this.self.getXRot(), d, e));
            this.self.xRotO += dxr;
            this.self.yRotO += dyr;
            this.self.xRotO = Mth.clamp(this.self.xRotO, d, e);

            poce.onPassengerTurned(this.self);
        } else {
            original.call(yaw, pitch);
        }
    }

}
