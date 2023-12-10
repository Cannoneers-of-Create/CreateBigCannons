package rbasamoyai.createbigcannons.cannons;

import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;

import javax.annotation.Nonnull;

public interface CannonContraptionProviderBlock {

	@Nonnull AbstractMountedCannonContraption getCannonContraption();

}
