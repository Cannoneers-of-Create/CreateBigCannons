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
import rbasamoyai.createbigcannons.cannon_loading.CBCModifiedContraptionRegistry;
import rbasamoyai.createbigcannons.cannon_loading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuildingContraption;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredBigCannonBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(ContraptionCollider.class)
public abstract class ContraptionColliderMixin {

	@ModifyExpressionValue(method = "isCollidingWithWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canBeReplaced()Z"))
	private static boolean createbigcannons$isCollidingWithWorld(boolean original, Level level, TranslatingContraption contraption,
																 BlockPos anchor, Direction movementDirection,
																 @Local(ordinal = 1) BlockPos pos,
																 @Local(ordinal = 2) BlockPos colliderPos,
																 @Local BlockState collidedState,
																 @Local StructureBlockInfo blockInfo) {
		if (original) return true;
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(contraption)) {
			boolean specialCollider = ((CanLoadBigCannon) contraption).createbigcannons$getCannonLoadingColliders().contains(pos);
			BlockPos offsetPos = pos.relative(movementDirection);
			if (contraption.entity != null || !contraption.getBlocks().containsKey(offsetPos))
				return ContraptionRemix.isLoadingCannon(level, colliderPos, movementDirection, collidedState, blockInfo) && !specialCollider;
			StructureBlockInfo offsetInfo = contraption.getBlocks().get(offsetPos);
			return !specialCollider || offsetInfo.state().getBlock() == collidedState.getBlock();
		} else if (contraption instanceof CannonBuildingContraption builder) {
			if (!builder.isActivated()) return false;
			boolean flag = contraption.entity == null && builder.getBlocks().containsKey(pos.relative(movementDirection));
			if (level.getBlockEntity(colliderPos) instanceof LayeredBigCannonBlockEntity layered) {
				if (CBCBlocks.CANNON_BUILDER_HEAD.has(blockInfo.state()))
					return contraption.entity == null && builder.getBlocks().containsKey(pos);
				if (contraption.presentBlockEntities.get(pos) instanceof LayeredBigCannonBlockEntity layered1)
					return flag || !layered.isCollidingWith(blockInfo, layered1, movementDirection);
			}
			return flag;
		}
		return false;
	}

}
