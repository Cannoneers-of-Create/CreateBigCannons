package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.foundation.fluid.FluidIngredient.FluidTagIngredient;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

@Mixin(FluidTagIngredient.class)
public abstract class FixFluidTagIngredientMixin {
	
	@Final
	@Shadow
	public TagKey<Fluid> tag;
	
	@SuppressWarnings("deprecation")
	@Inject(at = @At("HEAD"), method = "testInternal(Lnet/minecraftforge/fluids/FluidStack;)Z", cancellable = true, remap = false)
	public void createbigcannons$testInternal(FluidStack stack, CallbackInfoReturnable<Boolean> cbi) {
		FluidTagIngredient self = (FluidTagIngredient) (Object) this;
		if (tag == null) {
			for (FluidStack accepted : self.getMatchingFluidStacks()) {
				if (accepted.getFluid().isSame(stack.getFluid())) {
					cbi.setReturnValue(true);
					return;
				}
			}
			cbi.setReturnValue(false);
			return;
		}
		cbi.setReturnValue(stack.getFluid().is(tag));
	}
	
}
