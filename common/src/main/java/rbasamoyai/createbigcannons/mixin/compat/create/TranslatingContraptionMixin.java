package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.TranslatingContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import rbasamoyai.createbigcannons.cannonloading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;

import java.util.Set;

@Mixin(TranslatingContraption.class)
public abstract class TranslatingContraptionMixin extends Contraption {

	@ModifyExpressionValue(method = "createColliders", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/VoxelShape;isEmpty()Z", ordinal = 1), remap = false)
	private boolean createbigcannons$createColliders$0(boolean original, Level world, Direction movementDirection,
													 @Local StructureBlockInfo info, @Local BlockPos offsetPos) {
		if (original) return true;
		if (!(this instanceof CanLoadBigCannon)) return false;
		StructureBlockInfo offsetInfo = this.blocks.get(offsetPos);
		Direction.Axis axis = movementDirection.getAxis();
		if (this.getType() == ContraptionType.PISTON
			&& info.state.getBlock() == AllBlocks.MECHANICAL_PISTON_HEAD.get()
			&& info.state.getValue(BlockStateProperties.FACING) != movementDirection) return false;
		return !IBigCannonBlockEntity.isValidMunitionState(axis, info) && IBigCannonBlockEntity.isValidMunitionState(axis, offsetInfo);
	}

	@Inject(method = "createColliders", at = @At("TAIL"), remap = false, cancellable = true)
	private void createbigcannons$createColliders$1(Level world, Direction movementDirection, CallbackInfoReturnable<Set<BlockPos>> cir) {
		if (!(this instanceof CanLoadBigCannon loader)) return;
		Set<BlockPos> original = cir.getReturnValue();
		original.addAll(loader.createbigcannons$getCannonLoadingColliders());
		cir.setReturnValue(original); // Not sure if unnecessary but might as well --ritchie
	}

}
