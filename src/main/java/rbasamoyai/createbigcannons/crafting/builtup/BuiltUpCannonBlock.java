package rbasamoyai.createbigcannons.crafting.builtup;

import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class BuiltUpCannonBlock extends DirectionalBlock implements ITE<LayeredCannonBlockEntity>, CannonBlock {
	
	public BuiltUpCannonBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}
	
	@Override public CannonMaterial getCannonMaterial() { return CannonMaterial.INCOMPLETE_LAYERED; }
	@Override public CannonCastShape getCannonShape() { return CannonCastShape.MEDIUM.get(); }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }
	
	@Override
	public CannonMaterial getCannonMaterialInLevel(LevelAccessor level, BlockState state, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof LayeredCannonBlockEntity layered ? layered.getBaseMaterial() : this.getCannonMaterial();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.CLOSED; }
	@Override public boolean isComplete(BlockState state) { return false; }
	
	@Override public RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }
	
	@Override public Class<LayeredCannonBlockEntity> getTileEntityClass() { return LayeredCannonBlockEntity.class; }
	@Override public BlockEntityType<? extends LayeredCannonBlockEntity> getTileEntityType() { return CBCBlockEntities.LAYERED_CANNON.get(); }
	
	@Override public BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }
	@Override public BlockState mirror(BlockState state, Mirror mirror) { return state.setValue(FACING, mirror.mirror(state.getValue(FACING))); }
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return ItemStack.EMPTY;
	}

}
