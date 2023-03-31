package rbasamoyai.createbigcannons.fabric.mixin;

import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.fabric.CBCClientFabric;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

	@Inject(at = @At("TAIL"), method = "keyPress")
	private void createbigcannons$keyPress(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
		CBCClientFabric.keyInput(key, scanCode, action == 1);
	}

}
