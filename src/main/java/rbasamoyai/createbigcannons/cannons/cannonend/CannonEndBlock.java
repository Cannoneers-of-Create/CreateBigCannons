package rbasamoyai.createbigcannons.cannons.cannonend;

import java.util.Optional;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;

public class CannonEndBlock extends WrenchableDirectionalBlock implements CannonBlock {

	private final CannonMaterial cannonMaterial;
	private final VoxelShaper shapes;
	
	public CannonEndBlock(Properties properties, CannonMaterial cannonMaterial) {
		super(properties);
		this.cannonMaterial = cannonMaterial;
		this.shapes = this.makeShapes();
	}
	
	private VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(box(0, 0, 0, 16, 8, 16), box(6, 8, 6, 10, 10, 10), box(5, 10, 5, 11, 16, 11));
		return new AllShapes.Builder(base).forDirectional();
	}
	
	@Override public CannonMaterial getCannonMaterial() { return this.cannonMaterial; }
	@Override public Direction.Axis getAxis(BlockState state) { return state.getValue(FACING).getAxis(); }
	@Override public Optional<Direction> getFacing(BlockState state) { return Optional.of(state.getValue(FACING).getOpposite()); }
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.CLOSED; }
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}
	
}
