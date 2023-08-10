package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z", ordinal = 0))
	private boolean createbigcannons$render$isInvisible(Entity instance) {
		return instance.isInvisible() || instance.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(instance) != null;
	}

}
