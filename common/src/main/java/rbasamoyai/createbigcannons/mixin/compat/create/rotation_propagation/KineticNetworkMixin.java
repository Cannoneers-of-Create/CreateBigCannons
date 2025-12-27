package rbasamoyai.createbigcannons.mixin.compat.create.rotation_propagation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntity;
import rbasamoyai.createbigcannons.base.multiple_kinetic_interface.HasMultipleKineticInterfaces;

@Mixin(KineticNetwork.class)
public class KineticNetworkMixin {

	@ModifyExpressionValue(method = "calculateCapacity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"))
	private BlockEntity createbigcannons$calculateCapacity(BlockEntity original, @Local KineticBlockEntity kbe) {
		if (!(original instanceof HasMultipleKineticInterfaces hasMultiple))
			return original;
		return hasMultiple.getAllKineticBlockEntities().contains(kbe) ? kbe : original;
	}

	@ModifyExpressionValue(method = "calculateStress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"))
	private BlockEntity createbigcannons$calculateStress(BlockEntity original, @Local KineticBlockEntity kbe) {
		if (!(original instanceof HasMultipleKineticInterfaces hasMultiple))
			return original;
		return hasMultiple.getAllKineticBlockEntities().contains(kbe) ? kbe : original;
	}

}
