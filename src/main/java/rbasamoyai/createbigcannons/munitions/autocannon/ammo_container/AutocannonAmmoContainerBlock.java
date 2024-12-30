package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import java.util.Locale;

import javax.annotation.Nullable;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.index.CBCShapes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;

public class AutocannonAmmoContainerBlock extends Block implements IWrenchable, SimpleWaterloggedBlock, IBE<AutocannonAmmoContainerBlockEntity> {

	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<State> CONTAINER_STATE = EnumProperty.create("state", State.class);

	public AutocannonAmmoContainerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState()
			.setValue(CONTAINER_STATE, State.CLOSED)
			.setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AXIS).add(CONTAINER_STATE).add(WATERLOGGED);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		return this.defaultBlockState().setValue(AXIS, context.getHorizontalDirection().getAxis())
			.setValue(WATERLOGGED, waterlogged);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return CBCShapes.AUTOCANNON_AMMO_CONTAINER.get(state.getValue(AXIS));
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (level.getBlockEntity(pos) instanceof AutocannonAmmoContainerBlockEntity be) {
			if (stack.hasCustomHoverName()) be.setCustomName(stack.getHoverName());
			be.setMainAmmoDirect(AutocannonAmmoContainerItem.getMainAmmoStack(stack));
			be.setTracersDirect(AutocannonAmmoContainerItem.getTracerAmmoStack(stack));
			be.setSpacing(AutocannonAmmoContainerItem.getTracerSpacing(stack));
		}
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof AutocannonAmmoContainerBlockEntity be && player.isCreative() && be.canDropInCreative()) {
			ItemStack stack = new ItemStack(this.asItem());
			be.saveToItem(stack);
			if (be.hasCustomName()) stack.setHoverName(be.getCustomName());

			ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, stack);
			itemEntity.setDefaultPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
		super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			if (level.getBlockEntity(pos) instanceof AutocannonAmmoContainerBlockEntity) {
				level.updateNeighbourForOutputSignal(pos, state.getBlock());
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		ItemStack itemStack = super.getCloneItemStack(level, pos, state);
		if (level.getBlockEntity(pos) instanceof AutocannonAmmoContainerBlockEntity be) be.saveToItem(itemStack);
		return itemStack;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof AutocannonAmmoContainerBlockEntity be) {
			if (player instanceof ServerPlayer splayer) {
				CBCMenuTypes.AUTOCANNON_AMMO_CONTAINER.open(splayer, be.getDisplayName(), be, buf -> {
					buf.writeBoolean(be.isCreativeContainer());
					buf.writeVarInt(be.getSpacing());
					buf.writeBoolean(true);
					buf.writeBlockPos(pos);
				});
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		if (!(level.getBlockEntity(pos) instanceof AutocannonAmmoContainerBlockEntity be)) return 0;
		AutocannonAmmoType type = be.getAmmoType();
		if (type == AutocannonAmmoType.NONE) return 0;
		if (be.isCreativeContainer()) return be.getTotalCount() == 0 ? 0 : 15;
		return (int) Math.floor((float) be.getTotalCount() / (float) type.getCapacity() * 15f);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		super.tick(state, level, pos, random);
		if (level.getBlockEntity(pos) instanceof AutocannonAmmoContainerBlockEntity be) be.recheckOpen();
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public BlockState updateShape(BlockState state, Direction face, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return super.updateShape(state, face, otherState, level, pos, otherPos);
	}

	@Override public Class<AutocannonAmmoContainerBlockEntity> getBlockEntityClass() { return AutocannonAmmoContainerBlockEntity.class; }
	@Override public BlockEntityType<? extends AutocannonAmmoContainerBlockEntity> getBlockEntityType() { return CBCBlockEntities.AUTOCANNON_AMMO_CONTAINER.get(); }

	public enum State implements StringRepresentable {
		CLOSED,
		EMPTY,
		FILLED;

		private final String id = this.name().toLowerCase(Locale.ROOT);

		@Override public String getSerializedName() { return this.id; }

		public static State getFromFilled(boolean filled) { return filled ? FILLED : EMPTY; }
	}

}
