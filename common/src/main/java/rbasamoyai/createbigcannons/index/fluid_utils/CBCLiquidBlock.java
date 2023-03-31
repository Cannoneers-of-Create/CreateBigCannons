package rbasamoyai.createbigcannons.index.fluid_utils;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

public class CBCLiquidBlock extends LiquidBlock implements FluidGetter {

	private final NonNullSupplier<? extends FlowingFluid> fluidSup;

	public CBCLiquidBlock(NonNullSupplier<? extends FlowingFluid> fluid, Properties properties) {
		super("forge".equals(ArchitecturyTarget.getCurrentTarget()) ? Fluids.FLOWING_WATER : fluid.get(), properties);
		this.fluidSup = fluid;
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

	@Override
	public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
		if (state.getValue(LEVEL) == 0) {
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
			return new ItemStack(this.getFluid().getBucket());
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override public FlowingFluid getFluid() { return this.fluidSup.get(); }

	@Override public Optional<SoundEvent> getPickupSound() { return this.getFluid().getPickupSound(); }

}
