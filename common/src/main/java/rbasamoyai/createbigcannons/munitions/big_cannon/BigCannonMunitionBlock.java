package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public interface BigCannonMunitionBlock {

	default BlockState onCannonRotate(BlockState oldState, Direction.Axis rotationAxis, Rotation rotation) {
		if (oldState.hasProperty(BlockStateProperties.FACING)) {
			Direction facing = oldState.getValue(BlockStateProperties.FACING);
			for (int i = 0; i < rotation.ordinal(); ++i) {
				facing = facing.getClockWise(rotationAxis);
			}
			return oldState.setValue(BlockStateProperties.FACING, facing);
		}
		if (oldState.hasProperty(BlockStateProperties.AXIS)) {
			Direction.Axis axis = oldState.getValue(BlockStateProperties.AXIS);
			if (axis == rotationAxis) return oldState;
			for (int i = 0; i < rotation.ordinal(); ++i) {
				axis = switch (axis) {
					case X -> rotationAxis == Direction.Axis.Y ? Direction.Axis.Z : Direction.Axis.Y;
					case Y -> rotationAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
					case Z -> rotationAxis == Direction.Axis.Y ? Direction.Axis.X : Direction.Axis.Y;
				};
			}
			return oldState.setValue(BlockStateProperties.AXIS, axis);
		}
		return oldState;
	}

}
