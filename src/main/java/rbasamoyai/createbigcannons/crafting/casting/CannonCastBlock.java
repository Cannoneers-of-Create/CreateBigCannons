package rbasamoyai.createbigcannons.crafting.casting;

import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.CBCBlocks;

public class CannonCastBlock extends Block implements ITE<CannonCastBlockEntity> {
	
	public CannonCastBlock(Properties properties) {
		super(properties);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean dropContents) {
		if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
			this.withTileEntityDo(level, pos, CannonCastBlockEntity::destroyCastMultiblockAtLayer);
		}
		super.onRemove(state, level, pos, newState, dropContents);
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return CBCBlocks.CASTING_SAND.asStack();
	}
	
	@Override public VoxelShape getVisualShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
	@Override public boolean propagatesSkylightDown(BlockState state, BlockGetter blockGetter, BlockPos pos) { return false; }
	@Override public float getShadeBrightness(BlockState state, BlockGetter blockGetter, BlockPos pos) { return 0.8f; }
	
	@Override public RenderShape getRenderShape(BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.BLOCK; }

	@Override public Class<CannonCastBlockEntity> getTileEntityClass() { return CannonCastBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonCastBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_CAST.get(); }

	@Override public boolean hasAnalogOutputSignal(BlockState state) { return true; }
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return this.getTileEntityOptional(level, pos)
				.map(CannonCastBlockEntity::getControllerTE)
				.map(CannonCastBlockEntity::getFillState)
				.map(CannonCastBlock::castFractionToRedstoneLevel)
				.orElse(0);
	}
	
	public static int castFractionToRedstoneLevel(float frac) {
		return Mth.floor(Mth.clamp(frac * 13 + (frac > 0 ? 1 : 0), 0, 14));
	}
}
