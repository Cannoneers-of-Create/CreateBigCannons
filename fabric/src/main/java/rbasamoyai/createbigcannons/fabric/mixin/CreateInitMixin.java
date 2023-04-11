package rbasamoyai.createbigcannons.fabric.mixin;

import com.simibubi.create.Create;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.DefaultFluidCompat;

@Mixin(Create.class)
public class CreateInitMixin {

	@Inject(method = "onInitialize", at = @At("TAIL"), remap = false)
	private void createbigcannons$onInitialize(CallbackInfo ci) {
		DefaultFluidCompat.registerCreateBlobEffects();
	}

}
