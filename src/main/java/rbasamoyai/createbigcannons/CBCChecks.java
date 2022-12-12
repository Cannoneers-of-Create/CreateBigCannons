package rbasamoyai.createbigcannons;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks;
import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks.CheckResult;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannonmount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.IAutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.cannons.cannonend.ScrewBreechBlock;

public class CBCChecks {

	private static CheckResult attachedCheckCannons(BlockState state, Level level, BlockPos pos, Direction attached) {
		if (!(state.getBlock() instanceof CannonBlock cannonBlock)) return CheckResult.PASS;
		BlockPos otherPos = pos.relative(attached);
		BlockState attachedState = level.getBlockState(otherPos);
		if (!(attachedState.getBlock() instanceof CannonBlock otherBlock)) return CheckResult.PASS;
		
		if (!(level.getBlockEntity(pos) instanceof ICannonBlockEntity cbe) || !(level.getBlockEntity(otherPos) instanceof ICannonBlockEntity cbe1)) {
			return CheckResult.PASS;
		}
		
		boolean result = cbe.cannonBehavior().isConnectedTo(attached) && cbe1.cannonBehavior().isConnectedTo(attached.getOpposite());
		
		if (cannonBlock instanceof ScrewBreechBlock) {
			result &= cannonBlock.getOpeningType(level, state, pos) != CannonEnd.OPEN;
		}
		if (otherBlock instanceof ScrewBreechBlock) {
			result &= otherBlock.getOpeningType(level, attachedState, otherPos) != CannonEnd.OPEN && attachedState.getValue(BlockStateProperties.FACING) == attached;
		}
		return CheckResult.of(result);
	}
	
	private static CheckResult attachedCheckCannonLoader(BlockState state, Level level, BlockPos pos, Direction attached) {
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
	
	private static CheckResult overridePushReactionCheck(BlockState state, Level level, BlockPos pos) {
		if (state.getBlock() instanceof CannonBlock cBlock) return CheckResult.of(!cBlock.isImmovable(state));
		if (state.getBlock() instanceof AutocannonBlock) return CheckResult.SUCCESS;
		return CheckResult.PASS;
	}
	
	private static CheckResult unmovableCannonMount(BlockState state, Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof CannonMountBlockEntity mount ? mount.isRunning() ? CheckResult.FAIL : CheckResult.PASS : CheckResult.PASS;
	}
	
	private static CheckResult attachedMountBlocks(BlockState state, Level level, BlockPos pos, Direction attached) {
		BlockState attachedTo = level.getBlockState(pos.relative(attached));
		if (CBCBlocks.CANNON_MOUNT.has(state) && CBCBlocks.YAW_CONTROLLER.has(attachedTo)) {
			return attached == Direction.DOWN ? CheckResult.SUCCESS : CheckResult.PASS;
		}
		if (CBCBlocks.CANNON_MOUNT.has(attachedTo) && CBCBlocks.YAW_CONTROLLER.has(state)) {
			return attached == Direction.UP ? CheckResult.SUCCESS : CheckResult.PASS;
		}
		return CheckResult.PASS;
	}

	private static CheckResult attachedCheckAutocannons(BlockState state, Level level, BlockPos pos, Direction attached) {
		if (!(state.getBlock() instanceof AutocannonBlock)) return CheckResult.PASS;
		BlockPos otherPos = pos.relative(attached);
		BlockState attachedState = level.getBlockState(otherPos);
		if (!(attachedState.getBlock() instanceof AutocannonBlock)) return CheckResult.PASS;

		if (!(level.getBlockEntity(pos) instanceof IAutocannonBlockEntity cbe) || !(level.getBlockEntity(otherPos) instanceof IAutocannonBlockEntity cbe1)) {
			return CheckResult.PASS;
		}

		boolean result = cbe.cannonBehavior().isConnectedTo(attached) && cbe1.cannonBehavior().isConnectedTo(attached.getOpposite());
		return CheckResult.of(result);
	}
	
	public static void register() {
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckCannons);
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckCannonLoader);
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedMountBlocks);
		BlockMovementChecks.registerMovementAllowedCheck(CBCChecks::overridePushReactionCheck);
		BlockMovementChecks.registerMovementAllowedCheck(CBCChecks::unmovableCannonMount);
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckAutocannons);
	}
	
}
