package rbasamoyai.createbigcannons.crafting.incomplete;

import java.util.List;

import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEndBlockEntity;
import rbasamoyai.createbigcannons.crafting.WandActionable;

public class IncompleteCannonBlockEntity extends CannonEndBlockEntity implements IHaveHoveringInformation, WandActionable {

	public IncompleteCannonBlockEntity(BlockEntityType<? extends IncompleteCannonBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (!(this.getBlockState().getBlock() instanceof IncompleteCannonBlock incomplete)) return false;
		IncompleteCannonBlockTooltip.addToTooltip(tooltip, isPlayerSneaking, incomplete, this.getBlockState());
		return true;
	}
	
	@Override
	public InteractionResult onWandUsed(UseOnContext context) {
		if (!(this.getBlockState().getBlock() instanceof IncompleteCannonBlock incomplete)) return InteractionResult.PASS;
		if (!this.level.isClientSide) {
			CompoundTag loadTag = this.saveWithFullMetadata();
			loadTag.putBoolean("JustBored", true);
			BlockState boredState = incomplete.getCompleteBlockState(this.getBlockState());
			this.setRemoved();
			this.level.setBlock(this.worldPosition, boredState, 11);
			BlockEntity newBE = this.level.getBlockEntity(this.worldPosition);
			if (newBE != null) newBE.load(loadTag);
		}
		return InteractionResult.sidedSuccess(this.level.isClientSide);
	}

}
