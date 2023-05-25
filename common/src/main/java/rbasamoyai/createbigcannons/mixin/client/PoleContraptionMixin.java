package rbasamoyai.createbigcannons.mixin.client;

import com.simibubi.create.content.contraptions.components.structureMovement.TranslatingContraption;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionLighter;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.base.PoleContraption;
import rbasamoyai.createbigcannons.base.PoleContraptionLighter;

@Mixin(PoleContraption.class)
public abstract class PoleContraptionMixin extends TranslatingContraption {

	@Override public ContraptionLighter<?> makeLighter() { return new PoleContraptionLighter((PoleContraption) (Object) this); }

}
