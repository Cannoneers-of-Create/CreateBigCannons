package rbasamoyai.createbigcannons.cannons.autocannon.breech;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBaseBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.IAutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;

import static rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock.writeAndSyncSingleBlockData;

public class AutocannonBreechBlock extends AutocannonBaseBlock implements IBE<AbstractAutocannonBreechBlockEntity>, IWrenchable {

	public static final BooleanProperty HANDLE = BooleanProperty.create("handle");

	public AutocannonBreechBlock(Properties properties, AutocannonMaterial material) {
		super(properties, material);
		this.registerDefaultState(this.defaultBlockState().setValue(HANDLE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(HANDLE);
	}

	@Override
	public Class<AbstractAutocannonBreechBlockEntity> getBlockEntityClass() {
		return AbstractAutocannonBreechBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends AbstractAutocannonBreechBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.AUTOCANNON_BREECH.get();
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.AUTOCANNON_BREECH;
	}

	@Override
	public boolean canConnectToSide(LevelAccessor level, BlockState state, BlockPos pos, Direction face) {
		return this.getFacing(state) == face;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
	}

	@Override
	public boolean isBreechMechanism(BlockState state) {
		return true;
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AllShapes.EIGHT_VOXEL_POLE.get(this.getFacing(state).getAxis());
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		Level level = context.getLevel();
		if (level.isClientSide) return InteractionResult.SUCCESS;
		BlockPos pos = context.getClickedPos();
		AutocannonBlockEntity autocannon = this.getBlockEntity(level, pos);
		Direction facing = this.getFacing(state);

		BlockState newState = state.cycle(HANDLE);
		level.setBlock(pos, newState, 3);
		AbstractAutocannonBreechBlockEntity autocannon1 = this.getBlockEntity(level, pos);
		if (autocannon1 != null) {
			boolean previouslyConnected = autocannon.cannonBehavior().isConnectedTo(facing);
			autocannon1.cannonBehavior().setConnectedFace(facing, previouslyConnected);
			if (level.getBlockEntity(pos.relative(facing)) instanceof AutocannonBlockEntity autocannon2) {
				autocannon2.cannonBehavior().setConnectedFace(facing.getOpposite(), previouslyConnected);
			}

			if (!newState.getValue(HANDLE)) {
				DyeColor seat = autocannon1.getSeatColor();
				if (seat != null) {
					ItemStack drop = AllBlocks.SEATS.get(seat).asStack();
					Player player = context.getPlayer();
					if (!player.addItem(drop) && !player.isCreative()) {
						Vec3 spawnLoc = Vec3.atCenterOf(pos);
						ItemEntity dropEntity = new ItemEntity(level, spawnLoc.x, spawnLoc.y, spawnLoc.z, drop);
						level.addFreshEntity(dropEntity);
						dropEntity.setNoPickUpDelay();
						dropEntity.setOwner(player.getUUID());
					}
				}
				autocannon1.setSeatColor(null);
			}
		}
		return InteractionResult.CONSUME;
	}

	@Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.getBlockEntity(pos) instanceof AbstractAutocannonBreechBlockEntity breech) {
			if (breech.getSeatColor() == null
				&& state.getValue(HANDLE)
				&& stack.getItem() instanceof BlockItem blockItem
				&& blockItem.getBlock() instanceof SeatBlock seat) {
				if (!level.isClientSide) {
					breech.setSeatColor(seat.getColor());
					SoundType soundType = seat.defaultBlockState().getSoundType();
					level.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
					if (!player.isCreative()) stack.shrink(1);
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else if (stack.isEmpty() && breech.getSeatColor() != null) {
				if (!level.isClientSide) {
					DyeColor seat = breech.getSeatColor();
					if (seat != null) {
						ItemStack drop = AllBlocks.SEATS.get(seat).asStack();
						if (!player.addItem(drop) && !player.isCreative()) {
							Vec3 spawnLoc = Vec3.atCenterOf(pos);
							ItemEntity dropEntity = new ItemEntity(level, spawnLoc.x, spawnLoc.y, spawnLoc.z, drop);
							level.addFreshEntity(dropEntity);
						}
					}
					breech.setSeatColor(null);
					level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			}

			ItemStack container = breech.getMagazine();
			boolean changed = false;
			boolean tryAdd = false;
			if (!container.isEmpty()) {
				if (!level.isClientSide) {
					tryAdd = true;
					breech.setMagazine(ItemStack.EMPTY);
				}
				changed = true;
			}
			if (stack.getItem() instanceof AutocannonAmmoContainerItem) {
				if (!level.isClientSide) {
					breech.setMagazine(stack);
					player.setItemInHand(hand, ItemStack.EMPTY);
					CBCSoundEvents.PLACE_AUTOCANNON_AMMO_CONTAINER.playOnServer(level, pos);
				}
				changed = true;
			}

			if (tryAdd && !player.addItem(container)) {
				Vec3 spawnLoc = Vec3.atCenterOf(pos);
				ItemEntity dropEntity = new ItemEntity(level, spawnLoc.x, spawnLoc.y, spawnLoc.z, container);
				level.addFreshEntity(dropEntity);
			}
			if (changed) {
				breech.notifyUpdate();
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return super.use(state, level, pos, player, hand, result);
	}

	@Override
	public <T extends BlockEntity & IAutocannonBlockEntity> boolean onInteractWhileAssembled(Player player, BlockPos localPos,
			Direction side, InteractionHand interactionHand, Level level, MountedAutocannonContraption cannon, T be,
			StructureBlockInfo info, AbstractContraptionEntity entity) {
		if (!(be instanceof AbstractAutocannonBreechBlockEntity breech)) return false;

		ItemStack stack = player.getItemInHand(interactionHand);
		ItemStack container = breech.getMagazine();
		Vec3 globalPos = entity.toGlobalVector(Vec3.atCenterOf(localPos), 1);

		boolean insertingContainer = stack.getItem() instanceof AutocannonAmmoContainerItem;
		boolean canRemove = insertingContainer || stack.isEmpty() && (AutocannonAmmoContainerItem.getTotalAmmoCount(container) == 0 || player.isShiftKeyDown());

		boolean changed = false;
		boolean tryAdd = false;
		if (!container.isEmpty() && canRemove) {
			if (!level.isClientSide) {
				tryAdd = true;
				breech.setMagazine(ItemStack.EMPTY);
			}
			changed = true;
		}
		if (breech.getMagazine().isEmpty() && insertingContainer) {
			if (!level.isClientSide) {
				breech.setMagazine(stack);
				player.setItemInHand(interactionHand, ItemStack.EMPTY);
				CBCSoundEvents.PLACE_AUTOCANNON_AMMO_CONTAINER.playOnServer(level, new BlockPos(globalPos));
			}
			changed = true;
		}

		if (tryAdd && !player.addItem(container)) {
			ItemEntity dropEntity = new ItemEntity(level, globalPos.x, globalPos.y, globalPos.z, container);
			level.addFreshEntity(dropEntity);
		}
		if (changed && !level.isClientSide) writeAndSyncSingleBlockData(be, info, entity, cannon);
		return changed;
	}

}
