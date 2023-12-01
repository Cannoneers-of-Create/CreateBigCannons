package rbasamoyai.createbigcannons.mixin;

import com.simibubi.create.content.contraptions.gantry.GantryCarriageBlock;
import com.simibubi.create.content.contraptions.gantry.GantryCarriageBlockEntity;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import rbasamoyai.createbigcannons.cannonloading.GantryRammerBlock;
import rbasamoyai.createbigcannons.cannonloading.GantryRammerBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;

@Mixin(value = {GantryShaftBlockEntity.class})
public class GantryShaftBlockEntityMixin extends KineticBlockEntity {
	public GantryShaftBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@Shadow
    public boolean canAssembleOn() {
        return false;
    }

    @Inject(
            method = "checkAttachedCarriageBlocks()V",
            at = @At("TAIL"),
            remap = false
    )
    public void injectCheckAttachedCarriageBlocks(CallbackInfo ci) {
        if (!canAssembleOn())
            return;
        for (Direction d : Iterate.directions) {
            if (d.getAxis() == getBlockState().getValue(GantryShaftBlock.FACING)
                    .getAxis())
                continue;
            BlockPos offset = worldPosition.relative(d);
            BlockState pinionState = level.getBlockState(offset);
            if (!CBCBlocks.GANTRY_RAMMER_CARRIAGE.has(pinionState))
                continue;
            if (pinionState.getValue(GantryRammerBlock.FACING) != d)
                continue;
            BlockEntity tileEntity = level.getBlockEntity(offset);
            if (tileEntity instanceof GantryRammerBlockEntity)
                ((GantryRammerBlockEntity) tileEntity).queueAssembly();
        }
    }

	@Inject(
		method = "propagateRotationTo(Lcom/simibubi/create/content/kinetics/base/KineticBlockEntity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;ZZ)F",
		at = @At("HEAD"),
		cancellable = true
	)
	public void injectPropagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs, CallbackInfoReturnable<Float> cir) {
		Direction direction = Direction.getNearest(diff.getX(), diff.getY(), diff.getZ());
		if (CBCBlocks.GANTRY_RAMMER_CARRIAGE.has(stateTo)
			&& (stateTo.getValue(GantryRammerBlock.FACING) == direction)
			&& !stateFrom.getValue(GantryShaftBlock.POWERED)
		) {
			cir.setReturnValue(
				GantryRammerBlockEntity.getGantryPinionModifier(stateFrom.getValue(GantryShaftBlock.FACING),
					stateTo.getValue(GantryRammerBlock.FACING))
			);
		}
	}
}
