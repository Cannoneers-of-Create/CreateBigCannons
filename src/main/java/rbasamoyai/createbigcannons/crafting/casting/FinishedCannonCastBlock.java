package rbasamoyai.createbigcannons.crafting.casting;

import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.CBCBlocks;

public class FinishedCannonCastBlock extends Block implements ITE<FinishedCannonCastBlockEntity> {
	
	public FinishedCannonCastBlock(Properties properties) {
		super(properties);
	}
	
	@Override public VoxelShape getVisualShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
	@Override public boolean propagatesSkylightDown(BlockState state, BlockGetter blockGetter, BlockPos pos) { return false; }
	@Override public float getShadeBrightness(BlockState state, BlockGetter blockGetter, BlockPos pos) { return 0.8f; }
	
	@Override public RenderShape getRenderShape(BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.BLOCK; }

	@Override public Class<FinishedCannonCastBlockEntity> getTileEntityClass() { return FinishedCannonCastBlockEntity.class; }
	@Override public BlockEntityType<? extends FinishedCannonCastBlockEntity> getTileEntityType() { return CBCBlockEntities.FINISHED_CANNON_CAST.get(); }

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
			this.withTileEntityDo(level, pos, FinishedCannonCastBlockEntity::removeCast);
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return CBCBlocks.CASTING_SAND.asStack();
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (!level.isClientSide && player.getItemInHand(hand).isEmpty()) {
			this.withTileEntityDo(level, result.getBlockPos(), FinishedCannonCastBlockEntity::removeCast);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	@Override public boolean hasAnalogOutputSignal(BlockState state) { return true; }
	@Override public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) { return 15; }
	
}
