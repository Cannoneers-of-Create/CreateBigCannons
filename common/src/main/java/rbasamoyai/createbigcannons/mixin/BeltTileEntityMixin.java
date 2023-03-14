package rbasamoyai.createbigcannons.mixin;

import com.simibubi.create.content.contraptions.relays.belt.transport.BeltMovementHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;

@Mixin(BeltMovementHandler.class)
public class BeltTileEntityMixin {

	@Inject(at = @At("HEAD"), method = "canBeTransported", remap = false, cancellable = true)
	private static void createbigcannons$canBeTransported(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof AbstractPitchOrientedContraptionEntity) cir.setReturnValue(false);
	}

}
