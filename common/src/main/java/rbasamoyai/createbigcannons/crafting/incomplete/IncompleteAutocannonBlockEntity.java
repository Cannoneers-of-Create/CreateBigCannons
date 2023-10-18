package rbasamoyai.createbigcannons.crafting.incomplete;

import java.util.List;

import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannons.IncompleteItemCannonBehavior;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.IAutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.WandActionable;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;

public class IncompleteAutocannonBlockEntity extends AutocannonBlockEntity implements IHaveHoveringInformation, WandActionable {

	public IncompleteAutocannonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected ItemCannonBehavior makeBehavior() {
		return new IncompleteItemCannonBehavior(this);
	}

	@Override
	public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (!(this.getBlockState().getBlock() instanceof IncompleteWithItemsCannonBlock incomplete)) return false;
		IncompleteCannonBlockTooltip.addToTooltip(tooltip, isPlayerSneaking, incomplete, this.getBlockState());
		return true;
	}

	@Override
	public InteractionResult onWandUsed(UseOnContext context) {
		if (!this.level.isClientSide) {
			BlockState state = this.getBlockState();
			Direction dir = state.getValue(BlockStateProperties.FACING);
			AutocannonMaterial material = ((AutocannonBlock) state.getBlock()).getAutocannonMaterialInLevel(this.level, state, this.worldPosition);
			DrillBoringBlockRecipe recipe = AbstractCannonDrillBlockEntity.getBlockRecipe(state, dir);
			CompoundTag loadTag = this.saveWithFullMetadata();
			if (recipe != null) {
				BlockState newState = recipe.getResultState(state);
				this.level.setBlock(this.worldPosition, newState, 11);
				this.level.playSound(null, this.worldPosition, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0f, 1.0f);
			} else if (state.getBlock() instanceof IncompleteWithItemsCannonBlock incomplete) {
				BlockState newState = incomplete.getCompleteBlockState(state);
				this.level.setBlock(this.worldPosition, newState, 11);
				this.level.playSound(null, this.worldPosition, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
			} else {
				return InteractionResult.PASS;
			}
			this.setRemoved();
			BlockEntity newBE = this.level.getBlockEntity(this.worldPosition);
			if (newBE != null) newBE.load(loadTag);

			for (Direction dir1 : Iterate.directions) {
				if (!this.cannonBehavior().isConnectedTo(dir1)) continue;
				BlockPos pos1 = this.worldPosition.relative(dir1);
				BlockState state1 = this.level.getBlockState(pos1);
				BlockEntity be1 = this.level.getBlockEntity(pos1);
				if (state1.getBlock() instanceof AutocannonBlock cBlock1
					&& cBlock1.getAutocannonMaterialInLevel(this.level, state1, pos1) == material
					&& cBlock1.canConnectToSide(this.level, state1, pos1, dir1.getOpposite())
					&& be1 instanceof IAutocannonBlockEntity cbe1) {
					cbe1.cannonBehavior().setConnectedFace(dir1.getOpposite(), true);
					be1.setChanged();
				}
			}
		}
		return InteractionResult.sidedSuccess(this.getLevel().isClientSide);
	}

}
