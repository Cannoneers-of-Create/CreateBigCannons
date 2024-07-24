package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;

import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.CannonContraptionLighter;

@Mixin(AbstractMountedCannonContraption.class)
public abstract class AbstractMountedCannonContraptionMixin extends Contraption {

	@Shadow
	public abstract AABB createInitialLightingBounds();

	@Override
	public ContraptionLighter<?> makeLighter() {
		AABB oldBounds = this.bounds;
		this.bounds = this.createInitialLightingBounds();
		ContraptionLighter<?> lighter = new CannonContraptionLighter<>((AbstractMountedCannonContraption) (Object) this);
		this.bounds = oldBounds;
		return lighter;
	}

}
