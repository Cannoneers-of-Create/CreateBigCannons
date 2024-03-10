package rbasamoyai.createbigcannons.mixin.compat.create;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.bearing.MechanicalBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillingContraption;

@Mixin(MechanicalBearingBlockEntity.class)
public abstract class MechanicalBearingBlockEntityMixin extends GeneratingKineticBlockEntity {

	@Shadow
	protected ControlledContraptionEntity movedContraption;
	@Shadow
	protected boolean running;

	MechanicalBearingBlockEntityMixin(BlockEntityType<? extends MechanicalBearingBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Inject(at = @At("HEAD"), method = "tick", remap = false)
	public void createbigcannons$tick(CallbackInfo ci) {
		if (this.level.isClientSide || !this.running || this.movedContraption == null) return;
		List<ControlledContraptionEntity> contraptions = this.level.getEntitiesOfClass(ControlledContraptionEntity.class, this.movedContraption.getBoundingBox().inflate(2), e -> !e.equals(this.movedContraption));
		for (ControlledContraptionEntity e : contraptions) {
			if (!(e.getContraption() instanceof CannonDrillingContraption drill)) continue;
			BlockPos drillBase = drill.anchor.relative(drill.orientation(), -1);
			if (!(this.level.getBlockEntity(drillBase) instanceof AbstractCannonDrillBlockEntity drillBE)) continue;
			drillBE.collideWithContraptionToBore(this.movedContraption, false);
		}
	}

}
