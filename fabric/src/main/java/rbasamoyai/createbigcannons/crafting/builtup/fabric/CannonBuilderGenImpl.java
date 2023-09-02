package rbasamoyai.createbigcannons.crafting.builtup.fabric;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderGen;

public class CannonBuilderGenImpl extends CannonBuilderGen {

	public static CannonBuilderGen create() { return new CannonBuilderGenImpl(); }

	@Override
	public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
		boolean axisAlongFirst = state.getValue(CannonBuilderBlock.AXIS_ALONG_FIRST_COORDINATE);
		boolean rotated = state.getValue(CannonBuilderBlock.FACING).getAxis() == Direction.Axis.X ^ axisAlongFirst;

		String variant = state.getValue(CannonBuilderBlock.STATE).getSerializedName() + (rotated ? "_rotated" : "");
		return prov.models().getExistingFile(CreateBigCannons.resource("block/cannon_builder/" + variant));
	}

}
