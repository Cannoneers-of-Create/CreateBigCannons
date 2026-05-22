package rbasamoyai.createbigcannons.mixin.compat.sable;

import org.spongepowered.asm.mixin.Mixin;

import dev.ryanhcode.sable.api.block.BlockWithSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import rbasamoyai.createbigcannons.compat.sable.ShellSubLevelImpactCallback;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;

@Mixin(FuzedProjectileBlock.class)
public class FuzedProjectileBlockMixin implements BlockWithSubLevelCollisionCallback {
    @Override public BlockSubLevelCollisionCallback sable$getCallback() { return ShellSubLevelImpactCallback.INSTANCE; }
}
