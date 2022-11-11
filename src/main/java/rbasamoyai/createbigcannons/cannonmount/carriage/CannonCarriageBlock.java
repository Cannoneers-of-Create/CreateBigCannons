package rbasamoyai.createbigcannons.cannonmount.carriage;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import rbasamoyai.createbigcannons.CBCBlockEntities;

public class CannonCarriageBlock extends Block implements IWrenchable, ITE<CannonCarriageBlockEntity> {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public CannonCarriageBlock(Properties properties) {
		super(properties);
	}
	
	@Override protected void createBlockStateDefinition(Builder<Block, BlockState> builder) { builder.add(FACING); }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction dir = context.getHorizontalDirection();
		return super.getStateForPlacement(context).setValue(FACING, context.getPlayer().isShiftKeyDown() ? dir.getOpposite() : dir);
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		Level level = context.getLevel();
		if (!level.isClientSide && level.getBlockEntity(context.getClickedPos()) instanceof CannonCarriageBlockEntity carriage) {
			carriage.tryAssemble();
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override public Class<CannonCarriageBlockEntity> getTileEntityClass() { return CannonCarriageBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonCarriageBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_CARRIAGE.get(); }

}
