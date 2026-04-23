package rbasamoyai.createbigcannons;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

/**
 * Value transformers for mods such as Valkyrien Skies and Sable.
 */
public class CBCCompatTransformers {

    private static final List<BlockPosTransformer> BLOCK_POS = new ReferenceArrayList<>();

    public static void addBlockPosTransformer(BlockPosTransformer transformer) {
        BLOCK_POS.add(transformer);
    }

    public static BlockPos transformBlockPos(Level level, BlockPos pos, BlockPos root) {
        for (BlockPosTransformer t : BLOCK_POS) {
            pos = t.apply(level, pos, root);
        }
        return pos;
    }

    public static BlockPos transformBlockPos(Level level, BlockPos pos) {
        return transformBlockPos(level, pos, pos);
    }

    @FunctionalInterface
    public interface BlockPosTransformer {
        BlockPos apply(Level level, BlockPos pos, BlockPos root);
    }

    private static final List<Vec3Transformer> VEC3_POS = new ReferenceArrayList<>();

    public static void addVec3Transformer(Vec3Transformer transformer) {
        VEC3_POS.add(transformer);
    }

    public static Vec3 transformVec3(Level level, Vec3 pos, Vec3 root) {
        for (Vec3Transformer t : VEC3_POS) {
            pos = t.apply(level, pos, root);
        }
        return pos;
    }

    public static Vec3 transformVec3(Level level, Vec3 pos) {
        return transformVec3(level, pos, pos);
    }

    @FunctionalInterface
    public interface Vec3Transformer {
        Vec3 apply(Level level, Vec3 pos, Vec3 root);
    }

    private static final List<NormalTransformer> NORMAL = new ReferenceArrayList<>();

    public static void addNormalTransformer(NormalTransformer transformer) {
        NORMAL.add(transformer);
    }

    public static Vec3 transformHitNormal(Level level, BlockHitResult result, Vec3 normal) {
        for (NormalTransformer t : NORMAL) {
            normal = t.apply(level, result, normal);
        }
        return normal;
    }

    public static Vec3 transformLocationNormal(Level level, BlockPos loc, Vec3 normal) {
        for (NormalTransformer t : NORMAL) {
            normal = t.apply(level, loc, normal);
        }
        return normal;
    }

    public interface NormalTransformer {
        Vec3 apply(Level level, BlockPos loc, Vec3 normal);

        default Vec3 apply(Level level, BlockHitResult baseHitResult, Vec3 normal) {
            return this.apply(level, baseHitResult.getBlockPos(), normal);
        }
    }

    private static final List<ProjectileFallHandler> PROJECTILE_FALLING = new ReferenceArrayList<>();

    public static void addProjectileFallHandler(ProjectileFallHandler handler) {
        PROJECTILE_FALLING.add(handler);
    }

    public static boolean projectileShouldFall(Level level, AbstractCannonProjectile projectile, AABB collision) {
        for (ProjectileFallHandler t : PROJECTILE_FALLING) {
            if (!t.shouldFall(level, projectile, collision)) {
                return false;
            }
        }
        return true;
    }

    public static void projectileStartFalling(AbstractCannonProjectile projectile) {
        for (ProjectileFallHandler t : PROJECTILE_FALLING) {
            t.onFall(projectile);
        }
    }

    public interface ProjectileFallHandler {
        boolean shouldFall(Level level, AbstractCannonProjectile projectile, AABB collision);
        void onFall(AbstractCannonProjectile projectile);
    }

    private static final List<ProjectileGroundingHandler> PROJECTILE_GROUNDING = new ReferenceArrayList<>();

    public static void addProjectileGroundingHandler(ProjectileGroundingHandler handler) {
        PROJECTILE_GROUNDING.add(handler);
    }

    public static void groundProjectile(Level level, AbstractCannonProjectile projectile, BlockPos impactPos) {
        for (ProjectileGroundingHandler t : PROJECTILE_GROUNDING) {
            if (t.groundProjectile(level, projectile, impactPos))
                return;
        }
    }

    @FunctionalInterface
    public interface ProjectileGroundingHandler {
        boolean groundProjectile(Level level, AbstractCannonProjectile projectile, BlockPos impactPos);
    }

}
