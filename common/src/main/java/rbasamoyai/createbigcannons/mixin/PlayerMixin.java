package rbasamoyai.createbigcannons.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;

@Mixin(Player.class)
public class PlayerMixin {

	@Unique private final Player self = (Player) (Object) this;

	@Inject(method = "blockActionRestricted", at = @At("HEAD"), cancellable = true)
	private void createbigcannons$blockActionRestricted(Level level, BlockPos pos, GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
		Entity vehicle = this.self.getVehicle();
		if (CBCEntityTypes.CANNON_CARRIAGE.is(vehicle) || vehicle instanceof PitchOrientedContraptionEntity) {
			cir.setReturnValue(true);
		}
	}

}
