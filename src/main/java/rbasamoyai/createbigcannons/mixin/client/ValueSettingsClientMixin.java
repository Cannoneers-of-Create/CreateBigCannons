package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsClient;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountValueScreen;

@Mixin(ValueSettingsClient.class)
public class ValueSettingsClientMixin {

	@Shadow public BlockPos interactHeldPos;

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/gui/ScreenOpener;open(Lnet/minecraft/client/gui/screens/Screen;)V"))
	private void createbigcannons$tick$openScreen(Screen screen, Operation<Void> original, @Local ValueSettingsBehaviour valueSettingBehaviour,
												  @Local Player player, @Local BlockHitResult blockHitResult) {
		if (valueSettingBehaviour instanceof FixedCannonMountScrollValueBehaviour fixedMountBehaviour) {
			original.call(new FixedCannonMountValueScreen(this.interactHeldPos, valueSettingBehaviour.createBoard(player, blockHitResult),
				valueSettingBehaviour.getValueSettings(), valueSettingBehaviour::newSettingHovered, fixedMountBehaviour.setsPitch()));
			return;
		}
		original.call(screen);
	}

}
