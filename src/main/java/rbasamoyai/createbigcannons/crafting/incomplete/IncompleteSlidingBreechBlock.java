package rbasamoyai.createbigcannons.crafting.incomplete;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.ITransformableBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.StructureTransform;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.CannonBehavior;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.SolidCannonBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class IncompleteSlidingBreechBlock extends SolidCannonBlock<IncompleteCannonBlockEntity> implements IncompleteCannonBlock, ITransformableBlock {
	
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
		this.registerDefaultState(this.defaultBlockState().setValue(STAGE_2, 0).setValue(ALONG_FIRST, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STAGE_2);
		builder.add(ALONG_FIRST);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		int stage = state.getValue(STAGE_2);
		if (stage == 0 && !AllBlocks.SHAFT.is(stack.getItem())) return InteractionResult.PASS;
		if (stage == 1 && !stack.is(this.resolveSecondItem())) return InteractionResult.PASS;
		level.playSound(player, pos, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
		if (!level.isClientSide) {
			if (!player.isCreative()) stack.shrink(1);
			this.withTileEntityDo(level, pos, cbe -> {
				CannonBehavior behavior = cbe.cannonBehavior();
				cbe.setRemoved();
				
				if (stage == 0) {
					level.setBlock(pos, state.setValue(STAGE_2, 1), 3 | 16);
				} else {
					level.setBlock(pos, this.getCompleteBlockState(state), 3 | 16);
				}
				
				BlockEntity be = level.getBlockEntity(pos);
				if (!(be instanceof ICannonBlockEntity cbe1)) return;
				CannonBehavior behavior1 = cbe1.cannonBehavior();
				for (Direction dir : Direction.values()) {
					boolean isConnected = behavior.isConnectedTo(dir);
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
		Direction horizontal = context.getHorizontalDirection();
		return this.defaultBlockState()
				.setValue(FACING, facing)
				.setValue(ALONG_FIRST, horizontal.getAxis() == Direction.Axis.Z);
	}
	
	@Override public CannonCastShape getCannonShape() { return CannonCastShape.SLIDING_BREECH.get(); }
	
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

	@Override public int progress(BlockState state) { return state.getValue(STAGE_2); }
	
	@Override public boolean isComplete(BlockState state) { return false; }
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		if (rotation.ordinal() % 2 == 1) state = state.cycle(ALONG_FIRST);
		return super.rotate(state, rotation);
	}
	
	@Override
	public BlockState transform(BlockState state, StructureTransform transform) {
		if (transform.mirror != null) state = this.mirror(state, transform.mirror);
		if (transform.rotationAxis == Direction.Axis.Y) return this.rotate(state, transform.rotation);
		Direction newFacing = transform.rotateFacing(state.getValue(FACING));
		if (transform.rotationAxis == newFacing.getAxis() && transform.rotation.ordinal() % 2 == 1) state = state.cycle(ALONG_FIRST);
		return state.setValue(FACING, newFacing);
	}
	
	@Override
	public BlockState getCompleteBlockState(BlockState state) {
		if (this.result == null) this.result = this.resultSupplier.get();
		BlockState newState = this.result.delegate.get().defaultBlockState();
		if (newState.hasProperty(FACING)) newState = newState.setValue(FACING, state.getValue(FACING));
		return newState.hasProperty(ALONG_FIRST) ? newState = newState.setValue(ALONG_FIRST, state.getValue(ALONG_FIRST)) : newState;
	}

}
