package rbasamoyai.createbigcannons.fabric.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.fabricators_of_create.porting_lib.block.CaughtFireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.PowderChargeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.PrimedPropellant;

@Mixin(PowderChargeBlock.class)
public abstract class PowderChargeBlockMixin extends RotatedPillarBlock implements CaughtFireBlock {

	PowderChargeBlockMixin(Properties properties) { super(properties); }

	@Shadow public abstract PrimedPropellant spawnPrimedPropellant(Level level, BlockPos pos, BlockState state);

	@Override
	public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
		this.spawnPrimedPropellant(level, pos, state);
		level.removeBlock(pos, false);
	}

}
