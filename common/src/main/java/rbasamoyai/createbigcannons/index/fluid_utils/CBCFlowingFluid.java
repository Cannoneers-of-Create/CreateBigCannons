package rbasamoyai.createbigcannons.index.fluid_utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class CBCFlowingFluid extends FlowingFluid {

	private final boolean infinite;
	private final Supplier<? extends Fluid> flowing;
	private final Supplier<? extends Fluid> still;
	@Nullable private final Supplier<? extends Item> bucket;
	@Nullable private final Supplier<? extends LiquidBlock> block;
	private final int dropoff;
	private final int flowspeed;
	private final int tickRate;

	public CBCFlowingFluid(Properties properties) {
		this.infinite = properties.infinite;
		this.flowing = properties.flowing;
		this.still = properties.still;
		this.bucket = properties.bucket;
		this.block = properties.block;
		this.dropoff = properties.levelDecreasePerBlock;
		this.flowspeed = properties.flowSpeed;
		this.tickRate = properties.tickRate;
	}

	@Override public Fluid getFlowing() { return this.flowing.get(); }
	@Override public Fluid getSource() { return this.still.get(); }
	@Override protected boolean canConvertToSource() { return this.infinite; }

	@Override
	protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
		Block.dropResources(state, level, pos, blockEntity);
	}

	@Override protected int getSlopeFindDistance(LevelReader level) { return this.flowspeed; }
	@Override protected int getDropOff(LevelReader level) { return this.dropoff; }
	@Override public Item getBucket() { return this.bucket.get(); }

	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
		return false;
	}

	@Override public int getTickDelay(LevelReader level) { return this.tickRate; }

	@Override protected float getExplosionResistance() { return 100; }

	@Override
	protected BlockState createLegacyBlock(FluidState state) {
		if (this.block != null) {
			return this.block.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
		}
		return Blocks.AIR.defaultBlockState();
	}

	public static class Flowing extends CBCFlowingFluid {
		public Flowing(Properties properties) {
			super(properties);
			registerDefaultState(this.getStateDefinition().any().setValue(LEVEL, 7));
		}

		@Override
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		@Override public boolean isSource(FluidState state) { return false; }
		@Override public int getAmount(FluidState state) { return state.getValue(LEVEL); }
	}

	public static class Still extends CBCFlowingFluid {
		public Still(Properties properties) { super(properties); }
		@Override public boolean isSource(FluidState state) { return false; }
		@Override public int getAmount(FluidState state) { return 8; }
	}

	/**
	 * Taken from SimpleFlowableFluid.Properties
	 */
	public static class Properties {
		private Supplier<? extends Fluid> still;
		private Supplier<? extends Fluid> flowing;
		private boolean infinite;
		private Supplier<? extends Item> bucket;
		private Supplier<? extends LiquidBlock> block;
		private int flowSpeed = 4;
		private int levelDecreasePerBlock = 1;
		private float blastResistance = 1;
		private int tickRate = 5;

		public Properties(Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing) {
			this.still = still;
			this.flowing = flowing;
		}

		public Properties canMultiply() {
			infinite = true;
			return this;
		}

		public Properties bucket(Supplier<? extends Item> bucket) {
			this.bucket = bucket;
			return this;
		}

		public Properties block(Supplier<? extends LiquidBlock> block) {
			this.block = block;
			return this;
		}

		public Properties flowSpeed(int flowSpeed) {
			this.flowSpeed = flowSpeed;
			return this;
		}

		public Properties levelDecreasePerBlock(int levelDecreasePerBlock) {
			this.levelDecreasePerBlock = levelDecreasePerBlock;
			return this;
		}

		public Properties blastResistance(float blastResistance) {
			this.blastResistance = blastResistance;
			return this;
		}

		public Properties tickRate(int tickRate) {
			this.tickRate = tickRate;
			return this;
		}
	}

}
