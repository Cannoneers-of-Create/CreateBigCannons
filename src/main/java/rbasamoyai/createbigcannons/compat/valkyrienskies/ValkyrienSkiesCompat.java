package rbasamoyai.createbigcannons.compat.valkyrienskies;

import org.valkyrienskies.mod.common.VSGameUtilsKt;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.CBCPositionTransformers;

public class ValkyrienSkiesCompat {

    public static BlockPos transformFromShip(Level level, BlockPos pos) {
        return BlockPos.containing(VSGameUtilsKt.toWorldCoordinates(level, Vec3.atCenterOf(pos)));
    }

    public static void init() {
        CBCPositionTransformers.addBlockPosTransformer(ValkyrienSkiesCompat::transformFromShip);
    }

}
