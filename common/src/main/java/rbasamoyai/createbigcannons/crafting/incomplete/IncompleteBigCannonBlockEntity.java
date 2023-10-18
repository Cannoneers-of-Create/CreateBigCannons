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
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEndBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.WandActionable;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredBigCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class IncompleteBigCannonBlockEntity extends BigCannonEndBlockEntity implements IHaveHoveringInformation, WandActionable {

	public IncompleteBigCannonBlockEntity(BlockEntityType<? extends IncompleteBigCannonBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (!(this.getBlockState().getBlock() instanceof IncompleteWithItemsCannonBlock incomplete)) return false;
		IncompleteCannonBlockTooltip.addToTooltip(tooltip, isPlayerSneaking, incomplete, this.getBlockState());
		return true;
	}

	@Override
	public InteractionResult onWandUsed(UseOnContext context) {
		if (!(this.getBlockState().getBlock() instanceof IncompleteWithItemsCannonBlock incomplete))
			return InteractionResult.PASS;
		if (!this.getLevel().isClientSide) {
			CompoundTag loadTag = this.saveWithFullMetadata();
			BlockState state = this.getBlockState();
			BlockState boredState = incomplete.getCompleteBlockState(state);
			this.setRemoved();
			BigCannonMaterial material = ((BigCannonBlock) state.getBlock()).getCannonMaterialInLevel(this.level, state, this.worldPosition);
			this.level.setBlock(this.worldPosition, boredState, 11);
			BlockEntity newBE = this.level.getBlockEntity(this.worldPosition);
			if (newBE != null) newBE.load(loadTag);

			for (Direction dir1 : Iterate.directions) {
				if (!this.cannonBehavior().isConnectedTo(dir1)) continue;
				BlockPos pos1 = this.worldPosition.relative(dir1);
				BlockState state1 = this.level.getBlockState(pos1);
				BlockEntity be1 = this.level.getBlockEntity(pos1);
				if (state1.getBlock() instanceof BigCannonBlock cBlock1
					&& cBlock1.getCannonMaterialInLevel(this.level, state1, pos1) == material
					&& be1 instanceof IBigCannonBlockEntity cbe1) {
					Direction dir2 = cBlock1.getFacing(state1);
					if (dir1 == dir2.getOpposite() || cBlock1.isDoubleSidedCannon(state1) && dir1.getAxis() == dir2.getAxis()) {
						Direction opposite = dir1.getOpposite();
						cbe1.cannonBehavior().setConnectedFace(opposite, true);
						if (cbe1 instanceof LayeredBigCannonBlockEntity layered) {
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								layered.setLayerConnectedTo(opposite, layer, true);
							}
						}
						be1.setChanged();
					}
				}
			}
			this.level.playSound(null, this.worldPosition, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
		}
		return InteractionResult.sidedSuccess(this.getLevel().isClientSide);
	}

}
