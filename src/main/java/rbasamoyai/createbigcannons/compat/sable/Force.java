package rbasamoyai.createbigcannons.compat.sable;

import dev.ryanhcode.sable.api.physics.force.ForceGroup;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public record Force(UUID sublevelId,
                    Vec3 pos,
                    Vec3 force,
                    int remainSteps,
                    ForceGroup forceGroup) {
    public Force nextStep() {
        return new Force(this.sublevelId,
                        this.pos,
                        this.force,
                        this.remainSteps - 1,
                        this.forceGroup);
    }
}
