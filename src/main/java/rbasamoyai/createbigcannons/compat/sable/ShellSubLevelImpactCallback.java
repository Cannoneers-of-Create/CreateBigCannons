package rbasamoyai.createbigcannons.compat.sable;

import org.joml.Vector3d;

import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class ShellSubLevelImpactCallback implements BlockSubLevelCollisionCallback {

    public static final ShellSubLevelImpactCallback INSTANCE = new ShellSubLevelImpactCallback();

    private ShellSubLevelImpactCallback() {}

    @Override
    public CollisionResult sable$onCollision(BlockPos blockPos, Vector3d hitPos, double impactVelocity) {
        if (!CBCConfigs.server().compat.sableFuzedProjectilesCanExplodeAsPhysicsObject.get())
            return CollisionResult.NONE;

        SubLevelPhysicsSystem system = SubLevelPhysicsSystem.getCurrentlySteppingSystem();
        ServerLevel level = system.getLevel();

        BlockState state = level.getBlockState(blockPos);
        if (!(state.getBlock() instanceof FuzedProjectileBlock<?,?> fuzedBlock))
            return CollisionResult.NONE;
        if (!(level.getBlockEntity(blockPos) instanceof FuzedBlockEntity fuzedBE))
            return CollisionResult.NONE;
        ItemStack fuze = fuzedBE.getFuze();
        if (!(fuze.getItem() instanceof FuzeItem fuzeItem))
            return CollisionResult.NONE;

        Direction shellFacing = state.getValue(FuzedProjectileBlock.FACING);
        Direction hitFace = fuzedBlock.isBaseFuze() ? shellFacing.getOpposite() : shellFacing;
        Vec3 hitDir = JOMLConversion.toMojang(hitPos).subtract(blockPos.getCenter());
        Direction closest = Direction.getNearest(hitDir.x, hitDir.y, hitDir.z);
        if (closest != hitFace)
            return CollisionResult.NONE;

        // Taken from CBC: Fuze Sable Fix by Zizazr
        BlockHitResult hitResult = new BlockHitResult(JOMLConversion.toMojang(hitPos), Direction.DOWN, blockPos, false);
        AbstractCannonProjectile.ImpactResult impactResult = new AbstractCannonProjectile.ImpactResult(
            AbstractCannonProjectile.ImpactResult.KinematicOutcome.STOP, false);

        if (fuzeItem.onBlockImpact(fuze, level, blockPos, state, hitResult, impactResult)) {
            fuzedBlock.detonateProjectileOnTheSpot(level, blockPos, state, shellFacing);
        } else {
            fuzedBE.setFuze(fuze);
        }
        return CollisionResult.NONE;
    }
}
