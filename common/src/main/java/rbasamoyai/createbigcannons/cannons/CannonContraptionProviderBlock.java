package rbasamoyai.createbigcannons.cannons;

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;

public interface CannonContraptionProviderBlock {

	@Nonnull AbstractMountedCannonContraption getCannonContraption();
	Direction getFacing(BlockState state);

}
