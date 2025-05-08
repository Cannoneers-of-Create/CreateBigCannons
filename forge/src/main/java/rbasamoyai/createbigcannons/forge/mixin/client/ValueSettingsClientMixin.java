package rbasamoyai.createbigcannons.forge.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllKeys;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsClient;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.simple.SimpleChannel;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ServerboundSetFixedCannonMountValuePacket;

@Mixin(ValueSettingsClient.class)
public class ValueSettingsClientMixin {

	@Shadow public BlockPos interactHeldPos;
	@Shadow public InteractionHand interactHeldHand;
	@Shadow public Direction interactHeldFace;

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/network/simple/SimpleChannel;sendToServer(Ljava/lang/Object;)V"), remap = false)
	private void createbigcannons$tick$cancelPacket(SimpleChannel instance, Object message, Operation<Void> original, @Local ValueSettingsBehaviour valueSettingBehaviour, @Local HitResult hitResult) {
		if (valueSettingBehaviour instanceof FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour fixedMountBehaviour && hitResult instanceof BlockHitResult blockHitResult) {
			NetworkPlatform.sendToServer(new ServerboundSetFixedCannonMountValuePacket(this.interactHeldPos, 0, 0,
				this.interactHeldHand, blockHitResult, this.interactHeldFace, AllKeys.ctrlDown(), fixedMountBehaviour.setsPitch()));
			return;
		}
		original.call(instance, message);
	}

}
