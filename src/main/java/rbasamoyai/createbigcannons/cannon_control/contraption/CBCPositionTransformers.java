package rbasamoyai.createbigcannons.cannon_control.contraption;

import java.util.List;
import java.util.function.BiFunction;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CBCPositionTransformers {

    private static final List<BlockPosTransformer> BLOCK_POS = new ReferenceArrayList<>();

    public static void addBlockPosTransformer(BlockPosTransformer transformer) {
        BLOCK_POS.add(transformer);
    }

    public static BlockPos transformBlockPos(Level level, BlockPos pos) {
        for (BlockPosTransformer t : BLOCK_POS) {
            pos = t.apply(level, pos);
        }
        return pos;
    }

    @FunctionalInterface
    public interface BlockPosTransformer extends BiFunction<Level, BlockPos, BlockPos> {
    }

    private static final List<Vec3Transformer> VEC3_POS = new ReferenceArrayList<>();

    public static void addVec3Transformer(Vec3Transformer transformer) {
        VEC3_POS.add(transformer);
    }

    public static Vec3 transformVec3(Level level, Vec3 pos) {
        for (Vec3Transformer t : VEC3_POS) {
            pos = t.apply(level, pos);
        }
        return pos;
    }

    @FunctionalInterface
    public interface Vec3Transformer extends BiFunction<Level, Vec3, Vec3> {
    }

}
