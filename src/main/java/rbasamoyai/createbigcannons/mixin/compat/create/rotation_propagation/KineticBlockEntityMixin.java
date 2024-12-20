package rbasamoyai.createbigcannons.mixin.compat.create.rotation_propagation;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.base.multiple_kinetic_interface.HasMultipleKineticInterfaces;
import rbasamoyai.createbigcannons.remix.RotationPropagatorRemix;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityMixin extends SmartBlockEntity {

	KineticBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) { super(type, pos, state); }

	@Shadow
	@Nullable
	public BlockPos source;

	@ModifyExpressionValue(method = "validateKinetics", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"))
	private BlockEntity createbigcannons$validateKinetics(BlockEntity original) {
		return RotationPropagatorRemix.replaceGetBlockEntity(original, this.getBlockPos().subtract(this.source));
	}

	@ModifyExpressionValue(method = "setSource", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"))
	private BlockEntity createbigcannons$setSource(BlockEntity original) {
		return RotationPropagatorRemix.replaceGetBlockEntity(original, this.getBlockPos().subtract(this.source));
	}

	@WrapOperation(method = "switchToBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 0))
	private static boolean createbigcannons$switchToBlockState(Level instance, BlockPos pos, BlockState state, int flags,
															   Operation<Boolean> original,
															   @Local(ordinal = 1) BlockState currentState,
															   @Local BlockEntity blockEntity) {
		if (!(blockEntity instanceof HasMultipleKineticInterfaces hasMultiple))
			return false;
		if (!(state.getBlock() instanceof KineticBlock))
			return false;
		KineticBlockAccessor accessor = (KineticBlockAccessor) state.getBlock();
		if (accessor.callAreStatesKineticallyEquivalent(currentState, state))
			return false;
		for (KineticBlockEntity kbe : hasMultiple.getAllKineticBlockEntities()) {
			if (kbe.hasNetwork())
				kbe.getOrCreateNetwork().remove(kbe);
			kbe.detachKinetics();
			kbe.removeSource();
		}
		return original.call(instance, pos, state, flags);
	}

}
