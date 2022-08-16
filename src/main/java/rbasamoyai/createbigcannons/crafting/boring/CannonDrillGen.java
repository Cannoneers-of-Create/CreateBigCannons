package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.simibubi.create.repack.registrate.providers.DataGenContext;
import com.simibubi.create.repack.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonDrillGen extends SpecialBlockStateGen {
	
	@Override
	public <T extends Block> ModelFile getModel(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider, BlockState state) {
		boolean axisAlongFirst = state.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
		boolean rotated = state.getValue(CannonDrillBlock.FACING).getAxis() == Direction.Axis.X ^ axisAlongFirst;
		int base = switch (state.getValue(CannonDrillBlock.STATE)) {
			case RETRACTED -> 0;
			case MOVING -> 2;
			default -> 4;
		};
		int suf = base + (rotated ? 0 : 1);
		return provider.models().getExistingFile(CreateBigCannons.resource("block/cannon_drill" + suf));
	}

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
