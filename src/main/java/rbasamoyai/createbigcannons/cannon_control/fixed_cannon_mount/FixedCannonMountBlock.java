package rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount;

import javax.annotation.Nullable;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlock;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class FixedCannonMountBlock extends DirectionalBlock implements IBE<FixedCannonMountBlockEntity>, IWrenchable {

	public static final BooleanProperty ASSEMBLY_POWERED = CannonMountBlock.ASSEMBLY_POWERED;
	public static final BooleanProperty FIRE_POWERED = CannonMountBlock.FIRE_POWERED;
	public static final DirectionProperty ROTATION = DirectionProperty.create("rotation", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

	public FixedCannonMountBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
			.setValue(FACING, Direction.NORTH)
			.setValue(ROTATION, Direction.NORTH)
			.setValue(ASSEMBLY_POWERED, false)
			.setValue(FIRE_POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ROTATION, ASSEMBLY_POWERED, FIRE_POWERED);
		super.createBlockStateDefinition(builder);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction dir = context.getNearestLookingDirection();
		boolean flag = context.getPlayer() != null && context.getPlayer().isShiftKeyDown();
		BlockState state = this.defaultBlockState().setValue(FACING, flag ? dir : dir.getOpposite());
		if (dir.getAxis().isVertical()) {
			Direction facing = context.getHorizontalDirection();
			state = state.setValue(ROTATION, dir == Direction.UP ? facing : facing.getOpposite());
		}
		return state;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
		if (!level.isClientSide) {
			if (!level.getBlockTicks().willTickThisTick(pos, this)) {
				level.scheduleTick(pos, this, 0);
			}
		}
	}

	public Direction getAssemblyFace(BlockState state) {
		Direction facing = state.getValue(FACING);
		Direction rotation = state.getValue(ROTATION);
		if (facing.getAxis().isVertical())
			return facing == Direction.UP || rotation.getAxis() == Direction.Axis.X ? rotation : rotation.getOpposite();
		Direction dir1 = rotation.getAxis() == Direction.Axis.X ? rotation
			: rotation.getClockWise(Direction.Axis.X);
		if (dir1.getAxis().isVertical())
			return dir1;
		int rot = facing.get2DDataValue();
		Direction dir2 = dir1;
		for (int i = 0; i < rot; ++i)
			dir2 = dir2.getCounterClockWise();
		return facing.getAxis() == Direction.Axis.Z && rotation.getAxis() == Direction.Axis.X ? dir2.getOpposite() : dir2;
	}

	public Direction getFiringFace(BlockState state) {
		return this.getAssemblyFace(state).getOpposite();
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		boolean prevAssemblyPowered = state.getValue(ASSEMBLY_POWERED);
		boolean prevFirePowered = state.getValue(FIRE_POWERED);
		boolean assemblyPowered = this.hasNeighborSignal(level, state, pos, ASSEMBLY_POWERED);
		boolean firePowered = this.hasNeighborSignal(level, state, pos, FIRE_POWERED);
		Direction fireDirection = this.getFiringFace(state);
		int firePower = level.getSignal(pos.relative(fireDirection), fireDirection);
		this.withBlockEntityDo(level, pos, mount -> mount.onRedstoneUpdate(assemblyPowered, prevAssemblyPowered, firePowered, prevFirePowered, firePower));
	}

	private boolean hasNeighborSignal(Level level, BlockState state, BlockPos pos, BooleanProperty property) {
		if (property == FIRE_POWERED) {
			Direction fireDirection = this.getFiringFace(state);
			return level.getSignal(pos.relative(fireDirection), fireDirection) > 0;
		}
		if (property == ASSEMBLY_POWERED) {
			Direction assemblyDirection = this.getAssemblyFace(state);
			return level.getSignal(pos.relative(assemblyDirection), assemblyDirection) > 0;
		}
		return false;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		if (state.getValue(FACING).getAxis().isVertical()) {
			return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION)));
		} else {
			return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
		}
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		if (state.getValue(FACING).getAxis().isVertical()) {
			return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION)));
		} else {
			return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
		}
	}

	@Override
	public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
		Direction facing = originalState.getValue(FACING);
		if (targetedFace.getAxis() == facing.getAxis()) {
			return originalState.setValue(ROTATION, originalState.getValue(ROTATION).getClockWise());
		} else {
			Direction facing1 = facing.getClockWise(targetedFace.getAxis());
			BlockState newState = originalState.setValue(FACING, facing1);
			if (targetedFace.getAxis() == Direction.Axis.X && (facing == Direction.DOWN || facing1 == Direction.UP))
				newState = newState.setValue(ROTATION, newState.getValue(ROTATION).getOpposite());
			if (targetedFace.getAxis() == Direction.Axis.Z)
				newState = newState.setValue(ROTATION, newState.getValue(ROTATION).getCounterClockWise());
			return newState;
		}
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		InteractionResult resultType = IWrenchable.super.onWrenched(state, context);
		if (!context.getLevel().isClientSide && resultType.consumesAction()
			&& context.getLevel().getBlockEntity(context.getClickedPos()) instanceof FixedCannonMountBlockEntity mount) {
			mount.disassemble();
		}
		return resultType;
	}

	@Override public Class<FixedCannonMountBlockEntity> getBlockEntityClass() { return FixedCannonMountBlockEntity.class; }
	@Override public BlockEntityType<? extends FixedCannonMountBlockEntity> getBlockEntityType() { return CBCBlockEntities.FIXED_CANNON_MOUNT.get(); }

}
