package rbasamoyai.createbigcannons.cannons;

import java.util.Optional;

import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks;
import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks.CheckResult;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannons.cannonend.ScrewBreechBlock;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CannonChecks {

	public static CheckResult attachedCheck(BlockState state, Level level, BlockPos pos, Direction attached) {
		if (!CBCConfigs.SERVER.cannons.cannonsBlocksAreAttached.get() || !(state.getBlock() instanceof CannonBlock)) {
			return CheckResult.PASS;
		}
		CannonBlock cannonBlock = (CannonBlock) state.getBlock();
		BlockState attachedState = level.getBlockState(pos.relative(attached));
		if (!(attachedState.getBlock() instanceof CannonBlock)) return CheckResult.PASS;
		CannonBlock otherBlock = (CannonBlock) attachedState.getBlock();
		
		Direction.Axis axis = cannonBlock.getAxis(state);
		boolean result = cannonBlock.getCannonMaterial() == otherBlock.getCannonMaterial() && axis == otherBlock.getAxis(attachedState) && axis == attached.getAxis();
		Optional<Direction> facing = cannonBlock.getFacing(state);
		if (facing.isPresent()) {
			result &= facing.get() == attached;
		}
		
		if (cannonBlock instanceof ScrewBreechBlock) {
			result &= !ScrewBreechBlock.isOpen(state);
		}
		if (otherBlock instanceof ScrewBreechBlock) {
			result &= !ScrewBreechBlock.isOpen(attachedState) && attachedState.getValue(BlockStateProperties.FACING) == attached;
		}
		return CheckResult.of(result);
	}
	
	public static void register() {
		BlockMovementChecks.registerAttachedCheck(CannonChecks::attachedCheck);
	}
	
}
