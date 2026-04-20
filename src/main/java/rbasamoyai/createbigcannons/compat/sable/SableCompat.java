package rbasamoyai.createbigcannons.compat.sable;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCCompatTransformers;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class SableCompat {

    public static BlockPos transformFromShip(Level level, BlockPos pos) {
        return BlockPos.containing(Sable.HELPER.projectOutOfSubLevel(level, Vec3.atCenterOf(pos)));
    }

    public static Vec3 transformFromShip(Level level, Vec3 pos) {
        return Sable.HELPER.projectOutOfSubLevel(level, pos);
    }

    public static Vec3 transformNormalFromShip(Level level, BlockHitResult result, Vec3 normal) {
        SubLevel sublevel = Sable.HELPER.getContaining(level, result.getBlockPos());
        return sublevel == null ? normal : sublevel.logicalPose().transformNormal(normal);
    }

    public static boolean groundProjectile(Level level, AbstractCannonProjectile projectile, BlockPos impactPos) {
        SubLevel sublevel = Sable.HELPER.getContaining(level, impactPos);
        if (sublevel == null)
            return false;
        Vec3 projPos = projectile.position();
        Pose3d pose = sublevel.logicalPose();
        Vec3 shipPos = pose.transformPositionInverse(projPos);
        Vec3 nudge = Vec3.atCenterOf(impactPos).subtract(shipPos).scale(0.05);
        projectile.setPos(shipPos.add(nudge));
        Vec3 orientation = projectile.getOrientation();
        projectile.setOrientation(pose.transformNormalInverse(pose.transformNormalInverse(orientation)));
        return true;
    }

    public static void init() {
        CBCCompatTransformers.addBlockPosTransformer(SableCompat::transformFromShip);
        CBCCompatTransformers.addVec3Transformer(SableCompat::transformFromShip);
        CBCCompatTransformers.addNormalTransformer(SableCompat::transformNormalFromShip);
        CBCCompatTransformers.addProjectileFallHandler(new SableCannonProjectileCompat.ProjectileFallHandler());
        CBCCompatTransformers.addProjectileGroundingHandler(SableCompat::groundProjectile);
    }

}
