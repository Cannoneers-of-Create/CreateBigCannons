package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;

import rbasamoyai.createbigcannons.base.PoleContraption;
import rbasamoyai.createbigcannons.base.PoleContraptionLighter;

@Mixin(PoleContraption.class)
public abstract class PoleContraptionMixin extends TranslatingContraption {

	@Override
	public ContraptionLighter<?> makeLighter() {
		return new PoleContraptionLighter((PoleContraption) (Object) this);
	}

}
