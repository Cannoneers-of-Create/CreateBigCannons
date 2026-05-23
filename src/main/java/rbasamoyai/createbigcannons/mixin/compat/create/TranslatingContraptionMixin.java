package rbasamoyai.createbigcannons.mixin.compat.create;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.TranslatingContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannon_loading.CBCModifiedContraptionRegistry;
import rbasamoyai.createbigcannons.cannon_loading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;

@Mixin(TranslatingContraption.class)
public abstract class TranslatingContraptionMixin extends Contraption {

	@ModifyExpressionValue(method = "createColliders",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/VoxelShape;isEmpty()Z", ordinal = 1))
	private boolean createbigcannons$createColliders$0(boolean original, Level level, Direction movementDirection,
													   @Local StructureBlockInfo info, @Local BlockPos offsetPos) {
		if (original) return true;
		if (!(CBCModifiedContraptionRegistry.canLoadBigCannon(this))) return false;
		StructureBlockInfo offsetInfo = this.blocks.get(offsetPos);
		Direction.Axis axis = movementDirection.getAxis();
		if (info.state().getBlock() == AllBlocks.MECHANICAL_PISTON_HEAD.get()
			&& info.state().getValue(BlockStateProperties.FACING) != movementDirection) return false;
		return !IBigCannonBlockEntity.isValidMunitionState(axis, info) && IBigCannonBlockEntity.isValidMunitionState(axis, offsetInfo);
	}

	@WrapMethod(method = "createColliders", remap = false)
	private Set<BlockPos> createbigcannons$createColliders$1(Level world, Direction movementDirection, Operation<Set<BlockPos>> original) {
        Set<BlockPos> ret = original.call(world, movementDirection);
		if (CBCModifiedContraptionRegistry.canLoadBigCannon(this))
		    ret.addAll(((CanLoadBigCannon) this).createbigcannons$getCannonLoadingColliders());
        return ret;
    }

}
