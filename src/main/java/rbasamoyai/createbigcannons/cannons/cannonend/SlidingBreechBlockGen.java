package rbasamoyai.createbigcannons.cannons.cannonend;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class SlidingBreechBlockGen extends SpecialBlockStateGen {

	private final String pathAndMaterial;
	
	public SlidingBreechBlockGen(String pathAndMaterial) {
		this.pathAndMaterial = pathAndMaterial;
	}
	
	@Override
	public <T extends Block> ModelFile getModel(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider, BlockState state) {
		boolean axisAlongFirst = state.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
		boolean rotated = (state.getValue(BlockStateProperties.FACING).getAxis() == Direction.Axis.X) == axisAlongFirst;
		String suf = rotated ? "_rotated" : "";
		ResourceLocation baseLoc = CreateBigCannons.resource("block/sliding_breech" + suf);
		
		ResourceLocation holeLoc = CreateBigCannons.resource("block/" + this.pathAndMaterial + "_sliding_breech_hole");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + this.pathAndMaterial + "_sliding_breech_side");
		ResourceLocation sideHoleLoc = CreateBigCannons.resource("block/" + this.pathAndMaterial + "_sliding_breech_side_hole");
		ResourceLocation insideLoc = CreateBigCannons.resource("block/" + this.pathAndMaterial + "_sliding_breech_inside");
		return provider.models().withExistingParent(context.getName() + suf, baseLoc)
				.texture("hole", holeLoc)
				.texture("side", sideLoc)
				.texture("side_hole", sideHoleLoc)
				.texture("inside", insideLoc);
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
