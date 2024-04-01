package rbasamoyai.createbigcannons.cannonloading.forge;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_loading.CannonLoaderBlock;
import rbasamoyai.createbigcannons.cannon_loading.CannonLoaderGen;

public class CannonLoaderGenImpl extends CannonLoaderGen {

	public static CannonLoaderGen create() { return new CannonLoaderGenImpl(); }

	@Override
	public <T extends Block> ModelFile getModel(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider, BlockState state) {
		boolean axisAlongFirst = state.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
		boolean rotated = state.getValue(CannonLoaderBlock.FACING).getAxis() == Direction.Axis.X ^ axisAlongFirst;
		String suf = (state.getValue(CannonLoaderBlock.MOVING) ? "moving" : "stopped") + (rotated ? "_rotated" : "");
		return provider.models().getExistingFile(CreateBigCannons.resource("block/cannon_loader/" + suf));
	}

}
