package rbasamoyai.createbigcannons.mixin.compat.create;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import com.simibubi.create.content.contraptions.Contraption;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

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

import java.util.Map;

@Mixin(AbstractContraptionEntity.class)
public abstract class AbstractContraptionEntityMixin extends Entity {

	@Unique private final AbstractContraptionEntity self = (AbstractContraptionEntity) (Object) this;

	@Shadow protected Contraption contraption;

	@Shadow public abstract void setContraptionMotion(Vec3 vec);

	@Shadow
	public abstract void disassemble();

	AbstractContraptionEntityMixin(EntityType<?> entityType, Level level) { super(entityType, level); }

	@Inject(method = "tick", at = @At("TAIL"), remap = false)
	private void createbigcannons$tick(CallbackInfo ci) {
		if (this.contraption instanceof HasFragileContraption hfc && this.contraption instanceof CanLoadBigCannon loader) {
			Map<BlockPos, BlockState> encountered = hfc.createbigcannons$getEncounteredBlocks();
			if (this.level != null
				&& !this.level.isClientSide
				&& !CanLoadBigCannon.intersectionLoadingEnabled()
				&& CanLoadBigCannon.checkForIntersectingBlocks(this.level, this.self, encountered)) {
				loader.createbigcannons$setBrokenDisassembly(true);
				this.setContraptionMotion(Vec3.ZERO);
				this.disassemble();
				encountered.clear();
			}
		}
	}

}
