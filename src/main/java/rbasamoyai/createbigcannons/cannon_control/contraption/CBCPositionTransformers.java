package rbasamoyai.createbigcannons.cannon_control.contraption;

import java.util.List;
import java.util.function.BiFunction;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

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

}
