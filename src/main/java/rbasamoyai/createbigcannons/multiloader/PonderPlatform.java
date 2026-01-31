package rbasamoyai.createbigcannons.multiloader;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class PonderPlatform {

	public static void drain(BlockEntity be, int count, @Nullable Direction dir) {
		IFluidHandler handler = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), be.getBlockState(), be, dir);
		if (handler != null) {
		    handler.drain(count, IFluidHandler.FluidAction.EXECUTE);
		}
	}

	public static void fillWith(BlockEntity be, Fluid fluid, int count, @Nullable Direction dir) {
        IFluidHandler handler = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), be.getBlockState(), be, dir);
        if (handler != null) {
            handler.fill(new FluidStack(fluid, count), IFluidHandler.FluidAction.EXECUTE);
        }
	}

}
