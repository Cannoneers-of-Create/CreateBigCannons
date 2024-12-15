package rbasamoyai.createbigcannons.cannon_loading;

import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public interface CanLoadBigCannon {

	@Nullable Direction createbigcannons$getAssemblyMovementDirection(Level level);
	@Nullable Direction createbigcannons$getOriginalForcedDirection(Level level);

	BlockPos createbigcannons$toLocalPos(BlockPos globalPos);

	Set<BlockPos> createbigcannons$getCannonLoadingColliders();

	static boolean intersectionLoadingEnabled() { return CBCConfigs.SERVER.kinetics.enableIntersectionLoading.get(); }

	static boolean canBreakLoader(BlockState state) {
		return !state.canBeReplaced() || state.isSolid();
	}

}
