package rbasamoyai.createbigcannons.mixin;

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
    public void motor_checkAttachedCarriageBlocks(CallbackInfo ci) {
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
}
