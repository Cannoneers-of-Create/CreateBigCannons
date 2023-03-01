package rbasamoyai.createbigcannons.mixin;

import rbasamoyai.createbigcannons.CreateBigCannons;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        CreateBigCannons.LOGGER.info("Hello from {}!", CreateBigCannons.class.getName());
    }
}