package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;

public abstract class AutocannonBaseBlock extends DirectionalBlock implements AutocannonBlock, SimpleWaterloggedBlock {

    private final AutocannonMaterial material;

    protected AutocannonBaseBlock(Properties properties, AutocannonMaterial material) {
        super(properties.pushReaction(PushReaction.BLOCK));
        this.material = material;
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override public AutocannonMaterial getAutocannonMaterial() { return this.material; }
    @Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite())
			.setValue(BlockStateProperties.WATERLOGGED, waterlogged);
    }

    @Override public BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }
    @Override public BlockState mirror(BlockState state, Mirror mirror) { return state.setValue(FACING, mirror.mirror(state.getValue(FACING))); }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction face, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, face, otherState, level, pos, otherPos);
    }

}
