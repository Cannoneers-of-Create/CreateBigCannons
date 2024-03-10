package rbasamoyai.createbigcannons.mixin.compat.create;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.piston.LinearActuatorBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rbasamoyai.createbigcannons.cannonloading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.remix.HasFragileContraption;

import java.util.HashMap;
import java.util.Map;

@Mixin(LinearActuatorBlockEntity.class)
public abstract class LinearActuatorBlockEntityMixin extends KineticBlockEntity implements HasFragileContraption {

	@Unique private final Map<BlockPos, BlockState> encounteredBlocks = new HashMap<>();

	@Shadow public AbstractContraptionEntity movedContraption;

	@Shadow public abstract void disassemble();

	LinearActuatorBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) { super(typeIn, pos, state); }

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/piston/LinearActuatorBlockEntity;assemble()V"), remap = false)
	private void createbigcannons$tick$0(CallbackInfo ci) {
		this.encounteredBlocks.clear();
	}

	@Inject(method = "tick", at = @At("TAIL"), remap = false)
	private void createbigcannons$tick$1(CallbackInfo ci) {
		if (this.level != null && !this.level.isClientSide && !CanLoadBigCannon.intersectionLoadingEnabled()
			&& this.movedContraption != null && this.movedContraption.getContraption() instanceof CanLoadBigCannon loader
			&& CanLoadBigCannon.checkForIntersectingBlocks(this.level, this.movedContraption, this.encounteredBlocks)) {
			loader.createbigcannons$setBrokenDisassembly(true);
			this.movedContraption.setDeltaMovement(Vec3.ZERO);
			this.disassemble();
			this.encounteredBlocks.clear();
		}
	}

	@Inject(method = "remove", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/piston/LinearActuatorBlockEntity;disassemble()V"))
	private void createbigcannons$remove(CallbackInfo ci) {
		this.encounteredBlocks.clear();
	}

	@Override public Map<BlockPos, BlockState> createbigcannons$getEncounteredBlocks() { return this.encounteredBlocks; }

}
