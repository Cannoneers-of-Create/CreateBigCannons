package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_loading.CBCModifiedContraptionRegistry;
import rbasamoyai.createbigcannons.remix.HasFragileContraption;

@Mixin(AbstractContraptionEntity.class)
public abstract class AbstractContraptionEntityMixin extends Entity {

	@Unique private final AbstractContraptionEntity self = (AbstractContraptionEntity) (Object) this;

	@Shadow protected Contraption contraption;

	@Shadow public abstract void setContraptionMotion(Vec3 vec);

	@Shadow
	public abstract void disassemble();

	AbstractContraptionEntityMixin(EntityType<?> entityType, Level level) { super(entityType, level); }

	@Inject(method = "tick", at = @At("TAIL"))
	private void createbigcannons$tick(CallbackInfo ci) {
		if (CBCModifiedContraptionRegistry.isFragileContraption(this.contraption)) {
			HasFragileContraption fragile = (HasFragileContraption) this.contraption;
			if (this.level() != null
				&& !this.level().isClientSide
				&& HasFragileContraption.checkForIntersectingBlocks(this.level(), this.self, fragile)) {
				fragile.createbigcannons$setBrokenDisassembly(true);
				this.setContraptionMotion(Vec3.ZERO);
				fragile.createbigcannons$fragileDisassemble();
				fragile.createbigcannons$getEncounteredBlocks().clear();
			}
		}
	}

}
