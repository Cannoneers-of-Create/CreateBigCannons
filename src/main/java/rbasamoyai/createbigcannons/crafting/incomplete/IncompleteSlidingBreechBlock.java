package rbasamoyai.createbigcannons.crafting.incomplete;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.CannonBehavior;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.SolidCannonBlock;

public class IncompleteSlidingBreechBlock extends SolidCannonBlock<IncompleteCannonBlockEntity> implements IncompleteCannonBlock {
	
	public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 1);	
	public static final BooleanProperty ALONG_FIRST = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;
	
	private final NonNullSupplier<? extends Item> secondItemSupplier;
	private Item resolvedSecondItem;
	private final NonNullSupplier<? extends Block> resultSupplier;
	private Block result;
	private List<ItemLike> resolvedRequiredItems;
	
	public IncompleteSlidingBreechBlock(Properties properties, CannonMaterial material, NonNullSupplier<? extends Item> secondItemSupplier, NonNullSupplier<? extends Block> resultSupplier) {
		super(properties, material);
		this.secondItemSupplier = secondItemSupplier;
		this.resultSupplier = resultSupplier;
		this.registerDefaultState(this.defaultBlockState().setValue(STAGE, 0).setValue(ALONG_FIRST, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STAGE);
		builder.add(ALONG_FIRST);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		int stage = state.getValue(STAGE);
		if (stage == 0 && !AllBlocks.SHAFT.is(stack.getItem())) return InteractionResult.PASS;
		if (stage == 1 && !stack.is(this.resolveSecondItem())) return InteractionResult.PASS;
		level.playSound(player, pos, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
		if (!level.isClientSide) {
			if (!player.isCreative()) stack.shrink(1);
			this.withTileEntityDo(level, pos, cbe -> {
				CannonBehavior behavior = cbe.cannonBehavior();
				cbe.setRemoved();
				
				if (stage == 0) {
					level.setBlock(pos, state.setValue(STAGE, 1), 3 | 16);
				} else {
					if (this.result == null) {
						this.result = this.resultSupplier.get();
					}
					BlockState newState = this.result.delegate.get().defaultBlockState();
					if (newState.hasProperty(FACING)) {
						newState = newState.setValue(FACING, state.getValue(FACING));
					}
					if (newState.hasProperty(ALONG_FIRST)) {
						newState = newState.setValue(ALONG_FIRST, state.getValue(ALONG_FIRST));
					}
					level.setBlock(pos, newState, 3 | 16);
				}
				
				BlockEntity be = level.getBlockEntity(pos);
				if (!(be instanceof ICannonBlockEntity cbe1)) return;
				CannonBehavior behavior1 = cbe1.cannonBehavior();
				for (Direction dir : Direction.values()) {
					boolean isConnected = behavior.isConnectedTo(dir.getOpposite());
					behavior1.setConnectedFace(dir, isConnected);
					if (level.getBlockEntity(pos.relative(dir)) instanceof ICannonBlockEntity cbe2) {
						cbe2.cannonBehavior().setConnectedFace(dir.getOpposite(), isConnected);
					}
				}
			});
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getNearestLookingDirection().getOpposite();
		return this.defaultBlockState()
				.setValue(FACING, facing)
				.setValue(ALONG_FIRST, facing.getAxis() == Direction.Axis.Z);
	}
	
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }
	
	@Override public Class<IncompleteCannonBlockEntity> getTileEntityClass() { return IncompleteCannonBlockEntity.class; }
	@Override public BlockEntityType<? extends IncompleteCannonBlockEntity> getTileEntityType() { return CBCBlockEntities.INCOMPLETE_CANNON.get(); }

	@Override
	public List<ItemLike> requiredItems() {
		if (this.resolvedRequiredItems == null) {
			this.resolvedRequiredItems = new ArrayList<>(2);
			this.resolvedRequiredItems.add(AllBlocks.SHAFT.get());
			this.resolvedRequiredItems.add(this.resolveSecondItem());
		}
		return this.resolvedRequiredItems;
	}
	
	protected Item resolveSecondItem() {
		if (this.resolvedSecondItem == null) {
			this.resolvedSecondItem = this.secondItemSupplier.get();
		}
		return this.resolvedSecondItem.delegate.get();
	}

	@Override public int progress(BlockState state) { return state.getValue(STAGE); }
	
	@Override public boolean isComplete(BlockState state) { return false; }

}
