package rbasamoyai.createbigcannons.crafting.incomplete;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class IncompleteScrewBreechBlockGen extends SpecialBlockStateGen {

	@ExpectPlatform public static IncompleteScrewBreechBlockGen create(String pathAndMaterial) { throw new AssertionError(); }

	protected final String pathAndMaterial;

	protected IncompleteScrewBreechBlockGen(String pathAndMaterial) {
		this.pathAndMaterial = pathAndMaterial;
	}

	@Override
	protected int getXRotation(BlockState state) {
		Direction facing = state.getValue(BlockStateProperties.FACING);
		return facing.getAxis().isVertical() ? facing == Direction.DOWN ? 180 : 0 : 90;
	}

	@Override
	protected int getYRotation(BlockState state) {
		Direction facing = state.getValue(BlockStateProperties.FACING);
		return facing.getAxis().isVertical() ? 0 : this.horizontalAngle(facing) + 180;
	}

}
