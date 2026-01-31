package rbasamoyai.createbigcannons.index.fluid_utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nonnull;

public class CBCLiquidBlock extends LiquidBlock implements FluidGetter {

    private final FlowingFluid fluid;

	public CBCLiquidBlock(FlowingFluid fluid, Properties properties) {
		super(fluid, properties);
        this.fluid = fluid;
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction direction) {
		return adjacentBlockState.getFluidState().getType().isSame(this.getFluid());
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (this.shouldSpreadLiquid(level, pos, state)) {
			level.scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(level));
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (this.shouldSpreadLiquid(level, pos, state)) {
			level.scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(level));
		}
	}

	protected boolean shouldSpreadLiquid(Level level, BlockPos pos, BlockState state) { return true; }

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
		if (state.getFluidState().isSource() || neighborState.getFluidState().isSource()) {
			level.scheduleTick(currentPos, state.getFluidState().getType(), this.getFluid().getTickDelay(level));
		}
		return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
	}

    // todo: might not be needed anymore
	/*@Override
	public ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
		if (state.getValue(LEVEL) == 0) {
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
			return new ItemStack(this.getFluid().getBucket());
		} else {
			return ItemStack.EMPTY;
		}
	}*/

	@Override public FlowingFluid getFluid() { return this.fluid; }

	@Override public Optional<SoundEvent> getPickupSound() { return this.getFluid().getPickupSound(); }

    private List<FluidState> stateCache = null;

    // Taken from Bumblezone with help from TelepathicGrunt - thanks! --ritchie
    @Nonnull
    @Override
    public FluidState getFluidState(BlockState arg) {
        int i = arg.getValue(LEVEL);
        if (this.stateCache == null) {
            this.initFluidStateCache();
        }
        return this.stateCache.get(Math.min(i, 8));
    }

    protected synchronized void initFluidStateCache() {
        if (this.stateCache == null) {
            this.stateCache = new ArrayList<>();
            this.stateCache.add(this.getFluid().getSource(false));
            for(int i = 1; i < 8; ++i) {
                this.stateCache.add(this.getFluid().getFlowing(8 - i, false));
            }
            this.stateCache.add(this.getFluid().getFlowing(8, true));
        }
    }

}
