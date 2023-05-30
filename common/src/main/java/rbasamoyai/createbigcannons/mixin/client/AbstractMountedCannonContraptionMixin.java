package rbasamoyai.createbigcannons.mixin.client;

import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.NonStationaryLighter;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionLighter;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;

@Mixin(AbstractMountedCannonContraption.class)
public abstract class AbstractMountedCannonContraptionMixin extends Contraption {

	@Override public ContraptionLighter<?> makeLighter() { return new NonStationaryLighter<>(this); }

}
