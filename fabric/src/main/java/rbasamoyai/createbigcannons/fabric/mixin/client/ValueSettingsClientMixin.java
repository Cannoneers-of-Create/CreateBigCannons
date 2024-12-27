package rbasamoyai.createbigcannons.fabric.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllKeys;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsClient;

import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ServerboundSetFixedCannonMountValuePacket;

@Mixin(ValueSettingsClient.class)
public class ValueSettingsClientMixin {

	@Shadow public BlockPos interactHeldPos;
	@Shadow public InteractionHand interactHeldHand;
	@Shadow public Direction interactHeldFace;

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lme/pepperbell/simplenetworking/SimpleChannel;sendToServer(Lme/pepperbell/simplenetworking/C2SPacket;)V"), remap = false)
	private void createbigcannons$tick$cancelPacket(SimpleChannel instance, C2SPacket packet, Operation<Void> original,
													@Local ValueSettingsBehaviour valueSettingBehaviour) {
		if (valueSettingBehaviour instanceof FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour fixedMountBehaviour) {
			NetworkPlatform.sendToServer(new ServerboundSetFixedCannonMountValuePacket(this.interactHeldPos, 0, 0,
				this.interactHeldHand, this.interactHeldFace, AllKeys.ctrlDown(), fixedMountBehaviour.setsPitch()));
			return;
		}
		original.call(instance, packet);
	}

}
