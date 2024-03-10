package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import com.simibubi.create.content.contraptions.TranslatingContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannonloading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(ContraptionCollider.class)
public abstract class ContraptionColliderMixin {

	@ModifyExpressionValue(method = "isCollidingWithWorld",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/Material;isReplaceable()Z"),
		remap = false)
	private static boolean createbigcannons$isCollidingWithWorld(boolean original, Level level, TranslatingContraption contraption,
																 BlockPos anchor, Direction movementDirection,
																 @Local(ordinal = 1) BlockPos pos,
																 @Local(ordinal = 2) BlockPos colliderPos,
																 @Local BlockState collidedState,
																 @Local StructureBlockInfo blockInfo) {
		if (original) return true;
		if (contraption instanceof CanLoadBigCannon loader) {
			boolean specialCollider = loader.createbigcannons$getCannonLoadingColliders().contains(pos);
			BlockPos offsetPos = pos.relative(movementDirection);
			if (contraption.entity != null || !contraption.getBlocks().containsKey(offsetPos))
				return ContraptionRemix.isLoadingCannon(level, colliderPos, movementDirection, collidedState, blockInfo) && !specialCollider;
			StructureBlockInfo offsetInfo = contraption.getBlocks().get(offsetPos);
			return !specialCollider || offsetInfo.state.getBlock() == collidedState.getBlock();
		}
		return false;
	}

}
