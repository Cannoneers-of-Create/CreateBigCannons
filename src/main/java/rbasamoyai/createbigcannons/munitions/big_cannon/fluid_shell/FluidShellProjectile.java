package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;

public class FluidShellProjectile extends FuzedBigCannonProjectile {
	
	protected FluidStack finalFluid = FluidStack.EMPTY;
	
	public FluidShellProjectile(EntityType<? extends FluidShellProjectile> type, Level level) {
		super(type, level);
		this.setPenetrationPoints(3);
	}
	
	public void setFluid(FluidStack stack) {
		this.finalFluid = stack;
	}
	
	@Override
	protected void detonate() {
		Vec3 oldDelta = this.getDeltaMovement();
		this.level.explode(null, this.getX(), this.getY(), this.getZ(), 2.0f, CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		this.setDeltaMovement(oldDelta);
		
		if (!this.finalFluid.isEmpty()) {
			int mbPerBlob = CBCConfigs.SERVER.munitions.mbPerFluidBlob.get(); 
			int count = (int) Math.ceil((double) this.finalFluid.getAmount() / (double) mbPerBlob);
			float spread = CBCConfigs.SERVER.munitions.fluidBlobSpread.getF();
			List<FluidBlob> list = Shrapnel.spawnShrapnelBurst(this.level, CBCEntityTypes.FLUID_BLOB.get(), this.position(), this.getDeltaMovement(), count, spread, 0);
			for (FluidBlob blob : list) {
				FluidStack copy = this.finalFluid.copy();
				copy.setAmount(mbPerBlob);
				blob.setFluidStack(copy);
			}
		}
		this.discard();
	}
	
	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.FLUID_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}
	
}
