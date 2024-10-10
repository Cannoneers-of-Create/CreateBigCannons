package rbasamoyai.createbigcannons.forge.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import xfacthd.framedblocks.common.blockentity.doubled.FramedAdjustableDoubleBlockEntity;

@Mixin(FramedAdjustableDoubleBlockEntity.class)
public interface FramedAdjustableDoubleBlockEntityAccessor {

	@Accessor("firstHeight") int getFirstHeight();

}
