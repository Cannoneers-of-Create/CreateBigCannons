package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.ritchiesprojectilelib.network.ClientboundPreciseMotionSyncPacket;
import rbasamoyai.ritchiesprojectilelib.network.RPLClientHandlers;

@Mixin(RPLClientHandlers.class)
public class RPLClientHandlersMixin {

	@WrapMethod(method = "syncPreciseMotion", remap = false)
	private static void createbigcannons$syncPreciseMotion(ClientboundPreciseMotionSyncPacket packet, Operation<Void> original, @Local Entity entity) {
        original.call(packet);
		if (entity instanceof AbstractCannonProjectile proj)
			proj.updateKinematics(packet);
	}

}
