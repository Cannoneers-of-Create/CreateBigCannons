package rbasamoyai.createbigcannons.compat.sable;

import dev.ryanhcode.sable.Sable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.CBCPositionTransformers;

public class SableCompat {

    public static Vec3 transformFromShip(Level level, Vec3 pos) {
        return Sable.HELPER.projectOutOfSubLevel(level, pos);
    }

    public static void init() {
        CBCPositionTransformers.addVec3Transformer(SableCompat::transformFromShip);
    }

}
