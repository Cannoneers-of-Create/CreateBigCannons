package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.contraptions.render.NonStationaryLighter;

import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;

@Mixin(AbstractMountedCannonContraption.class)
public abstract class AbstractMountedCannonContraptionMixin extends Contraption {

	@Override
	public ContraptionLighter<?> makeLighter() {
		return new NonStationaryLighter<>(this);
	}

}
