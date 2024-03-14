package rbasamoyai.createbigcannons.multiloader.fabric;

import javax.annotation.Nullable;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;

public class PonderPlatformImpl {

	public static void drain(BlockEntity be, int count, @Nullable Direction dir) {
		if (!(be instanceof SidedStorageBlockEntity ft)) return;
		Storage<FluidVariant> sto = ft.getFluidStorage(dir);
		if (sto != null) TransferUtil.extractAnyFluid(sto, 144);
	}

	public static void fillWith(BlockEntity be, Fluid fluid, int count, @Nullable Direction dir) {
		if (!(be instanceof SidedStorageBlockEntity ft)) return;
		Storage<FluidVariant> sto = ft.getFluidStorage(dir);
		if (sto != null) TransferUtil.insertFluid(sto, new FluidStack(fluid, count));
	}

}
