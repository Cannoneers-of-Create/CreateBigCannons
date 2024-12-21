package rbasamoyai.createbigcannons.remix;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.BlockMovementChecks;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.chassis.ChassisBlockEntity;
import com.simibubi.create.content.contraptions.gantry.GantryContraption;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonHeadBlock;
import com.simibubi.create.content.contraptions.pulley.PulleyContraption;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.infrastructure.config.AllConfigs;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.material.PushReaction;
import rbasamoyai.createbigcannons.cannon_loading.CBCModifiedContraptionRegistry;
import rbasamoyai.createbigcannons.cannon_loading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.cannons.CannonContraptionProviderBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

public class ContraptionRemix {

	public static boolean customBlockPlacement(Contraption contraption, LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(contraption) && contraption.entity != null) {
			BlockPos entityAnchor = BlockPos.containing(contraption.entity.getAnchorVec().add(0.5d, 0.5d, 0.5d));

			BlockPos blockPos = pos.subtract(entityAnchor);
			StructureBlockInfo blockInfo = contraption.getBlocks().get(blockPos);
			BlockEntity blockEntity1 = levelAccessor.getBlockEntity(pos);
			BlockState intersectState = levelAccessor.getBlockState(pos);

			if (CBCModifiedContraptionRegistry.isFragileContraption(contraption)) {
				boolean isBrokenDisassembly = ((HasFragileContraption) contraption).createbigcannons$isBrokenDisassembly();
				if (isBrokenDisassembly && !intersectState.isAir() && blockInfo != null && !blockInfo.state().isAir()) {
					BlockEntity contraptionBE = blockInfo.nbt() == null ? null : BlockEntity.loadStatic(BlockPos.ZERO, blockInfo.state(), blockInfo.nbt());
					Block.dropResources(blockInfo.state(), contraption.entity.level(), pos, contraptionBE, null, ItemStack.EMPTY);
					levelAccessor.levelEvent(2001, pos, Block.getId(blockInfo.state()));
					levelAccessor.gameEvent(contraption.entity, GameEvent.BLOCK_DESTROY, pos);
					return true;
				}
			}
			if (blockEntity1 instanceof IBigCannonBlockEntity cannon) {
				if (cannon.cannonBehavior().tryLoadingBlock(blockInfo)) {
					cannon.cannonBehavior().blockEntity.notifyUpdate();
					return true;
				}
				return false;
			}
		}
		return false;
	}

	public static boolean customBlockRemoval(Contraption contraption, LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(contraption)) {
			BlockState intersectState = levelAccessor.getBlockState(pos);
			if (intersectState.getBlock() instanceof BigCannonBlock
				&& levelAccessor.getBlockEntity(pos) instanceof IBigCannonBlockEntity cannon
				&& state != intersectState) {
				cannon.cannonBehavior().removeBlock();
				cannon.cannonBehavior().blockEntity.notifyUpdate();
				return true;
			}
		}
		return false;
	}

	public static void customChecks(Contraption contraption, Level level, BlockPos pos, BlockState state,
									@Nullable Direction forcedDirection, Queue<BlockPos> frontier, Set<BlockPos> visited,
									CallbackInfoReturnable<Boolean> cir) {
		if (CBCBlocks.CANNON_MOUNT.has(state)) {
			Direction mountFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
			Direction vertical = state.getValue(BlockStateProperties.VERTICAL_DIRECTION);
			BlockPos assemblyPos = pos.relative(vertical, -2);
			if (level.isOutsideBuildHeight(assemblyPos)) return;
			BlockState state1 = level.getBlockState(assemblyPos);
			if (!(state1.getBlock() instanceof CannonContraptionProviderBlock provider)) return;
			Direction facing = provider.getFacing(state1);
			if (facing.getAxis().isVertical() || facing.getAxis() == mountFacing.getAxis()) {
				if (CBCModifiedContraptionRegistry.canLoadBigCannon(contraption))
					simpleMarking((Contraption & CanLoadBigCannon) contraption, level, assemblyPos, Direction.DOWN, forcedDirection);
				frontier.add(assemblyPos);
				return;
			}
		}
		if (CBCBlocks.CANNON_CARRIAGE.has(state)) {
			Direction mountFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
			BlockPos assemblyPos = pos.above();
			if (level.isOutsideBuildHeight(assemblyPos)) return;
			BlockState state1 = level.getBlockState(assemblyPos);
			if (!(state1.getBlock() instanceof CannonContraptionProviderBlock provider)) return;
			Direction facing = provider.getFacing(state1);
			if (facing.getAxis().isVertical() || facing.getAxis() == mountFacing.getAxis()) {
				frontier.add(assemblyPos);
				return;
			}
		}
	}

	public static <T extends Contraption & CanLoadBigCannon> void simpleMarking(T contraption, Level level, BlockPos pos,
																				Direction attached, @Nullable Direction forcedDirection) {
		if (forcedDirection == null) return;
		BlockPos local = contraption.createbigcannons$toLocalPos(pos);
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof BigCannonBlock cBlock
			&& (cBlock.getFacing(state).getAxis() != attached.getAxis() || cBlock.getFacing(state).getAxis() != forcedDirection.getAxis())) {
			if (!contraption.getBlocks().containsKey(local)) {
				contraption.getBlocks().put(local, new StructureBlockInfo(BlockPos.ZERO, Blocks.AIR.defaultBlockState(), null));
			}
			StructureBlockInfo oldInfo = contraption.getBlocks().get(local);
			if (oldInfo.state().getBlock() instanceof BigCannonBlock) return;
			CompoundTag tag = oldInfo.nbt() == null ? new CompoundTag() : oldInfo.nbt().copy();
			tag.putBoolean("createbigcannons:add_as_cannon", true);
			contraption.getBlocks().put(local, new StructureBlockInfo(oldInfo.pos(), oldInfo.state(), tag));
		} else if (IBigCannonBlockEntity.isValidMunitionState(forcedDirection.getAxis(), state)
			&& forcedDirection.getAxis() != attached.getAxis()) {
			addPosToCannonColliders(contraption, local, forcedDirection);
		}
	}

	public static <T extends Contraption & CanLoadBigCannon> void stickerMarking(T contraption, Level level, BlockPos pos,
																				 Direction attached, @Nullable Direction forcedDirection) {
		if (!BlockMovementChecks.isNotSupportive(level.getBlockState(pos), attached.getOpposite()))
			ContraptionRemix.simpleMarking(contraption, level, pos, attached, forcedDirection);
	}

	public static <T extends Contraption & CanLoadBigCannon> void pistonMarking(T contraption, Level level, BlockPos pos, BlockState state) {
		Direction forcedDirection = contraption.createbigcannons$getAssemblyMovementDirection(level);
		Direction direction = state.getValue(BlockStateProperties.FACING);
		BlockPos offset = pos.relative(direction);
		if ((forcedDirection == null || forcedDirection != direction) && !MechanicalPistonBlock.isStickyPiston(state)) return;
		simpleMarking(contraption, level, offset, direction, forcedDirection);
	}

	public static <T extends Contraption & CanLoadBigCannon> void pistonHeadMarking(T contraption, Level level, BlockPos pos, BlockState state) {
		Direction forcedDirection = contraption.createbigcannons$getAssemblyMovementDirection(level);
		Direction direction = state.getValue(BlockStateProperties.FACING);
		BlockPos offset = pos.relative(direction);
		if ((forcedDirection == null || forcedDirection != direction) && state.getValue(MechanicalPistonHeadBlock.TYPE) != PistonType.STICKY) return;
		simpleMarking(contraption, level, offset, direction, forcedDirection);
	}

	public static <T extends Contraption & CanLoadBigCannon> void gantryCarriageMarking(T contraption, Level level, BlockPos pos, BlockState state) {
		Direction forcedDirection = contraption.createbigcannons$getAssemblyMovementDirection(level);
		Direction direction = state.getValue(BlockStateProperties.FACING);
		BlockPos offset = pos.relative(direction);
		simpleMarking(contraption, level, offset, direction, forcedDirection);
	}

	public static <T extends Contraption & CanLoadBigCannon> void chassisMarking(T contraption, Level level, List<BlockPos> addedPositions,
																				 Queue<BlockPos> frontier, Set<BlockPos> visited,
																				 @Nullable Direction forcedDirection, ChassisBlockEntity chassisBE) {
		if (forcedDirection == null) return;
		Direction.Axis forcedAxis = forcedDirection.getAxis();
		Set<BlockPos> positionSet = new HashSet<>(addedPositions);
		Set<BlockPos> firstCannonBlocks = new HashSet<>();
		Set<BlockPos> finalCannonBlocks = new HashSet<>();
		Set<BlockPos> recheckPositions = new HashSet<>();

		BlockPos rootPos = chassisBE.getBlockPos();
		positionSet.remove(rootPos);

		for (BlockPos pos : positionSet) {
			BlockPos local = contraption.createbigcannons$toLocalPos(pos);
			BlockState state = level.getBlockState(pos);

			if (state.getBlock() instanceof BigCannonBlock cBlock && level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe) {
				if (cBlock.getFacing(state).getAxis() != forcedAxis) {
					finalCannonBlocks.add(pos);
					continue;
				}
				boolean addAsCannon = cbe.cannonBehavior().block().state().isAir();
				if (!addAsCannon) {
					for (Direction dir1 : Iterate.directions) {
						BlockPos offset = pos.relative(dir1);
						if (!rootPos.equals(offset) && !positionSet.contains(offset)) continue;
						if (dir1.getAxis() != forcedAxis || firstCannonBlocks.contains(offset)) {
							addAsCannon = true;
							break;
						}
					}
				}
				if (addAsCannon) {
					firstCannonBlocks.add(pos);
				} else {
					recheckPositions.add(pos);
				}
			} else if (IBigCannonBlockEntity.isValidMunitionState(forcedAxis, state)) {
				for (Direction dir1 : Iterate.directions) {
					if (dir1.getAxis() == forcedAxis) continue;
					BlockPos offset = pos.relative(dir1);
					if (!rootPos.equals(offset) && !positionSet.contains(offset)) continue;
					addPosToCannonColliders(contraption, local, forcedDirection);
					break;
				}
			}
		}
		Set<BlockPos> localVisited = new HashSet<>();
		Queue<BlockPos> localFrontier = new LinkedList<>(firstCannonBlocks);
		for (int i = 0; i < 300; ++i) {
			if (localFrontier.isEmpty()) break;
			BlockPos pos = localFrontier.poll();
			if (localVisited.contains(pos)) continue;
			localVisited.add(pos);
			finalCannonBlocks.add(pos);

			BlockState state = level.getBlockState(pos);
			if (!(state.getBlock() instanceof BigCannonBlock cBlock) || cBlock.getFacing(state).getAxis() != forcedAxis) continue;
			for (Direction dir : Iterate.directionsInAxis(forcedAxis)) {
				BlockPos offsetPos = pos.relative(dir);
				if (!recheckPositions.contains(offsetPos) || localVisited.contains(offsetPos)) continue;
				localFrontier.add(offsetPos);
			}
		}
		for (BlockPos pos : finalCannonBlocks) {
			BlockPos local = contraption.createbigcannons$toLocalPos(pos);
			if (!contraption.getBlocks().containsKey(local)) {
				contraption.getBlocks().put(local, new StructureBlockInfo(BlockPos.ZERO, Blocks.AIR.defaultBlockState(), null));
			}
			StructureBlockInfo oldInfo = contraption.getBlocks().get(local);
			if (oldInfo.state().getBlock() instanceof BigCannonBlock) continue;
			CompoundTag tag = oldInfo.nbt() == null ? new CompoundTag() : oldInfo.nbt().copy();
			tag.putBoolean("createbigcannons:add_as_cannon", true);
			contraption.getBlocks().put(local, new StructureBlockInfo(oldInfo.pos(), oldInfo.state(), tag));
		}
	}

	public static <T extends Contraption & CanLoadBigCannon> void moveLoaderBlocks(T contraption, Level level,
												@Nullable Direction forcedDirection, Queue<BlockPos> frontier,
												Set<BlockPos> visited, BlockPos pos, BlockState state) {
		BlockEntity be = level.getBlockEntity(pos);
		state = IBigCannonBlockEntity.getInnerCannonBlockState(level, pos, state);
		if (state.getBlock() instanceof BigCannonBlock && be instanceof IBigCannonBlockEntity cbe) {
			StructureBlockInfo blockInfo = cbe.cannonBehavior().block();
			if (!blockInfo.state().isAir()) state = blockInfo.state();
		}

		if (CBCBlocks.WORM_HEAD.has(state)
			&& forcedDirection != null
			&& state.getValue(BlockStateProperties.FACING) == forcedDirection.getOpposite()) {
			Direction facing = state.getValue(BlockStateProperties.FACING);
			BlockPos offset = pos.relative(facing);
			BlockState offsetState = level.getBlockState(offset);
			if (offsetState.getBlock() instanceof BigCannonBlock cBlock
				&& cBlock.getFacing(offsetState).getAxis() == facing.getAxis()
				&& level.getBlockEntity(offset) instanceof IBigCannonBlockEntity cbe) {
				StructureBlockInfo containedInfo = cbe.cannonBehavior().block();
				if (containedInfo.state().getBlock() instanceof BigCannonMunitionBlock) offsetState = containedInfo.state();
			}
			if (offsetState.getBlock() instanceof BigCannonMunitionBlock mBlock
				&& mBlock.getAxis(offsetState) == forcedDirection.getAxis()) {
				frontier.add(offset);
			}
		}
	}

	public static <T extends Contraption & CanLoadBigCannon> boolean getStickFlag(T contraption, Level level, BlockPos pos,
																				  BlockPos offsetPos, BlockState state,
																				  BlockState offsetState, Direction offset,
																				  @Nullable Direction forcedDirection,
																				  boolean blockAttachedTowardsFace) {
		if (blockAttachedTowardsFace) {
			BlockPos localOffsetPos = contraption.createbigcannons$toLocalPos(offsetPos);
			if (contraption.getBlocks().containsKey(localOffsetPos)) {
				StructureBlockInfo offsetInfo = contraption.getBlocks().get(localOffsetPos);
				if (BlockMovementChecks.isBlockAttachedTowards(offsetInfo.state(), level, offsetPos, offset.getOpposite())) return true;
			}
			Direction.Axis offsetAxis = offset.getAxis();
			if (offsetState.getBlock() instanceof BigCannonBlock cBlock
				&& cBlock.getFacing(offsetState).getAxis() == offsetAxis
				&& level.getBlockEntity(offsetPos) instanceof IBigCannonBlockEntity cbe) {
				BlockPos localPos = contraption.createbigcannons$toLocalPos(pos);
				if (contraption.getBlocks().containsKey(localPos)) {
					StructureBlockInfo info = contraption.getBlocks().get(localPos);
					if (info.nbt() != null && info.nbt().contains("createbigcannons:add_as_cannon")) return true;
				}
				StructureBlockInfo offsetInfo = cbe.cannonBehavior().block();
				BlockPos prevPos = pos.relative(offset.getOpposite());
				BlockPos localPrev = contraption.createbigcannons$toLocalPos(prevPos);

				StructureBlockInfo prevInfo = null;
				if (contraption.getBlocks().containsKey(localPrev)) {
					prevInfo = contraption.getBlocks().get(localPrev);
				} else if (contraption instanceof PulleyContraption && pos.equals(contraption.anchor) && offset == Direction.DOWN) {
					prevInfo = new StructureBlockInfo(BlockPos.ZERO, AllBlocks.PULLEY_MAGNET.getDefaultState(), null);
				}
				BlockState currentState = state;
				if (state.getBlock() instanceof BigCannonBlock cBlock1
					&& cBlock1.getFacing(state).getAxis() == offsetAxis
					&& level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe1) {
					currentState = cbe1.cannonBehavior().block().state();
				}
				if (IBigCannonBlockEntity.isValidMunitionState(offsetAxis, currentState)
					&& prevInfo != null && IBigCannonBlockEntity.isValidMunitionState(offsetAxis, prevInfo.state())) {
					if (BlockMovementChecks.isBlockAttachedTowards(offsetInfo.state(), level, offsetPos, offset.getOpposite())) return true;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}

		boolean brittle = BlockMovementChecks.isBrittle(offsetState);
		boolean canStick = !brittle && ContraptionRemix.canStickTo(state, offsetState) && ContraptionRemix.canStickTo(offsetState, state);
		if (canStick) {
			if (state.getPistonPushReaction() == PushReaction.PUSH_ONLY || offsetState.getPistonPushReaction() == PushReaction.PUSH_ONLY) {
				canStick = false;
			}
			if (BlockMovementChecks.isNotSupportive(state, offset)) {
				canStick = false;
			}
			if (BlockMovementChecks.isNotSupportive(offsetState, offset.getOpposite())) {
				canStick = false;
			}
		}
		if (canStick) return true;
		if (forcedDirection == null) return false;

		Direction.Axis forcedAxis = forcedDirection.getAxis();
		BlockState pushState = offsetState;
		if (offsetState.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(offsetState).getAxis() == forcedAxis
			&& level.getBlockEntity(offsetPos) instanceof IBigCannonBlockEntity cbe) {
			pushState = cbe.cannonBehavior().block().state();
		}
		boolean push = offset == forcedDirection && !BlockMovementChecks.isNotSupportive(state, forcedDirection);
		if ((contraption instanceof GantryContraption || contraption instanceof PulleyContraption && !push)
			&& !IBigCannonBlockEntity.isValidMunitionState(forcedAxis, pushState)) {
			return false;
		}
		if (push
			&& !blockAttachedTowardsFace
			&& offsetState.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(offsetState).getAxis() == forcedAxis
			&& level.getBlockEntity(offsetPos) instanceof IBigCannonBlockEntity cbe
			&& cbe.canLoadBlock(new StructureBlockInfo(BlockPos.ZERO, state, null))) {
			return false;
		}
		return push;
	}

	public static <T extends Contraption & CanLoadBigCannon> boolean handleCannonFrontier(T contraption, Level level,
												BlockPos pos, BlockPos offsetPos, Set<BlockPos> visited,
												Direction offset, @Nullable Direction forcedDirection, boolean stickFlag) {
		if (forcedDirection == null) return false;

		BlockState state = level.getBlockState(pos);
		BlockPos localPos = contraption.createbigcannons$toLocalPos(pos);

		BlockState offsetState = level.getBlockState(offsetPos);
		BlockEntity offsetBE = level.getBlockEntity(offsetPos);
		BlockPos localOffset = contraption.createbigcannons$toLocalPos(offsetPos);

		boolean flag = !stickFlag;
		Direction.Axis forcedAxis = forcedDirection.getAxis();

		if (offsetState.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(offsetState).getAxis() == forcedAxis
			&& offsetBE instanceof IBigCannonBlockEntity cbe) {
			if (forcedAxis != offset.getAxis() && stickFlag) {
				if (!contraption.getBlocks().containsKey(localOffset)) {
					contraption.getBlocks().put(localOffset, new StructureBlockInfo(localOffset, Blocks.AIR.defaultBlockState(), null));
				}
				StructureBlockInfo offsetInfo = contraption.getBlocks().get(localOffset);
				CompoundTag tag = offsetInfo.nbt() == null ? new CompoundTag() : offsetInfo.nbt().copy();
				tag.putBoolean("createbigcannons:add_as_cannon", true);
				contraption.getBlocks().put(localOffset, new StructureBlockInfo(offsetInfo.pos(), offsetInfo.state(), tag));
			} else if (contraption.getBlocks().containsKey(localOffset) && !visited.contains(offsetPos)) {
				flag = !shouldAddAsCannon(contraption, localPos); // Prevent back tracking
			} else if (shouldAddAsCannon(contraption, localPos)) {
				flag = false;
			} else {
				BlockPos prevPos = pos.relative(offset.getOpposite());
				BlockPos localPrev = contraption.createbigcannons$toLocalPos(prevPos);

				StructureBlockInfo prevInfo = null;
				if (contraption.getBlocks().containsKey(localPrev)) {
					prevInfo = contraption.getBlocks().get(localPrev);
				} else if (contraption instanceof PulleyContraption && pos.equals(contraption.anchor) && offset == Direction.DOWN) {
					prevInfo = new StructureBlockInfo(BlockPos.ZERO, AllBlocks.PULLEY_MAGNET.getDefaultState(), null);
				}
				if (!IBigCannonBlockEntity.isValidMunitionState(forcedAxis, state)
					&& state.getBlock() instanceof BigCannonBlock cBlock1
					&& level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe1
					&& cBlock1.getFacing(state).getAxis() == forcedAxis
					&& prevInfo != null
					&& stickFlag) {
					// Determine from previous block if grabbing cannon.
					boolean prevMunition = IBigCannonBlockEntity.isValidMunitionState(forcedAxis, prevInfo.state());
					boolean alignedCannonBlock = prevInfo.state().getBlock() instanceof BigCannonBlock cBlock2
						&& cBlock2.getFacing(prevInfo.state()).getAxis() == forcedAxis;

					BlockState innerState = cbe1.cannonBehavior().block().state();
					boolean filled = IBigCannonBlockEntity.isValidMunitionState(forcedAxis, innerState);
					BlockState innerOffsetState = cbe.cannonBehavior().block().state();

					if (filled && (prevMunition || !alignedCannonBlock)) {
						flag = !IBigCannonBlockEntity.isValidMunitionState(forcedAxis, innerOffsetState);
					}
					state = innerState;
					offsetState = innerOffsetState;
				}
			}
		}

		if (IBigCannonBlockEntity.isValidMunitionState(forcedAxis, offsetState) && forcedAxis != offset.getAxis() && stickFlag) {
			addPosToCannonColliders(contraption, localOffset, forcedDirection);
		}
		offsetState = getInnerCannonState(level, offsetState, offsetPos, forcedDirection);
		if (CBCBlocks.WORM_HEAD.has(state)
			&& state.getValue(BlockStateProperties.FACING) == forcedDirection
			&& IBigCannonBlockEntity.isValidMunitionState(forcedAxis, offsetState)) {
			flag = true;
		}
		return flag;
	}

	@Nullable
	public static <T extends Contraption & CanLoadBigCannon> Pair<StructureBlockInfo, BlockEntity> handleCapture(T contraption,
												Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited,
												Direction forcedDirection, Set<SuperGlueEntity> glueToRemove) {
		BlockPos localPos = contraption.createbigcannons$toLocalPos(pos);
		if (shouldAddAsCannon(contraption, localPos)) {
			StructureBlockInfo info = contraption.getBlocks().get(localPos);
			if (info != null && info.nbt() != null) info.nbt().remove("createbigcannons:add_as_cannon");
			visited.add(pos);
			return null;
		}
		if (forcedDirection == null) return null;

		Direction.Axis forcedAxis = forcedDirection.getAxis();

		BlockState state = level.getBlockState(pos);
		BlockEntity be = level.getBlockEntity(pos);
		if (!(state.getBlock() instanceof BigCannonBlock cBlock)
			|| !(be instanceof IBigCannonBlockEntity cbe)
			|| cBlock.getFacing(state).getAxis() != forcedAxis) return null;

		BigCannonBehavior behavior = cbe.cannonBehavior();
		StructureBlockInfo currentInfo = behavior.block();

		BlockState addedState = currentInfo.state();
		for (Direction offset : Iterate.directions) {
			Direction.Axis offsetAxis = offset.getAxis();
			if (offsetAxis != forcedAxis) continue;
			BlockPos offsetPos = pos.relative(offset);
			BlockPos localOffsetPos = contraption.createbigcannons$toLocalPos(offsetPos);
			BlockState offsetState = level.getBlockState(offsetPos);

			if (contraption.getBlocks().containsKey(localOffsetPos)) {
				StructureBlockInfo offsetInfo = contraption.getBlocks().get(localOffsetPos);
				if (offsetInfo.state().getBlock() instanceof BigCannonBlock cBlock1
					&& cBlock1.getFacing(offsetInfo.state()).getAxis() == forcedAxis) {
					if (!visited.contains(offsetPos)) {
						frontier.remove(offsetPos);
					} else if (behavior.isConnectedTo(offset)) {
						return null;
					}
				} else if (offset == forcedDirection.getOpposite() && !IBigCannonBlockEntity.isValidMunitionState(forcedAxis, offsetInfo)) {
					return null; // Supposed fix: constant collision - problem: pulling on stuff in the wrong way
				}
			} else if (frontier.contains(offsetPos)) {
				if (offsetState.getBlock() instanceof BigCannonBlock
					&& level.getBlockEntity(offsetPos) instanceof IBigCannonBlockEntity cbe1
					&& cbe1.cannonBehavior().isConnectedTo(offset.getOpposite())) {

					BlockPos prevLocalPos = localPos.relative(offset.getOpposite());
					if (!contraption.getBlocks().containsKey(prevLocalPos)) continue;
					StructureBlockInfo otherInfo = cbe1.cannonBehavior().block();
					BlockState otherState = otherInfo.state();
					StructureBlockInfo prevInfo = contraption.getBlocks().get(prevLocalPos);
					if (!IBigCannonBlockEntity.isValidMunitionState(offsetAxis, prevInfo)) {
						if (prevInfo.state().getBlock() instanceof BigCannonBlock || addedState.isAir() || offset == forcedDirection) return null;
					}

					if (addedState.isAir() || otherState.isAir() || !isAttachedCapture(level, offset, pos, offsetPos,
						addedState, otherState, forcedDirection, glueToRemove)) {
						frontier.remove(offsetPos);
					}
					if (CBCBlocks.WORM_HEAD.has(addedState)
						&& IBigCannonBlockEntity.isValidMunitionState(forcedDirection.getAxis(), otherState)) {
						Direction facing = addedState.getValue(BlockStateProperties.FACING);
						if (facing == forcedDirection.getOpposite()) {
							frontier.add(offsetPos);
						} else {
							frontier.remove(offsetPos);
						}
					}
				}
			}
		}
		if (addedState.isAir()) return null;

		visited.remove(pos);
		CompoundTag tag = currentInfo.nbt() == null ? null : currentInfo.nbt().copy();
		BlockEntity newBe = tag != null && tag.contains("id", Tag.TAG_STRING) ? BlockEntity.loadStatic(currentInfo.pos(), currentInfo.state(), tag) : null;
		return Pair.of(new StructureBlockInfo(currentInfo.pos(), currentInfo.state(), tag), newBe);
	}

	private static boolean isAttachedCapture(Level level, Direction offset, BlockPos pos, BlockPos offsetPos,
											 BlockState state, BlockState offsetState, Direction forcedDirection,
											 Set<SuperGlueEntity> glueToRemove) {
		if (SuperGlueEntity.isGlued(level, pos, offset, glueToRemove)) return true;
		if (BlockMovementChecks.isBlockAttachedTowards(offsetState, level, offsetPos, offset.getOpposite())) return true;
		boolean brittle = BlockMovementChecks.isBrittle(offsetState);
		boolean canStick = !brittle && canStickTo(offsetState, state) && canStickTo(state, offsetState);
		if (canStick) {
			if (state.getPistonPushReaction() == PushReaction.PUSH_ONLY
				|| offsetState.getPistonPushReaction() == PushReaction.PUSH_ONLY) {
				canStick = false;
			}
			if (BlockMovementChecks.isNotSupportive(state, offset)) {
				canStick = false;
			}
			if (BlockMovementChecks.isNotSupportive(offsetState, offset.getOpposite())) {
				canStick = false;
			}
		}
		if (canStick) return true;
		return offset == forcedDirection && !BlockMovementChecks.isNotSupportive(state, forcedDirection);
	}

	public static boolean isLoadingCannon(Level level, BlockPos colliderPos, Direction movementDirection,
										  BlockState collidedState, StructureBlockInfo blockInfo) {
		return collidedState.getBlock() instanceof BigCannonBlock cannonBlock
			&& cannonBlock.getFacing(collidedState).getAxis() == movementDirection.getAxis()
			&& level.getBlockEntity(colliderPos) instanceof IBigCannonBlockEntity cbe
			&& cbe.cannonBehavior().canLoadBlock(blockInfo);
	}

	public static <T extends Contraption & HasFragileContraption> void markFragileBlocks(T contraption) {
		Set<BlockPos> fragileBlocks = contraption.createbigcannons$getFragileBlockPositions();
		fragileBlocks.clear();
		for (Map.Entry<BlockPos, StructureBlockInfo> info : contraption.getBlocks().entrySet()) {
			if (IBigCannonBlockEntity.isValidMunitionState(null, info.getValue())) fragileBlocks.add(info.getKey());
		}
	}

	public static <T extends Contraption & HasFragileContraption> void writeFragileBlocks(T contraption, CompoundTag tag) {
		ListTag fragileList = new ListTag();
		for (BlockPos p : contraption.createbigcannons$getFragileBlockPositions())
			fragileList.add(LongTag.valueOf(p.asLong()));
		tag.put("createbigcannons:fragile_blocks", fragileList);
	}

	public static <T extends Contraption & CanLoadBigCannon> void writeCannonLoaderData(T contraption, CompoundTag tag) {
		ListTag cannonColliderList = new ListTag();
		for (BlockPos p : contraption.createbigcannons$getCannonLoadingColliders())
			cannonColliderList.add(LongTag.valueOf(p.asLong()));
		tag.put("createbigcannons:cannon_loading_colliders", cannonColliderList);
	}

	public static <T extends Contraption & HasFragileContraption> void readFragileBlocks(T contraption, CompoundTag tag) {
		Set<BlockPos> fragileBlocks = contraption.createbigcannons$getFragileBlockPositions();
		fragileBlocks.clear();
		for (Tag t : tag.getList("createbigcannons:fragile_blocks", Tag.TAG_LONG)) {
			if (t.getType() == LongTag.TYPE)
				fragileBlocks.add(BlockPos.of(((LongTag) t).getAsLong()));
		}
	}

	public static <T extends Contraption & CanLoadBigCannon> void readCannonLoaderData(T contraption, CompoundTag tag) {
		Set<BlockPos> cannonColliders = contraption.createbigcannons$getCannonLoadingColliders();
		cannonColliders.clear();
		for (Tag t : tag.getList("createbigcannons:cannon_loading_colliders", Tag.TAG_LONG)) {
			if (t.getType() == LongTag.TYPE)
				cannonColliders.add(BlockPos.of(((LongTag) t).getAsLong()));
		}
	}

	public static <T extends Contraption & CanLoadBigCannon> void addPosToCannonColliders(T contraption, BlockPos pos, Direction dir) {
		Set<BlockPos> colliders = contraption.createbigcannons$getCannonLoadingColliders();
		if (colliders.contains(pos.relative(dir))) return;
		colliders.remove(pos.relative(dir.getOpposite()));
		colliders.add(pos);
	}

	public static void removeInnerStateRopes(Level level, BlockPos pos, boolean isMoving) {
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(state).getAxis().isVertical()
			&& level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe) {
			BlockState innerState = cbe.cannonBehavior().block().state();
			if (!AllBlocks.ROPE.has(innerState) && !AllBlocks.PULLEY_MAGNET.has(innerState)) return;

			cbe.cannonBehavior().removeBlock();
			cbe.cannonBehavior().blockEntity.notifyUpdate();
			innerState.onRemove(level, pos, Blocks.AIR.defaultBlockState(), isMoving);
			SoundType soundtype = innerState.getSoundType();
			level.playSound(null, pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
		}
	}

	public static boolean shouldAddAsCannon(Contraption contraption, BlockPos localPos) {
		Map<BlockPos, StructureBlockInfo> blocks = contraption.getBlocks();
		if (!blocks.containsKey(localPos)) return false;
		StructureBlockInfo info = blocks.get(localPos);
		return info.nbt() != null && info.nbt().contains("createbigcannons:add_as_cannon");
	}

	public static void pulleyChecks(PulleyContraption contraption, Level level, BlockPos pos, @Nullable Direction forcedDirection, Queue<BlockPos> frontier) {
		BlockState state = level.getBlockState(pos);
		BlockState pulledState = state;
		if (state.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(state).getAxis().isVertical()
			&& level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe) {
			BlockState innerState = cbe.cannonBehavior().block().state();
			if (innerState.isAir()) {
				frontier.remove(pos);
				return;
			}
			pulledState = innerState;
		}
		if (forcedDirection == Direction.UP) {
			BlockState offsetState = level.getBlockState(pos.above());
			if (offsetState.getBlock() instanceof BigCannonBlock cBlock
				&& cBlock.getFacing(offsetState).getAxis().isVertical()
				&& level.getBlockEntity(pos.above()) instanceof IBigCannonBlockEntity cbe
				&& !cbe.canPushBlock(new StructureBlockInfo(BlockPos.ZERO, pulledState, null))) {
				frontier.remove(pos);
			}
		}
	}

	public static BlockState getInnerCannonState(LevelAccessor level, BlockState state, BlockPos pos, @Nullable Direction direction) {
		if (state.getBlock() instanceof BigCannonBlock cBlock
			&& (direction == null || cBlock.getFacing(state).getAxis() == direction.getAxis())
			&& level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe) {
			StructureBlockInfo info = cbe.cannonBehavior().block();
			return info.state();
		}
		return state;
	}

	public static boolean removeCannonContentsOnBreak(Level level, BlockPos pos, boolean drops) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof IBigCannonBlockEntity cbe)) return false;
		StructureBlockInfo info = cbe.cannonBehavior().block();
		cbe.cannonBehavior().removeBlock();
		cbe.cannonBehavior().blockEntity.notifyUpdate();
		info.state().onRemove(level, pos, Blocks.AIR.defaultBlockState(), false);
		if (drops) Block.dropResources(info.state(), level, pos, null, null, ItemStack.EMPTY);
		SoundType soundtype = info.state().getSoundType();
		level.playSound(null, pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
		return true;
	}

	public static void validateCannonRope(Contraption contraption, Level level, @Nullable Direction direction,
										  Function<BlockPos, BlockPos> toLocalPos) throws AssemblyException {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(contraption) && (direction == null || direction.getAxis().isVertical())) return;
		BlockPos diff = toLocalPos.apply(BlockPos.ZERO).multiply(-1);

		Set<BlockPos> blocksToCheck = new HashSet<>();

		for (Map.Entry<BlockPos, StructureBlockInfo> entry : contraption.getBlocks().entrySet()) {
			BlockPos gpos = entry.getKey().offset(diff);
			BlockState levelState = level.getBlockState(gpos);
			StructureBlockInfo info = entry.getValue();
			if (levelState.getBlock() instanceof BigCannonBlock
				&& level.getBlockEntity(gpos) instanceof IBigCannonBlockEntity cbe) {
				if (AllBlocks.ROPE.has(info.state()) || AllBlocks.PULLEY_MAGNET.has(info.state()))
					throw AssemblyException.unmovableBlock(gpos, level.getBlockState(gpos));
				StructureBlockInfo contained = cbe.cannonBehavior().block();
				if (!AllBlocks.ROPE.has(contained.state()) && !AllBlocks.PULLEY_MAGNET.has(contained.state())) continue;
				blocksToCheck.add(gpos);
			}
		}

		Set<BlockPos> checked = new HashSet<>();
		int LIMIT = AllConfigs.server().kinetics.maxRopeLength.get();

		for (BlockPos pos : blocksToCheck) {
			if (checked.contains(pos)) continue;
			checked.add(pos);
			check: for (Direction dir : Iterate.directionsInAxis(Direction.Axis.Y)) {
				for (int i = 1; i <= LIMIT; ++i) {
					BlockPos pos1 = pos.relative(dir, i);
					if (checked.contains(pos1)) break check;
					checked.add(pos1);
					BlockState state = level.getBlockState(pos1);
					BlockState containedState = getInnerCannonState(level, state, pos1, Direction.UP);
					if (contraption.getBlocks().containsKey(pos1.subtract(diff))) {
						if (AllBlocks.ROPE_PULLEY.has(containedState) && dir == Direction.UP
							|| AllBlocks.PULLEY_MAGNET.has(containedState) && dir == Direction.DOWN) break;
						continue;
					}
					if (AllBlocks.ROPE_PULLEY.has(state) || AllBlocks.ROPE.has(containedState) || AllBlocks.PULLEY_MAGNET.has(containedState))
						throw AssemblyException.unmovableBlock(pos1, level.getBlockState(pos1));
					if (state.getBlock() instanceof BigCannonBlock cBlock
						&& cBlock.getFacing(state).getAxis().isVertical()
						&& level.getBlockEntity(pos1) instanceof IBigCannonBlockEntity cbe
						&& cbe.cannonBehavior().isConnectedTo(dir.getOpposite()))
						throw AssemblyException.unmovableBlock(pos1, level.getBlockState(pos1));
				}
			}
		}
	}

	@ExpectPlatform public static boolean canStickTo(BlockState state, BlockState state1) { throw new AssertionError(); }

}
