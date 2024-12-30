package rbasamoyai.createbigcannons.mixin.compat.create;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.contraptions.chassis.ChassisBlockEntity;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.contraptions.pulley.PulleyContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannon_loading.CBCModifiedContraptionRegistry;
import rbasamoyai.createbigcannons.cannon_loading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;
import rbasamoyai.createbigcannons.remix.HasFragileContraption;

@Mixin(Contraption.class)
public abstract class ContraptionMixin {

	@Unique private final Contraption createbigcannons$self = (Contraption) (Object) this;

	@Shadow private Set<SuperGlueEntity> glueToRemove;

	@Shadow
	protected abstract BlockPos toLocalPos(BlockPos globalPos);

	@Shadow
	public boolean disassembled;

	@Inject(method = "searchMovedStructure",
			at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/BlockMovementChecks;isBrittle(Lnet/minecraft/world/level/block/state/BlockState;)Z", shift = At.Shift.BEFORE))
	private void createbigcannons$searchMovedStructure$setForcedDirection(Level level, BlockPos pos, Direction forcedDirection,
																		  CallbackInfoReturnable<Boolean> cir,
																		  @Local(argsOnly = true) LocalRef<Direction> forcedDirectionRef) {
		if (!(CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))) return;
		if (forcedDirectionRef.get() == null)
			forcedDirectionRef.set(((CanLoadBigCannon) this.createbigcannons$self).createbigcannons$getAssemblyMovementDirection(level));
	}

	@Inject(method = "searchMovedStructure",
			at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER),
			remap = false)
	private void createbigcannons$searchMovedStructure$removePulley(Level level, BlockPos pos, Direction forcedDirection,
																	CallbackInfoReturnable<Boolean> cir, @Local Queue<BlockPos> frontier) {
		if (this.createbigcannons$self.getType() == ContraptionType.PULLEY)
			ContraptionRemix.pulleyChecks((PulleyContraption) this.createbigcannons$self, level, pos, forcedDirection, frontier);
	}

	@Inject(method = "addBlocksToWorld", at = @At("HEAD"))
	private void createbigcannons$addBlocksToWorld(Level world, StructureTransform transform, CallbackInfo ci) {
		if (this.disassembled || !(CBCModifiedContraptionRegistry.isFragileContraption(this.createbigcannons$self)))
			return;
		HasFragileContraption fragile = (HasFragileContraption) this.createbigcannons$self;
		if (!fragile.createbigcannons$isBrokenDisassembly())
			fragile.createbigcannons$setBrokenDisassembly(HasFragileContraption.checkForIntersectingBlocks(this.createbigcannons$self.entity.level(), this.createbigcannons$self.entity, fragile));
	}

	@ModifyExpressionValue(method = "addBlocksToWorld", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;customBlockPlacement(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean createbigcannons$addBlocksToWorld$customBlockPlacement(boolean original, Level level, StructureTransform transform,
																		   @Local(ordinal = 0) BlockPos targetPos, @Local(ordinal = 0) BlockState state) {
		return original || ContraptionRemix.customBlockPlacement(this.createbigcannons$self, level, targetPos, state);
	}

	@ModifyExpressionValue(method = "removeBlocksFromWorld", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;customBlockRemoval(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean createbigcannons$removeBlocksFromWorld$customBlockRemoval(boolean original, Level level, BlockPos offset,
																			  @Local(ordinal = 1) BlockPos add, @Local(ordinal = 0) StructureBlockInfo block) {
		return original || ContraptionRemix.customBlockRemoval(this.createbigcannons$self, level, add, block.state());
	}

	@Inject(method = "moveBlock",
			at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 0, shift = At.Shift.BEFORE))
	private void createbigcannons$moveBlock$customChecks(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														 Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
														 @Local BlockPos pos, @Local BlockState state) {
		ContraptionRemix.customChecks(this.createbigcannons$self, level, pos, state, forcedDirection, frontier, visited, cir);
	}

	@Inject(method = "moveBlock",
			at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.BEFORE),
		remap = false)
	private void createbigcannons$moveBlock$stickerMarking(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														   Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
														   @Local(ordinal = 1) Direction offset, @Local(ordinal = 1) BlockPos attached) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			ContraptionRemix.stickerMarking((Contraption & CanLoadBigCannon) this.createbigcannons$self, level, attached, offset, forcedDirection);
	}

	@ModifyExpressionValue(method = "moveBlock",
		at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;movementAllowed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z", ordinal = 1))
	private boolean createbigcannons$moveBlock$bypassMovementError(boolean original,
																   @Local(argsOnly = true) Level level,
																   @Local(argsOnly = true) Direction forcedDirection,
																   @Local(ordinal = 1) LocalRef<Direction> offset) {
		if (original || !CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			return original;
		Direction offsetVal = offset.get();
		Direction originalForcedDir = ((CanLoadBigCannon) this.createbigcannons$self).createbigcannons$getOriginalForcedDirection(level);
		if (offsetVal != originalForcedDir && originalForcedDir != forcedDirection)
			offset.set(originalForcedDir == null ? null : offsetVal.getOpposite());
		return false;
	}

	@Inject(method = "moveChassis", at = @At(value = "TAIL", shift = At.Shift.BEFORE), remap = false)
	private void createbigcannons$moveChassis(Level level, BlockPos pos, Direction movementDirection, Queue<BlockPos> frontier,
											  Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
											  @Local ChassisBlockEntity chassis, @Local List<BlockPos> includedBlockPositions) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			ContraptionRemix.chassisMarking((Contraption & CanLoadBigCannon) this.createbigcannons$self, level, includedBlockPositions, frontier, visited, movementDirection, chassis);
	}

	@Inject(method = "moveMechanicalPiston", at = @At("TAIL"), remap = false)
	private void createbigcannons$moveMechanicalPiston(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited,
													   BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			ContraptionRemix.pistonMarking((Contraption & CanLoadBigCannon) this.createbigcannons$self, level, pos, state);
	}

	@Inject(method = "movePistonHead", at = @At("TAIL"), remap = false)
	private void createbigcannons$movePistonHead(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state, CallbackInfo ci) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			ContraptionRemix.pistonHeadMarking((Contraption & CanLoadBigCannon) this.createbigcannons$self, level, pos, state);
	}

	@Inject(method = "moveGantryPinion", at = @At("HEAD"), remap = false)
	private void createbigcannons$moveGantryPinion(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state, CallbackInfo ci) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			ContraptionRemix.gantryCarriageMarking((Contraption & CanLoadBigCannon) this.createbigcannons$self, level, pos, state);
	}

	@Inject(method = "moveBlock",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos;", shift = At.Shift.BEFORE))
	private void createbigcannons$moveBlock$loaderBlocks(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														 Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
														 @Local BlockPos pos, @Local BlockState state) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			ContraptionRemix.moveLoaderBlocks((Contraption & CanLoadBigCannon) this.createbigcannons$self, level, forcedDirection, frontier, visited, pos, state);
	}

	@Inject(method = "moveBlock",
		at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/BlockMovementChecks;isBrittle(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private void createbigcannons$moveBlock$addFrontier$0(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														  Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
														  @Local(ordinal = 0) BlockPos pos,
														  @Local(ordinal = 0) BlockState state,
														  @Local(ordinal = 1) Direction offset,
														  @Local(ordinal = 2) BlockPos offsetPos,
														  @Local(ordinal = 2) BlockState blockState,
														  @Local(ordinal = 1) boolean faceHasGlue,
														  @Local(ordinal = 2) boolean blockAttachedTowardsFace,
														  @Share("removeFlag") LocalBooleanRef removeFlag) {
		removeFlag.set(false);
		if (!(CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self)) || frontier.contains(offsetPos)) return;
		boolean stickFlag = ContraptionRemix.getStickFlag((Contraption & CanLoadBigCannon) this.createbigcannons$self, level, pos, offsetPos,
			state, blockState, offset, forcedDirection, faceHasGlue | blockAttachedTowardsFace);
		removeFlag.set(ContraptionRemix.handleCannonFrontier((Contraption & CanLoadBigCannon) this.createbigcannons$self, level, pos, offsetPos,
			visited, offset, forcedDirection, stickFlag));
	}

	@Inject(method = "moveBlock",
		at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z", ordinal = 4, shift = At.Shift.AFTER),
		remap = false)
	private void createbigcannons$moveBlock$addFrontier$1(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														  Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
														  @Local(ordinal = 2) BlockPos offsetPos,
														  @Share("removeFlag") LocalBooleanRef removeFlag) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self) && removeFlag.get())
			frontier.remove(offsetPos);
	}

	@ModifyExpressionValue(method = "moveBlock",
			at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;capture(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lorg/apache/commons/lang3/tuple/Pair;"))
	private Pair<StructureBlockInfo, BlockEntity> createbigcannons$moveBlock$preCannonBlockCapture(Pair<StructureBlockInfo, BlockEntity> original,
												Level level, @Nullable Direction forcedDirection, Queue<BlockPos> frontier, Set<BlockPos> visited,
												@Local(ordinal = 0) BlockPos pos) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self)) {
			Pair<StructureBlockInfo, BlockEntity> pair = ContraptionRemix.handleCapture((Contraption & CanLoadBigCannon) this.createbigcannons$self,
				level, pos, frontier, visited, forcedDirection, this.glueToRemove);
			return pair == null ? original : pair;
		}
		return original;
	}

	@Inject(method = "movePulley", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;", shift = At.Shift.BEFORE))
	private void createbigcannons$movePulley$0(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, CallbackInfo ci,
											   @Local(ordinal = 1) BlockPos ropePos, @Local LocalRef<BlockState> ropeState) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			ropeState.set(ContraptionRemix.getInnerCannonState(level, ropeState.get(), ropePos, Direction.DOWN));
	}

	@Inject(method = "movePulley", at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER), remap = false)
	private void createbigcannons$movePulley$1(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, CallbackInfo ci,
											   @Local(ordinal = 1) BlockPos ropePos) {
		BlockState state = level.getBlockState(ropePos);
		if (state.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(state).getAxis().isVertical()
			&& level.getBlockEntity(ropePos) instanceof IBigCannonBlockEntity cbe
			&& cbe.cannonBehavior().block().state().isAir()) {
			BlockPos local = this.toLocalPos(ropePos.above());
			if (this.createbigcannons$self.getBlocks().containsKey(local)
				&& this.createbigcannons$self.getBlocks().get(local).state().getBlock() instanceof BigCannonBlock
				&& level.getBlockEntity(ropePos.above()) instanceof IBigCannonBlockEntity cbe1
				&& cbe1.cannonBehavior().isConnectedTo(Direction.DOWN)) {
				return;
			}
			frontier.remove(ropePos);
		}
	}

	@ModifyExpressionValue(method = "movePulley",
			at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;capture(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lorg/apache/commons/lang3/tuple/Pair;"))
	private Pair<StructureBlockInfo, BlockEntity> createbigcannons$movePulley$2(Pair<StructureBlockInfo, BlockEntity> original,
																				Level level, BlockPos pos,
																				@Local(ordinal = 1) BlockPos ropePos) {
		Direction forcedDirection = null;
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			forcedDirection = ((CanLoadBigCannon) this.createbigcannons$self).createbigcannons$getAssemblyMovementDirection(level);
		BlockState state = level.getBlockState(ropePos);
		BlockPos local = this.toLocalPos(ropePos);

		if ((forcedDirection == null || forcedDirection.getAxis().isHorizontal())
			&& this.createbigcannons$self.getBlocks().containsKey(local)
			&& this.createbigcannons$self.getBlocks().get(local).state().getBlock() instanceof BigCannonBlock) {
			return original;
		}
		if (state.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(state).getAxis().isVertical()
			&& level.getBlockEntity(ropePos) instanceof IBigCannonBlockEntity cbe) {
			return Pair.of(cbe.cannonBehavior().block(), null);
		}
		return original;
	}

	@Inject(method = "searchMovedStructure", at = @At(value = "RETURN", ordinal = 1), remap = false)
	private void createbigcannons$searchMovedStructure(Level level, BlockPos pos, Direction forcedDirection, CallbackInfoReturnable<Boolean> cir) throws AssemblyException {
		ContraptionRemix.validateCannonRope(this.createbigcannons$self, level, forcedDirection, this::toLocalPos);
		if (CBCModifiedContraptionRegistry.isFragileContraption(this.createbigcannons$self))
			ContraptionRemix.markFragileBlocks((Contraption & HasFragileContraption) this.createbigcannons$self);
	}

	@Inject(method = "readNBT", at = @At("TAIL"), remap = false)
	private void createbigcannons$readNBT(Level level, CompoundTag nbt, boolean spawnData, CallbackInfo ci) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			ContraptionRemix.readCannonLoaderData((Contraption & CanLoadBigCannon) this.createbigcannons$self, nbt);
		if (CBCModifiedContraptionRegistry.isFragileContraption(this.createbigcannons$self))
			ContraptionRemix.readFragileBlocks((Contraption & HasFragileContraption) this.createbigcannons$self, nbt);
	}

	@Inject(method = "writeNBT", at = @At("TAIL"), remap = false)
	private void createbigcannons$writeNBT(boolean spawnPacket, CallbackInfoReturnable<CompoundTag> cir, @Local(ordinal = 0) CompoundTag nbt) {
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this.createbigcannons$self))
			ContraptionRemix.writeCannonLoaderData((Contraption & CanLoadBigCannon) this.createbigcannons$self, nbt);
		if (CBCModifiedContraptionRegistry.isFragileContraption(this.createbigcannons$self))
			ContraptionRemix.writeFragileBlocks((Contraption & HasFragileContraption) this.createbigcannons$self, nbt);
	}

}
