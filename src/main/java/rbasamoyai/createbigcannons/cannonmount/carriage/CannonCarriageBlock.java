package rbasamoyai.createbigcannons.cannonmount.carriage;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.CBCBlockEntities;

import java.util.List;

public class CannonCarriageBlock extends Block implements IWrenchable, ITE<CannonCarriageBlockEntity> {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty SADDLED = BooleanProperty.create("saddled");
	
	public CannonCarriageBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(SADDLED, false));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING)
				.add(SADDLED);
	}

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

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		if (state.getValue(SADDLED)) {
			if (stack.isEmpty()) {
				level.setBlock(pos, state.setValue(SADDLED, false), 11);
				if (!level.isClientSide) player.addItem(Items.SADDLE.getDefaultInstance());
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		} else {
			if (stack.is(Items.SADDLE)) {
				if (!level.isClientSide && !player.isCreative()) stack.shrink(1);
				level.setBlock(pos, state.setValue(SADDLED, true), 11);
				level.playSound(player, pos, SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 0.5F, 1.0F);
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return InteractionResult.PASS;
	}

	@SuppressWarnings("deprecated")
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> loot = super.getDrops(state, builder);
		if (state.getValue(SADDLED)) loot.add(Items.SADDLE.getDefaultInstance());
		return loot;
	}

	@Override public Class<CannonCarriageBlockEntity> getTileEntityClass() { return CannonCarriageBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonCarriageBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_CARRIAGE.get(); }

}
