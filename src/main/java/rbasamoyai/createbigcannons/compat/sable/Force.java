package rbasamoyai.createbigcannons.compat.sable;

import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public record Force(UUID sublevelId,
                    Vec3 pos,
                    Vec3 force,
                    int remainSteps) {
    public Force nextStep() {
        return new Force(this.sublevelId,
                        this.pos,
                        this.force,
                        this.remainSteps - 1);
    }
}
