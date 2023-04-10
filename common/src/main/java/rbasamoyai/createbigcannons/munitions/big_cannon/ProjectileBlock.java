package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

import javax.annotation.Nullable;

public abstract class ProjectileBlock extends DirectionalBlock implements IWrenchable, BigCannonMunitionBlock {

	private final VoxelShaper shapes;
	
	public ProjectileBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP));
		this.shapes = this.makeShapes();
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}
	
	protected VoxelShaper makeShapes() {
		VoxelShape base = box(3, 0, 3, 13, 16, 13);
		return new AllShapes.Builder(base).forDirectional();		
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Player player = context.getPlayer();
		Direction facing = context.getClickedFace();
		boolean flag = player != null && player.isShiftKeyDown();
		BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos().relative(facing.getOpposite()));

		if (clickedState.getBlock() instanceof BigCannonBlock cblock
				&& cblock.getFacing(clickedState).getAxis() == facing.getAxis()
				&& !flag) {
			facing = facing.getOpposite();
		}
		return this.defaultBlockState().setValue(FACING, facing);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}
	
	public abstract AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity blockEntity);
	
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.NORMAL; }
	
}
