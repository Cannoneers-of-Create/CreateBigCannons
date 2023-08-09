package rbasamoyai.createbigcannons.fabric.mixin.client;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.fabric.CBCClientFabric;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@Inject(at = @At("TAIL"), method = "onPress")
	private void createbigcannons$onPress(long windowPointer, int button, int action, int modifiers, CallbackInfo ci) {
		CBCClientFabric.mouseInput(button, action == 1);
	}

}
