package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.kinetics.belt.transport.BeltMovementHandler;

import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(BeltMovementHandler.class)
public class BeltMovementHandlerMixin {

	@WrapMethod(method = "canBeTransported", remap = false)
	private static boolean createbigcannons$canBeTransported(Entity entity, Operation<Boolean> original) {
        return !(entity instanceof PitchOrientedContraptionEntity) && original.call(entity);
    }

}
