package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) { super(entityType, level); }

    @WrapMethod(method = "blockActionRestricted")
	private boolean createbigcannons$blockActionRestricted(Level level, BlockPos pos, GameType gameMode, Operation<Boolean> original) {
		Entity vehicle = this.getVehicle();
		if (CBCEntityTypes.CANNON_CARRIAGE.is(vehicle) || vehicle instanceof PitchOrientedContraptionEntity)
			return true;
		return original.call(level, pos, gameMode);
    }

}
