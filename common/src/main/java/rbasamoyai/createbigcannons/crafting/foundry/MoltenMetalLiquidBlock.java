package rbasamoyai.createbigcannons.crafting.foundry;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCLiquidBlock;
import rbasamoyai.createbigcannons.munitions.CBCDamageSource;

public class MoltenMetalLiquidBlock extends CBCLiquidBlock {

	public static final DamageSource MOLTEN_METAL = new CBCDamageSource(CreateBigCannons.MOD_ID + ".molten_metal").setIsFire();

	public MoltenMetalLiquidBlock(NonNullSupplier<? extends FlowingFluid> fluid, Properties properties) {
		super(fluid, properties);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		level.scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(level));
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		level.scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(level));
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!entity.fireImmune()) {
			entity.setSecondsOnFire(15);
			if (entity.hurt(MOLTEN_METAL, 4.0F)) {
				entity.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + level.random.nextFloat() * 0.4F);
			}
		}
		entity.fallDistance *= 0.5;
		super.entityInside(state, level, pos, entity);
	}

}
