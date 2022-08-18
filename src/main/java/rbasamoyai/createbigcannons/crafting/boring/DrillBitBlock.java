package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock.PistonState;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlocks;

public class DrillBitBlock extends WrenchableDirectionalBlock implements SimpleWaterloggedBlock {

	private final VoxelShaper shapes;
	
	public DrillBitBlock(Properties properties) {
		super(properties);
		this.shapes = this.makeShapes();
		this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, false));
	}
	
	private VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(Block.box(6, 0, 6, 10, 16, 10), Block.box(5, 16, 5, 11, 22, 11));
		return new AllShapes.Builder(base).forDirectional();
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.WATERLOGGED);
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return CBCBlocks.CANNON_DRILL.asStack();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}
	
	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		Direction direction = state.getValue(FACING);
		BlockPos drillBase = null;
		
		for (int i = 1; i < CannonDrillBlock.maxAllowedDrillLength(); ++i) {
			BlockPos currentPos = pos.relative(direction.getOpposite(), i);
			BlockState block = level.getBlockState(currentPos);
			
			if (isExtensionPole(block) && direction.getAxis() == block.getValue(BlockStateProperties.FACING).getAxis()) continue;
			if (isDrillBlock(block) && block.getValue(FACING) == direction) drillBase = currentPos;
			
			break;
		}
		
		if (drillBase != null) {
			BlockPos basePos = drillBase.immutable();
			BlockPos.betweenClosedStream(drillBase, pos.immutable())
			.filter(p -> !p.equals(pos) && !p.equals(basePos))
			.forEach(p -> level.destroyBlock(p, !player.isCreative()));
			level.setBlockAndUpdate(basePos, level.getBlockState(basePos).setValue(CannonDrillBlock.STATE, PistonState.RETRACTED));
		}
		
		super.playerWillDestroy(level, pos, state, player);
	}
	
	private static boolean isExtensionPole(BlockState state) { return AllBlocks.PISTON_EXTENSION_POLE.has(state); }
	private static boolean isDrillBlock(BlockState state) { return CBCBlocks.CANNON_DRILL.has(state); }
	
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.NORMAL; }
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fstate = context.getLevel().getFluidState(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(BlockStateProperties.WATERLOGGED, fstate.getType() == Fluids.WATER);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return state;
	}
	
	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}

}
