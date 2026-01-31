package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllKeys;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsClient;

import net.createmod.catnip.platform.services.NetworkHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountValueScreen;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ServerboundSetFixedCannonMountValuePacket;

@Mixin(ValueSettingsClient.class)
public class ValueSettingsClientMixin {

	@Shadow public BlockPos interactHeldPos;
    @Shadow public InteractionHand interactHeldHand;
    @Shadow public Direction interactHeldFace;

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/gui/ScreenOpener;open(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void createbigcannons$tick$openScreen(Screen screen, Operation<Void> original, @Local ValueSettingsBehaviour valueSettingBehaviour,
												  @Local Player player, @Local BlockHitResult blockHitResult) {
		if (valueSettingBehaviour instanceof FixedCannonMountScrollValueBehaviour fixedMountBehaviour) {
			original.call(new FixedCannonMountValueScreen(this.interactHeldPos, valueSettingBehaviour.createBoard(player, blockHitResult),
				valueSettingBehaviour.getValueSettings(), valueSettingBehaviour::newSettingHovered, fixedMountBehaviour.setsPitch(), valueSettingBehaviour.netId()));
			return;
		}
		original.call(screen);
	}

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/platform/services/NetworkHelper;sendToServer(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;)V"), remap = false)
    private void createbigcannons$tick$cancelPacket(NetworkHelper instance, CustomPacketPayload message, Operation<Void> original, @Local ValueSettingsBehaviour valueSettingBehaviour, @Local HitResult hitResult) {
        if (valueSettingBehaviour instanceof FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour fixedMountBehaviour && hitResult instanceof BlockHitResult blockHitResult) {
            NetworkPlatform.sendToServer(new ServerboundSetFixedCannonMountValuePacket(this.interactHeldPos, 0, 0,
                this.interactHeldHand, blockHitResult, this.interactHeldFace, AllKeys.ctrlDown(), fixedMountBehaviour.setsPitch()));
            return;
        }
        original.call(instance, message);
    }

}
