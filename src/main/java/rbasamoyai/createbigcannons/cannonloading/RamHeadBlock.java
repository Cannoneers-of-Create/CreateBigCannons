package rbasamoyai.createbigcannons.cannonloading;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RamHeadBlock extends WrenchableDirectionalBlock implements SimpleWaterloggedBlock {

	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	
	private final VoxelShaper shapes;
	
	public RamHeadBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.shapes = this.makeShapes();
	}
	
	private VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(Block.box(6, 0, 6, 10, 10, 10), Block.box(3, 10, 3, 13, 16, 13));
		return new AllShapes.Builder(base).forDirectional();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		return state.setValue(FACING, state.getValue(FACING).getOpposite());
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}
	
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.NORMAL; }
	
}
