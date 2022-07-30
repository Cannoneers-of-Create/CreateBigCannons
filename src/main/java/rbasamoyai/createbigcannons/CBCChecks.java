package rbasamoyai.createbigcannons;

import java.util.Optional;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks;
import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks.CheckResult;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.cannonend.ScrewBreechBlock;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CBCChecks {

	public static CheckResult attachedCheckCannons(BlockState state, Level level, BlockPos pos, Direction attached) {
		if (!CBCConfigs.SERVER.cannons.cannonsBlocksAreAttached.get() || !(state.getBlock() instanceof CannonBlock cannonBlock)) {
			return CheckResult.PASS;
		}
		BlockState attachedState = level.getBlockState(pos.relative(attached));
		if (!(attachedState.getBlock() instanceof CannonBlock otherBlock)) return CheckResult.PASS;
		
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
	
	public static CheckResult attachedCheckCannonLoader(BlockState state, Level level, BlockPos pos, Direction attached) {
		if (CBCBlocks.CANNON_LOADER.has(state)
			|| AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
			return CheckResult.of(state.getValue(BlockStateProperties.FACING).getAxis() == attached.getAxis());
		}
		if (CBCBlocks.WORM_HEAD.has(state)
			|| CBCBlocks.RAM_HEAD.has(state)) {
			return CheckResult.of(state.getValue(BlockStateProperties.FACING) == attached.getOpposite());
		}
		
		return CheckResult.PASS;
	}
	
	public static void register() {
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckCannons);
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckCannonLoader);
	}
	
}
