package rbasamoyai.createbigcannons.mixin.compat.framedblocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import xfacthd.framedblocks.common.blockentity.doubled.slab.FramedAdjustableDoubleBlockEntity;

@Mixin(FramedAdjustableDoubleBlockEntity.class)
public interface FramedAdjustableDoubleBlockEntityAccessor {

	@Accessor("firstHeight") int getFirstHeight();

}
