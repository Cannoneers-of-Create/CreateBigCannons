package rbasamoyai.createbigcannons.compat.sable;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.force.ForceGroup;
import dev.ryanhcode.sable.api.physics.force.QueuedForceGroup;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.mixinterface.clip_overwrite.LevelPoseProviderExtension;
import dev.ryanhcode.sable.neoforge.event.ForgeSablePrePhysicsTickEvent;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;

import rbasamoyai.createbigcannons.CBCCompatTransformers;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SableCompat {

    private static final Map<ResourceKey<Level>, Queue<Force>> BY_DIMENSION = new ConcurrentHashMap<>();

    public static void enqueueForce(ServerLevel level, Vec3 pos, Vec3 force, int steps, ForceGroup forceGroup) {
        SubLevel subLevel = Sable.HELPER.getContaining(level, pos);
        if (subLevel == null) return;
        int safeSteps = Math.max(1, steps);
        Force forceData = new Force(
            subLevel.getUniqueId(),
            pos,
            force,
            safeSteps,
            forceGroup
        );
        enqueue(level.dimension(), forceData);
    }

    public static void enqueue(ResourceKey<Level> level, Force forceData) {
        BY_DIMENSION.computeIfAbsent(level, ignored -> new ConcurrentLinkedDeque<>()).add(forceData);
    }

    public static List<Force> drain(ResourceKey<Level> level) {
        Queue<Force> forces = BY_DIMENSION.get(level);
        if (forces == null || forces.isEmpty()) return List.of();
        List<Force> drained = new ArrayList<>();
        Force force;
        while ((force = forces.poll()) != null) {
            drained.add(force);
        }
        return drained;
    }

    public static BlockPos transformFromShip(Level level, BlockPos pos, BlockPos root) {
        // Adapted from ActiveSableCompanion; this implementation is mainly leveraged for cannon particle handling as
        // the cannon particle spawn points can be far away from the cannon mount. --ritchie
        SubLevel sublevel = Sable.HELPER.getContaining(level, root);
        if (sublevel == null)
            return pos;
        Pose3dc pose = level instanceof LevelPoseProviderExtension extension ? extension.sable$getPose(sublevel) : sublevel.logicalPose();
        return BlockPos.containing(pose.transformPosition(Vec3.atCenterOf(pos)));
    }

    public static Vec3 transformFromShip(Level level, Vec3 pos, Vec3 root) {
        // See the BlockPos version for commentary. --ritchie
        SubLevel sublevel = Sable.HELPER.getContaining(level, root);
        if (sublevel == null)
            return pos;
        Pose3dc pose = level instanceof LevelPoseProviderExtension extension ? extension.sable$getPose(sublevel) : sublevel.logicalPose();
        return pose.transformPosition(pos);
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
        CBCCompatTransformers.addNormalTransformer(new SableCannonProjectileCompat.NormalTransformer());
        CBCCompatTransformers.addProjectileFallHandler(new SableCannonProjectileCompat.ProjectileFallHandler());
        CBCCompatTransformers.addProjectileGroundingHandler(SableCompat::groundProjectile);
    }

    public static void recoilCannon(Level level, Vec3 pos, Vec3 direction, float power) {
        if (level instanceof ServerLevel serverLevel) {
            ForceGroup forceGroup = SableForceGroupsCompat.RECOIL.get();
            Vec3 force = direction.scale(power * -CBCConfigs.server().compats.recoilingFactor.get());
            enqueueForce(serverLevel, pos, force, 1, forceGroup);
        }
    }

    public static void impactContraption(Level level, HitResult hitResult, ProjectileContext projectileContext) {
        float massLost = projectileContext.projectile.massLost;
        if (level instanceof ServerLevel serverLevel) {
            Vec3 projDir = projectileContext.projectile.getDeltaMovement().normalize();
            Vec3 projLoc = hitResult.getLocation();
            SubLevel subLevel = Sable.HELPER.getContaining(level, projLoc);
            if (subLevel == null) return;

            Vec3 direction = subLevel.logicalPose().transformNormalInverse(projDir);
            ForceGroup forceGroup = SableForceGroupsCompat.IMPACT.get();
            double recoilingFactor = CBCConfigs.server().compats.recoilingFactor.get();
            double power;
            if (projectileContext.projectile instanceof AbstractAutocannonProjectile) { power = 0.5; }
            else { power = projectileContext.projectile.getDeltaMovement().length(); }
            Vec3 force = direction.scale(massLost * recoilingFactor * power);
            enqueueForce(serverLevel, projLoc, force, 1, forceGroup);
        }
    }

    @SubscribeEvent
    public static void onPrePhysicsTick(ForgeSablePrePhysicsTickEvent event) {
        ServerLevel level = event.getPhysicsSystem().getLevel();
        List<Force> forces = drain(level.dimension());
        if (forces.isEmpty()) return;

        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) return;

        for (Force force : forces) {
            SubLevel subLevel = container.getSubLevel(force.sublevelId());
            if (!(subLevel instanceof ServerSubLevel serverSubLevel) || serverSubLevel.isRemoved()) continue;
            QueuedForceGroup queuedForceGroup = serverSubLevel.getOrCreateQueuedForceGroup(force.forceGroup());
            queuedForceGroup.applyAndRecordPointForce(
                JOMLConversion.toJOML(force.pos()),
                JOMLConversion.toJOML(force.force())
            );

            if (force.remainSteps() > 1) enqueue(level.dimension(), force.nextStep());
        }
    }
}
