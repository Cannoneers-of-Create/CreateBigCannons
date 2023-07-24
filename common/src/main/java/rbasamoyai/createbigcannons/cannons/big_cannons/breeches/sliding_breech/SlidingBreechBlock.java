package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech;

import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class SlidingBreechBlock extends DirectionalAxisKineticBlock implements IBE<SlidingBreechBlockEntity>, BigCannonBlock {

	private final BigCannonMaterial cannonMaterial;
	private final NonNullSupplier<? extends Block> quickfiringConversion;

	public SlidingBreechBlock(Properties properties, BigCannonMaterial cannonMaterial, NonNullSupplier<? extends Block> quickfiringConversion) {
		super(properties);
		this.cannonMaterial = cannonMaterial;
		this.quickfiringConversion = quickfiringConversion;
	}

	@Override
	public BigCannonMaterial getCannonMaterial() {
		return this.cannonMaterial;
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.SLIDING_BREECH;
	}

	@Override
	public Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}

	@Override
	public BigCannonEnd getOpeningType(@Nullable Level level, BlockState state, BlockPos pos) {
		return level != null && level.getBlockEntity(pos) instanceof SlidingBreechBlockEntity breech ? breech.getOpeningType() : BigCannonEnd.OPEN;
	}

	@Override
	public BigCannonEnd getOpeningType(MountedBigCannonContraption contraption, BlockState state, BlockPos pos) {
		return contraption.presentBlockEntities.get(pos) instanceof SlidingBreechBlockEntity breech ? breech.getOpeningType() : BigCannonEnd.OPEN;
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getNearestLookingDirection().getOpposite();
		Direction horizontal = context.getHorizontalDirection();
		return this.defaultBlockState()
			.setValue(FACING, facing)
			.setValue(AXIS_ALONG_FIRST_COORDINATE, horizontal.getAxis() == Direction.Axis.Z);
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		return InteractionResult.PASS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public Class<SlidingBreechBlockEntity> getBlockEntityClass() {
		return SlidingBreechBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SlidingBreechBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.SLIDING_BREECH.get();
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	public BlockState getConversion(BlockState old) {
		return this.quickfiringConversion.get().defaultBlockState()
			.setValue(FACING, old.getValue(FACING))
			.setValue(AXIS_ALONG_FIRST_COORDINATE, old.getValue(AXIS_ALONG_FIRST_COORDINATE));
	}

}
