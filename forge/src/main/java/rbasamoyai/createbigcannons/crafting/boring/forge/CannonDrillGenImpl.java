package rbasamoyai.createbigcannons.crafting.boring.forge;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillGen;

public class CannonDrillGenImpl extends CannonDrillGen {

	public static CannonDrillGen create() { return new CannonDrillGenImpl(); }

	@Override
	public <T extends Block> ModelFile getModel(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider, BlockState state) {
		boolean axisAlongFirst = state.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
		boolean rotated = state.getValue(CannonDrillBlock.FACING).getAxis() == Direction.Axis.X ^ axisAlongFirst;
		String suf = state.getValue(CannonDrillBlock.STATE).getSerializedName() + (rotated ? "_rotated" : "");
		return provider.models().getExistingFile(CreateBigCannons.resource("block/cannon_drill/" + suf));
	}

}
