package rbasamoyai.createbigcannons.cannonloading;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonLoaderGen extends SpecialBlockStateGen {
	
	@Override
	public <T extends Block> ModelFile getModel(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider, BlockState state) {
		boolean axisAlongFirst = state.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
		boolean rotated = state.getValue(CannonLoaderBlock.FACING).getAxis() == Direction.Axis.X ^ axisAlongFirst;
		int suf = (state.getValue(CannonLoaderBlock.MOVING) ? 0 : 1) | (rotated ? 0 : 2);
		return provider.models().getExistingFile(CreateBigCannons.resource("block/cannon_loader" + suf));
	}

	@Override
	protected int getXRotation(BlockState state) {
		Direction facing = state.getValue(CannonLoaderBlock.FACING);
		return facing.getAxis().isVertical() ? facing == Direction.DOWN ? 180 : 0 : 90;
	}

	@Override
	protected int getYRotation(BlockState state) {
		Direction facing = state.getValue(CannonLoaderBlock.FACING);
		return facing.getAxis().isVertical() ? 0 : horizontalAngle(facing) + 180;
	}

}
