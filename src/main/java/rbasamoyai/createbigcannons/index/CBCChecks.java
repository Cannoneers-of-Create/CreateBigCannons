package rbasamoyai.createbigcannons.index;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.BlockMovementChecks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.ExtendsCannonMount;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.IAutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech.ScrewBreechBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;

public class CBCChecks {

	private static BlockMovementChecks.CheckResult attachedCheckCannons(BlockState state, Level level, BlockPos pos, Direction attached) {
		if (!(state.getBlock() instanceof BigCannonBlock cannonBlock)) return BlockMovementChecks.CheckResult.PASS;
		BlockPos otherPos = pos.relative(attached);
		BlockState attachedState = level.getBlockState(otherPos);
		if (!(attachedState.getBlock() instanceof BigCannonBlock otherBlock))
			return BlockMovementChecks.CheckResult.PASS;

		if (!(level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe) || !(level.getBlockEntity(otherPos) instanceof IBigCannonBlockEntity cbe1)) {
			return BlockMovementChecks.CheckResult.PASS;
		}

		boolean result = cbe.cannonBehavior().isConnectedTo(attached) && cbe1.cannonBehavior().isConnectedTo(attached.getOpposite());

		if (cannonBlock instanceof ScrewBreechBlock) {
			result &= cannonBlock.getOpeningType(level, state, pos) != BigCannonEnd.OPEN;
		}
		if (otherBlock instanceof ScrewBreechBlock) {
			result &= otherBlock.getOpeningType(level, attachedState, otherPos) != BigCannonEnd.OPEN && attachedState.getValue(BlockStateProperties.FACING) == attached;
		}
		return BlockMovementChecks.CheckResult.of(result);
	}

	private static BlockMovementChecks.CheckResult attachedCheckCannonLoader(BlockState state, Level level, BlockPos pos, Direction attached) {
		BlockState rootState = level.getBlockState(pos.relative(attached));
		state = IBigCannonBlockEntity.getInnerCannonBlockState(level, pos, state);
		rootState = IBigCannonBlockEntity.getInnerCannonBlockState(level, pos.relative(attached), rootState);

		if (CBCBlocks.CANNON_LOADER.has(state)) {
			Direction facing = state.getValue(BlockStateProperties.FACING);
			if (CBCBlocks.RAM_HEAD.has(rootState) || CBCBlocks.WORM_HEAD.has(rootState)) {
				Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
				return BlockMovementChecks.CheckResult.of(facing == facing1 && facing == attached);
			}
			if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
				Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
				return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
			}
		}
		if (AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
			Direction facing = state.getValue(BlockStateProperties.FACING);
			if (CBCBlocks.RAM_HEAD.has(rootState) || CBCBlocks.WORM_HEAD.has(rootState)) {
				Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
				return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing1 == attached);
			}
			if (CBCBlocks.CANNON_LOADER.has(rootState)) {
				Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
				return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
			}
			if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
				Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
				return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
			}
		}
		if (CBCBlocks.WORM_HEAD.has(state) || CBCBlocks.RAM_HEAD.has(state)) {
			Direction facing = state.getValue(BlockStateProperties.FACING);
			if (CBCBlocks.CANNON_LOADER.has(rootState)) {
				Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
				return BlockMovementChecks.CheckResult.of(facing == facing1 && facing == attached.getOpposite());
			}
			if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
				Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
				return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing == attached.getOpposite());
			}
		}

		return BlockMovementChecks.CheckResult.PASS;
	}

	private static BlockMovementChecks.CheckResult overridePushReactionCheck(BlockState state, Level level, BlockPos pos) {
		if (state.getBlock() instanceof BigCannonBlock cBlock)
			return BlockMovementChecks.CheckResult.of(!cBlock.isImmovable(state));
		if (state.getBlock() instanceof AutocannonBlock) return BlockMovementChecks.CheckResult.SUCCESS;
		return BlockMovementChecks.CheckResult.PASS;
	}

	private static BlockMovementChecks.CheckResult unmovableCannonMount(BlockState state, Level level, BlockPos pos) {
		if (!(level.getBlockEntity(pos) instanceof ExtendsCannonMount extension))
			return BlockMovementChecks.CheckResult.PASS;
		CannonMountBlockEntity mount = extension.getCannonMount();
		return mount != null && mount.isRunning() ? BlockMovementChecks.CheckResult.FAIL : BlockMovementChecks.CheckResult.PASS;
	}

	private static BlockMovementChecks.CheckResult attachedMountBlocks(BlockState state, Level level, BlockPos pos, Direction attached) {
		// TODO: upside down cannon mount
		BlockPos attachedPos = pos.relative(attached);
		BlockState attachedTo = level.getBlockState(attachedPos);
		if (CBCBlocks.CANNON_MOUNT.has(state) && level.getBlockEntity(attachedPos) instanceof ExtendsCannonMount extension) {
			CannonMountBlockEntity mount = extension.getCannonMount();
			return mount != null && mount.getBlockPos().equals(pos) ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.PASS;
		}
		if (CBCBlocks.CANNON_MOUNT.has(attachedTo) && level.getBlockEntity(pos) instanceof ExtendsCannonMount extension) {
			CannonMountBlockEntity mount = extension.getCannonMount();
			return mount != null && mount.getBlockPos().equals(attachedPos) ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.PASS;
		}
		return BlockMovementChecks.CheckResult.PASS;
	}

	private static BlockMovementChecks.CheckResult attachedCheckAutocannons(BlockState state, Level level, BlockPos pos, Direction attached) {
		if (!(state.getBlock() instanceof AutocannonBlock)) return BlockMovementChecks.CheckResult.PASS;
		BlockPos otherPos = pos.relative(attached);
		BlockState attachedState = level.getBlockState(otherPos);
		if (!(attachedState.getBlock() instanceof AutocannonBlock)) return BlockMovementChecks.CheckResult.PASS;

		if (!(level.getBlockEntity(pos) instanceof IAutocannonBlockEntity cbe) || !(level.getBlockEntity(otherPos) instanceof IAutocannonBlockEntity cbe1)) {
			return BlockMovementChecks.CheckResult.PASS;
		}

		boolean result = cbe.cannonBehavior().isConnectedTo(attached) && cbe1.cannonBehavior().isConnectedTo(attached.getOpposite());
		return BlockMovementChecks.CheckResult.of(result);
	}

	public static void register() {
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckCannons);
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckCannonLoader);
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedMountBlocks);
		BlockMovementChecks.registerAttachedCheck(CBCChecks::attachedCheckAutocannons);
		BlockMovementChecks.registerMovementAllowedCheck(CBCChecks::overridePushReactionCheck);
		BlockMovementChecks.registerMovementAllowedCheck(CBCChecks::unmovableCannonMount);
	}

}
