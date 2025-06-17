package rbasamoyai.createbigcannons.mixin.compat.create.rotation_propagation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import rbasamoyai.createbigcannons.base.multiple_kinetic_interface.HasMultipleKineticInterfaces;
import rbasamoyai.createbigcannons.remix.RotationPropagatorRemix;

@Mixin(RotationPropagator.class)
public class RotationPropagatorMixin {

	@WrapOperation(method = "handleAdded", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/RotationPropagator;propagateNewSource(Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;)V"))
	private static void createbigcannons$handleAdded(KineticBlockEntity addedBE, Operation<Void> original) {
		if (addedBE instanceof HasMultipleKineticInterfaces hasMultiple) {
			List<KineticBlockEntity> subBEs = hasMultiple.getAllKineticBlockEntities();
			for (KineticBlockEntity kbe : subBEs) {
				original.call(kbe);
				if (addedBE.isRemoved())
					return;
			}
		} else {
			original.call(addedBE);
		}
	}

	@ModifyExpressionValue(method = "findConnectedNeighbour", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"))
	private static BlockEntity createbigcannons$findConnectedNeighbour(BlockEntity original,
																	   @Local(argsOnly = true) KineticBlockEntity currentBE,
																	   @Local(argsOnly = true) BlockPos neighborPos) {
		return RotationPropagatorRemix.replaceGetBlockEntity(original, currentBE.getBlockPos().subtract(neighborPos));
	}

	@ModifyExpressionValue(method = "handleRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"))
	private static BlockEntity createbigcannons$handleRemoved(BlockEntity original,
															  @Local(argsOnly = true) KineticBlockEntity removedBE,
															  @Local(ordinal = 1) BlockPos neighborPos) {
		return RotationPropagatorRemix.replaceGetBlockEntity(original, removedBE.getBlockPos().subtract(neighborPos));
	}

	@Inject(method = "propagateMissingSource", at = @At("HEAD"), remap = false)
	private static void createbigcannons$propagateMissingSource$0(KineticBlockEntity updateTE, CallbackInfo ci,
																  @Share("sourceMap") LocalRef<Map<BlockPos, Queue<BlockPos>>> sourceMapRef) {
		Map<BlockPos, Queue<BlockPos>> sourceMap = new HashMap<>();
		if (updateTE.hasSource() && updateTE.getLevel().getBlockEntity(updateTE.getBlockPos()) instanceof HasMultipleKineticInterfaces) {
			Queue<BlockPos> queue = new LinkedList<>();
			queue.add(updateTE.source);
			sourceMap.put(updateTE.getBlockPos(), queue);
		}
		sourceMapRef.set(sourceMap);
	}

	@ModifyExpressionValue(method = "propagateMissingSource", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"), remap = false)
	private static BlockEntity createbigcannons$propagateMissingSource$1(BlockEntity original,
																		 @Share("sourceMap") LocalRef<Map<BlockPos, Queue<BlockPos>>> sourceMapRef) {
		BlockPos pos = original.getBlockPos();
		Map<BlockPos, Queue<BlockPos>> sourceMap = sourceMapRef.get();
		if (!sourceMap.containsKey(pos))
			return original;
		Queue<BlockPos> queue = sourceMap.get(pos);
		if (queue.isEmpty()) {
			sourceMap.remove(pos);
			return original;
		}
		BlockPos sourcePos = queue.poll();
		if (queue.isEmpty())
			sourceMap.remove(pos);
		return RotationPropagatorRemix.replaceGetBlockEntity(original, sourcePos.subtract(pos));
	}

	@Inject(method = "propagateMissingSource", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 3), remap = false)
	private static void createbigcannons$propagateMissingSource$2(KineticBlockEntity updateBE, CallbackInfo ci,
																  @Local Level level,
																  @Local(ordinal = 1) KineticBlockEntity currentBE,
																  @Local(ordinal = 2) KineticBlockEntity neighborBE,
																  @Share("sourceMap") LocalRef<Map<BlockPos, Queue<BlockPos>>> sourceMapRef) {
		if (!(level.getBlockEntity(neighborBE.getBlockPos()) instanceof HasMultipleKineticInterfaces))
			return;
		Map<BlockPos, Queue<BlockPos>> sourceMap = sourceMapRef.get();
		BlockPos pos = neighborBE.getBlockPos();
		if (!sourceMap.containsKey(pos))
			sourceMap.put(pos, new LinkedList<>());
		Queue<BlockPos> queue = sourceMap.get(pos);
		queue.add(currentBE.getBlockPos()); // Keep track of origin
	}

}
