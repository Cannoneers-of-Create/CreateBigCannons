package rbasamoyai.createbigcannons.cannon_control.config;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface CannonMountBlockPropertiesProvider {

	float maximumElevation(Level level, BlockState state, BlockPos pos);
	default float maximumElevation(BlockEntity be) { return this.maximumElevation(be.getLevel(), be.getBlockState(), be.getBlockPos()); }

	float maximumDepression(Level level, BlockState state, BlockPos pos);
	default float maximumDepression(BlockEntity be) { return this.maximumDepression(be.getLevel(), be.getBlockState(), be.getBlockPos()); }

}
