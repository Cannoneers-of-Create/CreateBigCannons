package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z", ordinal = 0))
	private boolean createbigcannons$render$isInvisible(Entity instance, Operation<Boolean> original) {
		return original.call(instance) || instance.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(instance) != null;
	}

}
