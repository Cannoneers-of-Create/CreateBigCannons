package rbasamoyai.createbigcannons.mixin.compat.create;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.api.contraption.BlockMovementChecks;
import com.simibubi.create.content.contraptions.chassis.ChassisBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(ChassisBlockEntity.class)
public abstract class ChassisBlockEntityMixin extends SmartBlockEntity {

	@Shadow public abstract int getRange();

	ChassisBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) { super(type, pos, state); }

	@WrapMethod(method = "getIncludedBlockPositionsRadial", remap = false)
	private List<BlockPos> createbigcannons$getIncludedBlockPositionsRadial$0(Direction forcedMovement, boolean visualize,
                                                                              Operation<List<BlockPos>> original,
                                                                              @Share("cannonFlags") LocalRef<Set<BlockPos>> cannonFlagsRef) {
		cannonFlagsRef.set(new HashSet<>());
        return original.call(forcedMovement, visualize);
    }

	@ModifyExpressionValue(method = "getIncludedBlockPositionsRadial",
			at = @At(value = "INVOKE", target = "Lcom/simibubi/create/api/contraption/BlockMovementChecks;isNotSupportive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"))
	private boolean createbigcannons$getIncludedBlockPositionsRadial$1(boolean original, Direction forcedMovement, boolean visualize,
																	   @Local(name = "positions") List<BlockPos> positions,
																	   @Local(name = "searchPos") BlockPos searchPos,
																	   @Local(name = "searchedState") BlockState searchedState,
																	   @Local(name = "offset") Direction offset,
																	   @Share("cannonFlags") LocalRef<Set<BlockPos>> cannonFlagsRef) {
		return original || createbigcannons$checkBlock(this.level, forcedMovement, cannonFlagsRef.get(), searchPos, searchedState, offset, positions);
	}

	@WrapMethod(method = "getIncludedBlockPositionsLinear", remap = false)
	private List<BlockPos> createbigcannons$getIncludedBlockPositionsLinear$0(Direction forcedMovement, boolean visualize,
                                                                              Operation<List<BlockPos>> original,
                                                                              @Share("cannonFlags") LocalRef<Set<BlockPos>> cannonFlagsRef) {
		cannonFlagsRef.set(new HashSet<>());
        return original.call(forcedMovement, visualize);
    }

	@ModifyExpressionValue(method = "getIncludedBlockPositionsLinear",
		at = @At(value = "INVOKE", target = "Lcom/simibubi/create/api/contraption/BlockMovementChecks;isNotSupportive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"))
	private boolean createbigcannons$getIncludedBlockPositionsLinear$1(boolean original, Direction forcedMovement, boolean visualize,
																	   @Local(name = "positions") List<BlockPos> positions,
																	   @Local(name = "current") BlockPos currentPos,
																	   @Local(name = "currentState") BlockState currentState,
																	   @Local(name = "facing") Direction facing,
																	   @Share("cannonFlags") LocalRef<Set<BlockPos>> cannonFlagsRef) {
		return original || createbigcannons$checkBlock(this.level, forcedMovement, cannonFlagsRef.get(), currentPos, currentState, facing, positions);
	}

	@Unique
	private static boolean createbigcannons$checkBlock(Level level, @Nullable Direction forcedMovement, Set<BlockPos> cannonFlags,
                                                       BlockPos currentPos, BlockState currentState, Direction facing, List<BlockPos> positions) {
		if (forcedMovement == null) return false;
		Direction.Axis forcedAxis = forcedMovement.getAxis();
		BlockPos connectedPos = currentPos.relative(facing);
		BlockState connectedState = level.getBlockState(connectedPos);

		boolean isCannon = connectedState.getBlock() instanceof BigCannonBlock cBlock && cBlock.getFacing(connectedState).getAxis() == forcedAxis
			&& level.getBlockEntity(connectedPos) instanceof IBigCannonBlockEntity;
		if (isCannon && (cannonFlags.contains(currentPos) || facing.getAxis() != forcedAxis && !(currentState.getBlock() instanceof BigCannonBlock))) {
			cannonFlags.add(connectedPos);
			return false;
		}
		BlockState currentContainedState = ContraptionRemix.getInnerCannonState(level, currentState, currentPos, forcedMovement);
		if (CBCBlocks.WORM_HEAD.has(currentContainedState) && currentContainedState.getValue(BlockStateProperties.FACING) == forcedMovement
			|| CBCBlocks.RAM_HEAD.has(currentContainedState) && currentContainedState.getValue(BlockStateProperties.FACING) == forcedMovement.getOpposite()) {
			return true;
		}
		BlockState connectedContainedState = ContraptionRemix.getInnerCannonState(level, connectedState, connectedPos, forcedMovement);
		if (IBigCannonBlockEntity.isValidMunitionState(forcedAxis, currentContainedState)) {
			if (connectedContainedState.isAir())
				return true;
			if (BlockMovementChecks.isBlockAttachedTowards(connectedContainedState, level, connectedPos, facing.getOpposite()))
				return false;
			return facing != forcedMovement;
		}
		if (isCannon && connectedContainedState.isAir()) {
			cannonFlags.add(connectedPos);
		}
		return false;
	}


}
