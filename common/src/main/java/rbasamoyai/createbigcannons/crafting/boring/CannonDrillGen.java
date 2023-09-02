package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CannonDrillGen extends SpecialBlockStateGen {

	@ExpectPlatform public static CannonDrillGen create() { throw new AssertionError(); }

	@Override
	protected int getXRotation(BlockState state) {
		Direction facing = state.getValue(CannonDrillBlock.FACING);
		return facing.getAxis().isVertical() ? facing == Direction.DOWN ? 180 : 0 : 90;
	}

	@Override
	protected int getYRotation(BlockState state) {
		Direction facing = state.getValue(CannonDrillBlock.FACING);
		return facing.getAxis().isVertical() ? 0 : horizontalAngle(facing) + 180;
	}

}
