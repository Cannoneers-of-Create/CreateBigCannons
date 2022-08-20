package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;

public class UnboredSlidingBreechBlock extends UnboredCannonBlock {

	public static final BooleanProperty ALONG_FIRST = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;
	
	public UnboredSlidingBreechBlock(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup, VoxelShape baseShape) {
		super(properties, material, boredBlockSup, baseShape);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(ALONG_FIRST);
		super.createBlockStateDefinition(builder);
	}
	
	@Override
	public BlockState getBoredBlockState(BlockState state) {
		BlockState bored = super.getBoredBlockState(state);
		return bored.hasProperty(ALONG_FIRST) ? bored.setValue(ALONG_FIRST, state.getValue(ALONG_FIRST)) : bored;
	}

}
