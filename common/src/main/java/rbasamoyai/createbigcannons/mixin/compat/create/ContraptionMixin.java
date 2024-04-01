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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
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
import rbasamoyai.createbigcannons.cannon_loading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(Contraption.class)
public abstract class ContraptionMixin {

	@Unique private final Contraption self = (Contraption) (Object) this;

	@Shadow private Set<SuperGlueEntity> glueToRemove;

	@Shadow protected abstract Pair<StructureBlockInfo, BlockEntity> capture(Level world, BlockPos pos);

	@Shadow
	protected abstract BlockPos toLocalPos(BlockPos globalPos);

	@Inject(method = "searchMovedStructure",
			at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/BlockMovementChecks;isBrittle(Lnet/minecraft/world/level/block/state/BlockState;)Z", shift = At.Shift.BEFORE),
			remap = false)
	private void createbigcannons$searchMovedStructure$setForcedDirection(Level level, BlockPos pos, Direction forcedDirection,
																		  CallbackInfoReturnable<Boolean> cir, @Local LocalRef<Direction> forcedDirectionRef) {
		if (!(this.self instanceof CanLoadBigCannon loader)) return;
		if (forcedDirectionRef.get() == null) forcedDirectionRef.set(loader.createbigcannons$getAssemblyMovementDirection(level));
	}

	@Inject(method = "searchMovedStructure",
			at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER),
			locals = LocalCapture.CAPTURE_FAILHARD,
			remap = false)
	private void createbigcannons$searchMovedStructure$removePulley(Level level, BlockPos pos, Direction forcedDirection,
																	CallbackInfoReturnable<Boolean> cir, Queue<BlockPos> frontier) {
		if (this.self instanceof PulleyContraption pulley)
			ContraptionRemix.pulleyChecks(pulley, level, pos, forcedDirection, frontier);
	}

	@ModifyExpressionValue(method = "addBlocksToWorld", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;customBlockPlacement(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"), remap = false)
	private boolean createbigcannons$addBlocksToWorld$customBlockPlacement(boolean original, Level level, StructureTransform transform,
																		   @Local(ordinal = 0) BlockPos targetPos, @Local(ordinal = 0) BlockState state) {
		return original || ContraptionRemix.customBlockPlacement(this.self, level, targetPos, state);
	}

	@ModifyExpressionValue(method = "removeBlocksFromWorld", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;customBlockRemoval(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"), remap = false)
	private boolean createbigcannons$removeBlocksFromWorld$customBlockRemoval(boolean original, Level level, BlockPos offset,
																			  @Local(ordinal = 1) BlockPos add, @Local(ordinal = 0) StructureBlockInfo block) {
		return original || ContraptionRemix.customBlockRemoval(this.self, level, add, block.state);
	}

	@Inject(method = "moveBlock",
			at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 0, shift = At.Shift.BEFORE),
			locals = LocalCapture.CAPTURE_FAILHARD,
			remap = false)
	private void createbigcannons$moveBlock$customChecks(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														 Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
														 BlockPos pos, BlockState state) {
		ContraptionRemix.customChecks(this.self, level, pos, state, forcedDirection, frontier, visited, cir);
	}

	@Inject(method = "moveBlock",
			at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.BEFORE),
			locals = LocalCapture.CAPTURE_FAILHARD,
			remap = false)
	private void createbigcannons$moveBlock$stickerMarking(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														   Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir, BlockPos pos,
														   BlockState state, Direction offset, BlockPos attached) {
		if (this.self instanceof CanLoadBigCannon)
			ContraptionRemix.stickerMarking((Contraption & CanLoadBigCannon) this.self, level, attached, offset, forcedDirection);
	}

	@Inject(method = "moveChassis", at = @At(value = "TAIL", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
	private void createbigcannons$moveChassis(Level level, BlockPos pos, Direction movementDirection, Queue<BlockPos> frontier,
											  Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir, BlockEntity be,
											  ChassisBlockEntity chassis, List<BlockPos> includedBlockPositions) {
		if (this.self instanceof CanLoadBigCannon)
			ContraptionRemix.chassisMarking((Contraption & CanLoadBigCannon) this.self, level, includedBlockPositions, frontier, visited, movementDirection, chassis);
	}

	@Inject(method = "moveMechanicalPiston", at = @At("TAIL"), remap = false)
	private void createbigcannons$moveMechanicalPiston(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited,
													   BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (this.self instanceof CanLoadBigCannon)
			ContraptionRemix.pistonMarking((Contraption & CanLoadBigCannon) this.self, level, pos, state);
	}

	@Inject(method = "movePistonHead", at = @At("TAIL"), remap = false)
	private void createbigcannons$movePistonHead(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state, CallbackInfo ci) {
		if (this.self instanceof CanLoadBigCannon)
			ContraptionRemix.pistonHeadMarking((Contraption & CanLoadBigCannon) this.self, level, pos, state);
	}

	@Inject(method = "moveGantryPinion", at = @At("HEAD"), remap = false)
	private void createbigcannons$moveGantryPinion(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state, CallbackInfo ci) {
		if (this.self instanceof CanLoadBigCannon)
			ContraptionRemix.gantryCarriageMarking((Contraption & CanLoadBigCannon) this.self, level, pos, state);
	}

	@Inject(method = "moveBlock",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos;", shift = At.Shift.BEFORE),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void createbigcannons$moveBlock$loaderBlocks(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														 Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
														 BlockPos pos, BlockState state) {
		if (this.self instanceof CanLoadBigCannon)
			ContraptionRemix.moveLoaderBlocks((Contraption & CanLoadBigCannon) this.self, level, forcedDirection, frontier, visited, pos, state);
	}

	@Inject(method = "moveBlock",
		at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/BlockMovementChecks;isBrittle(Lnet/minecraft/world/level/block/state/BlockState;)Z"),
		locals = LocalCapture.CAPTURE_FAILHARD,
		remap = false)
	private void createbigcannons$moveBlock$addFrontier$0(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														  Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
														  BlockPos pos, BlockState state, BlockPos posDown,
														  BlockState stateBelow, Direction[] var9, int var10, int var11,
														  Direction offset, BlockPos offsetPos, BlockState blockState,
														  boolean wasVisited, boolean faceHasGlue, boolean blockAttachedTowardsFace,
														  @Share("removeFlag") LocalBooleanRef removeFlag) {
		removeFlag.set(false);
		if (!(this.self instanceof CanLoadBigCannon) || frontier.contains(offsetPos)) return;
		boolean stickFlag = ContraptionRemix.getStickFlag((Contraption & CanLoadBigCannon) this.self, level, pos, offsetPos,
			state, blockState, offset, forcedDirection, faceHasGlue | blockAttachedTowardsFace);
		removeFlag.set(ContraptionRemix.handleCannonFrontier((Contraption & CanLoadBigCannon) this.self, level, pos, offsetPos,
			visited, offset, forcedDirection, stickFlag));
	}

	@Inject(method = "moveBlock",
		at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z", ordinal = 4, shift = At.Shift.AFTER),
		locals = LocalCapture.CAPTURE_FAILHARD,
		remap = false)
	private void createbigcannons$moveBlock$addFrontier$1(Level level, Direction forcedDirection, Queue<BlockPos> frontier,
														  Set<BlockPos> visited, CallbackInfoReturnable<Boolean> cir,
														  BlockPos pos, BlockState state, BlockPos posDown,
														  BlockState stateBelow, Direction[] var9, int var10, int var11,
														  Direction offset, BlockPos offsetPos, @Share("removeFlag") LocalBooleanRef removeFlag) {
		if (this.self instanceof CanLoadBigCannon && removeFlag.get())
			frontier.remove(offsetPos);
	}

	@ModifyExpressionValue(method = "moveBlock",
			at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;capture(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lorg/apache/commons/lang3/tuple/Pair;"),
			remap = false)
	private Pair<StructureBlockInfo, BlockEntity> createbigcannons$moveBlock$preCannonBlockCapture(Pair<StructureBlockInfo, BlockEntity> original,
												Level level, @Nullable Direction forcedDirection, Queue<BlockPos> frontier, Set<BlockPos> visited,
												@Local(ordinal = 0) BlockPos pos) {
		if (this.self instanceof CanLoadBigCannon) {
			Pair<StructureBlockInfo, BlockEntity> pair = ContraptionRemix.handleCapture((Contraption & CanLoadBigCannon) this.self,
				level, pos, frontier, visited, forcedDirection, this.glueToRemove);
			return pair == null ? original : pair;
		}
		return original;
	}

	@Inject(method = "movePulley", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;", shift = At.Shift.BEFORE))
	private void createbigcannons$movePulley$0(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, CallbackInfo ci,
											   @Local(ordinal = 1) BlockPos ropePos, @Local LocalRef<BlockState> ropeState) {
		if (this.self instanceof CanLoadBigCannon)
			ropeState.set(ContraptionRemix.getInnerCannonState(level, ropeState.get(), ropePos, Direction.DOWN));
	}

	@Inject(method = "movePulley", at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER), remap = false)
	private void createbigcannons$movePulley$1(Level level, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, CallbackInfo ci,
											   @Local(ordinal = 1) BlockPos ropePos) {
		BlockState state = level.getBlockState(ropePos);
		if (state.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(state).getAxis().isVertical()
			&& level.getBlockEntity(ropePos) instanceof IBigCannonBlockEntity cbe
			&& cbe.cannonBehavior().block().state.isAir()) {
			BlockPos local = this.toLocalPos(ropePos.above());
			if (this.self.getBlocks().containsKey(local)
				&& this.self.getBlocks().get(local).state.getBlock() instanceof BigCannonBlock
				&& level.getBlockEntity(ropePos.above()) instanceof IBigCannonBlockEntity cbe1
				&& cbe1.cannonBehavior().isConnectedTo(Direction.DOWN)) {
				return;
			}
			frontier.remove(ropePos);
		}
	}

	@ModifyExpressionValue(method = "movePulley",
			at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;capture(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lorg/apache/commons/lang3/tuple/Pair;"),
			remap = false)
	private Pair<StructureBlockInfo, BlockEntity> createbigcannons$movePulley$2(Pair<StructureBlockInfo, BlockEntity> original,
																				Level level, BlockPos pos,
																				@Local(ordinal = 1) BlockPos ropePos) {
		Direction forcedDirection = null;
		if (this.self instanceof CanLoadBigCannon loader)
			forcedDirection = loader.createbigcannons$getAssemblyMovementDirection(level);
		BlockState state = level.getBlockState(ropePos);
		BlockPos local = this.toLocalPos(ropePos);

		if ((forcedDirection == null || forcedDirection.getAxis().isHorizontal())
			&& this.self.getBlocks().containsKey(local)
			&& this.self.getBlocks().get(local).state.getBlock() instanceof BigCannonBlock) {
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
		ContraptionRemix.validateCannonRope(this.self, level, forcedDirection, this::toLocalPos);
		if (this.self instanceof CanLoadBigCannon) {
			ContraptionRemix.markFragileBlocks((Contraption & CanLoadBigCannon) this.self);
		}
	}

	@Inject(method = "readNBT", at = @At("TAIL"), remap = false)
	private void createbigcannons$readNBT(Level level, CompoundTag nbt, boolean spawnData, CallbackInfo ci) {
		if (this.self instanceof CanLoadBigCannon) ContraptionRemix.readCannonLoaderData((Contraption & CanLoadBigCannon) this.self, nbt);
	}

	@Inject(method = "writeNBT", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
	private void createbigcannons$writeNBT(boolean spawnPacket, CallbackInfoReturnable<CompoundTag> cir, CompoundTag nbt) {
		if (this.self instanceof CanLoadBigCannon) ContraptionRemix.writeCannonLoaderData((Contraption & CanLoadBigCannon) this.self, nbt);
	}

}
