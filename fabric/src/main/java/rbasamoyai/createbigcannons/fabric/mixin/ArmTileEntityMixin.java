package rbasamoyai.createbigcannons.fabric.mixin;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCArmInteractionPointTypes;

@Mixin(ArmBlockEntity.class)
public class ArmTileEntityMixin extends KineticBlockEntity {

	ArmTileEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@Inject(method = "depositItem",
		at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/mechanicalArm/ArmInteractionPoint;insert(Lnet/minecraft/world/item/ItemStack;Lnet/fabricmc/fabric/api/transfer/v1/transaction/TransactionContext;)Lnet/minecraft/world/item/ItemStack;"),
		locals = LocalCapture.CAPTURE_FAILHARD)
	private void createbigcannons$depositItem(CallbackInfo ci, ArmInteractionPoint point, ItemStack stack) {
		ArmInteractionPointType type = point.getType();
		if (type != CBCArmInteractionPointTypes.CANNON_MOUNT) return;
		stack.getOrCreateTag().putBoolean("DontSimulate", true);
	}

}
