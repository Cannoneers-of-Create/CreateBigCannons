package rbasamoyai.createbigcannons.cannons;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public interface CannonContraptionProviderBlock {

	@Nonnull AbstractMountedCannonContraption getCannonContraption();
	Direction getFacing(BlockState state);

	default boolean canConnectToSide(BlockState state, Direction face) { return this.getFacing(state).getAxis() == face.getAxis(); }

	CannonCastShape getCannonShape();
	default CannonCastShape getCannonShapeInLevel(LevelAccessor level, BlockState state, BlockPos pos) { return this.getCannonShape(); }

	boolean isComplete(BlockState state);

}
