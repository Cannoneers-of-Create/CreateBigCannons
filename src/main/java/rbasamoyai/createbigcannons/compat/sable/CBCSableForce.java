package rbasamoyai.createbigcannons.compat.sable;

import java.util.UUID;

import dev.ryanhcode.sable.api.physics.force.ForceGroup;
import net.minecraft.world.phys.Vec3;

public record CBCSableForce(UUID sublevelId, Vec3 pos, Vec3 force, int remainSteps, ForceGroup forceGroup) {

    public CBCSableForce nextStep() {
        return new CBCSableForce(this.sublevelId, this.pos, this.force, this.remainSteps - 1, this.forceGroup);
    }

}
