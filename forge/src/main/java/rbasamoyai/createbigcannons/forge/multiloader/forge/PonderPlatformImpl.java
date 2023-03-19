package rbasamoyai.createbigcannons.forge.multiloader.forge;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import javax.annotation.Nullable;

public class PonderPlatformImpl {

	public static void drain(BlockEntity be, int count, @Nullable Direction dir) {
		be.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir).ifPresent(h -> {
			h.drain(count, FluidAction.EXECUTE);
		});
	}

	public static void fillWith(BlockEntity be, Fluid fluid, int count, @Nullable Direction dir) {
		be.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir).ifPresent(h -> {
			h.fill(new FluidStack(fluid, count), FluidAction.EXECUTE);
		});
	}

}
