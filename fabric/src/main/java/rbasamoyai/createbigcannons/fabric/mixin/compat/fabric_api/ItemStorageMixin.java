package rbasamoyai.createbigcannons.fabric.mixin.compat.fabric_api;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

@Mixin(ItemStorage.class)
public class ItemStorageMixin {

	@Inject(method = "lambda$static$2", at = @At("HEAD"), remap = false, cancellable = true)
	private static void createbigcannons$lambda$static$2(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity,
														 Direction direction, CallbackInfoReturnable<Storage> cir) {
		if (CBCBlockEntities.AUTOCANNON_AMMO_CONTAINER.is(blockEntity) && cir.isCancellable())
			cir.setReturnValue(null);
	}

}
