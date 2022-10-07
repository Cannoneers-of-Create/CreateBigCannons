package rbasamoyai.createbigcannons.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.bearing.MechanicalBearingTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillingContraption;

@Mixin(MechanicalBearingTileEntity.class)
public abstract class BearingCannonDrillFinderMixin extends GeneratingKineticTileEntity {
	
	@Shadow protected ControlledContraptionEntity movedContraption;
	@Shadow protected boolean running;
	
	public BearingCannonDrillFinderMixin(BlockEntityType<? extends MechanicalBearingTileEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Inject(at = @At("HEAD"), method = "tick", remap = false)
	public void createbigcannons$tick(CallbackInfo ci) {
		if (this.level.isClientSide || !this.running || this.movedContraption == null) return;
		List<ControlledContraptionEntity> contraptions = this.level.getEntitiesOfClass(ControlledContraptionEntity.class, this.movedContraption.getBoundingBox().inflate(2), e -> !e.equals(this.movedContraption));
		for (ControlledContraptionEntity e : contraptions) {
			if (!(e.getContraption() instanceof CannonDrillingContraption drill)) continue;
			BlockPos drillBase = drill.anchor.relative(drill.orientation(), -1);
			if (!(this.level.getBlockEntity(drillBase) instanceof CannonDrillBlockEntity drillBE)) continue;
			drillBE.collideWithContraptionToBore(this.movedContraption, false);
		}
	}
	
}
