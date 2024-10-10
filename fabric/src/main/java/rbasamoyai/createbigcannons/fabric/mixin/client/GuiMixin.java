package rbasamoyai.createbigcannons.fabric.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskItem;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskOverlay;

@Mixin(Gui.class)
public abstract class GuiMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	protected abstract void renderTextureOverlay(GuiGraphics graphics, ResourceLocation textureLocation, float alpha);

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getArmor(I)Lnet/minecraft/world/item/ItemStack;"))
	private void createbigcannons$render(GuiGraphics graphics, float partialTick, CallbackInfo ci) {
		if (GasMaskItem.canShowGasMaskOverlay(this.minecraft.player))
			this.renderTextureOverlay(graphics, GasMaskOverlay.GAS_MASK_OVERLAY, 1);
	}

}
