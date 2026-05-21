package rbasamoyai.createbigcannons.compat.sable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class CBCSableConnectivityUtils {

    // TODO: change to support different tall cannonmounts?

    public static boolean isCannonMountAirGap(BlockPos blockPos, Level level) {
        BlockState below = level.getBlockState(blockPos.below());
        if (below.is(CBCBlocks.CANNON_MOUNT) && below.getValue(CannonMountBlock.VERTICAL_DIRECTION) == Direction.DOWN)
            return true;
        BlockState above = level.getBlockState(blockPos.above());
        return above.is(CBCBlocks.CANNON_MOUNT) && above.getValue(CannonMountBlock.VERTICAL_DIRECTION) == Direction.UP;
    }

    public static List<BlockPos> getCannonMountGaps(BlockPos blockPos, Level level, Direction facing) {
        boolean isVertical = facing.getAxis().isVertical();
        Direction.Axis axis = facing.getAxis();
        List<BlockPos> airGaps = new ArrayList<>(2);
        BlockState twoBelow = level.getBlockState(blockPos.below(2));
        if (twoBelow.is(CBCBlocks.CANNON_MOUNT) && twoBelow.getValue(CannonMountBlock.VERTICAL_DIRECTION) == Direction.DOWN
            && (isVertical || twoBelow.getValue(CannonMountBlock.HORIZONTAL_FACING).getAxis() == axis))
            airGaps.add(blockPos.below());
        BlockState twoAbove = level.getBlockState(blockPos.above(2));
        if (twoAbove.is(CBCBlocks.CANNON_MOUNT) && twoAbove.getValue(CannonMountBlock.VERTICAL_DIRECTION) == Direction.UP
            && (isVertical || twoAbove.getValue(CannonMountBlock.HORIZONTAL_FACING).getAxis() == axis))
            airGaps.add(blockPos.above());
        return airGaps;
    }

    private CBCSableConnectivityUtils() {}

}
