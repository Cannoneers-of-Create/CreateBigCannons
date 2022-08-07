package rbasamoyai.createbigcannons;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks;
import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks.CheckResult;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.ScrewBreechBlock;

public class CBCChecks {

	public static CheckResult attachedCheckCannons(BlockState state, Level level, BlockPos pos, Direction attached) {
		if (!(state.getBlock() instanceof CannonBlock cannonBlock)) return CheckResult.PASS;
		BlockState attachedState = level.getBlockState(pos.relative(attached));
		if (!(attachedState.getBlock() instanceof CannonBlock otherBlock)) return CheckResult.PASS;
		
		if (!(level.getBlockEntity(pos) instanceof ICannonBlockEntity cbe) || !(level.getBlockEntity(pos.relative(attached)) instanceof ICannonBlockEntity cbe1)) {
			return CheckResult.PASS;
		}
		
		boolean result = cbe.cannonBehavior().isConnectedTo(attached.getOpposite()) && cbe1.cannonBehavior().isConnectedTo(attached);
		
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
	
	public static CheckResult overridePushReactionCheck(BlockState state, Level level, BlockPos pos) {
		return state.getBlock() instanceof CannonBlock ? CheckResult.SUCCESS : CheckResult.PASS;
	}
	
	public static void register() {
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckCannons);
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckCannonLoader);
		BlockMovementChecks.registerMovementAllowedCheck(CBCChecks::overridePushReactionCheck);
	}
	
}
