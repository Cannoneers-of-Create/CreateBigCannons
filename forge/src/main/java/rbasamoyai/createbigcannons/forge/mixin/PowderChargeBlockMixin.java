package rbasamoyai.createbigcannons.forge.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.PowderChargeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.PrimedPropellant;

@Mixin(PowderChargeBlock.class)
public abstract class PowderChargeBlockMixin extends RotatedPillarBlock {

	PowderChargeBlockMixin(Properties properties) { super(properties); }

	@Shadow public abstract PrimedPropellant spawnPrimedPropellant(Level level, BlockPos pos, BlockState state);

	@Override
	public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
		this.spawnPrimedPropellant(level, pos, state);
		level.removeBlock(pos, false);
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 100;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 30;
	}

}
