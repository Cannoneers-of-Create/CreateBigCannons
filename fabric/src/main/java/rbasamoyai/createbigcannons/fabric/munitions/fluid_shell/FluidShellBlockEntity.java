package rbasamoyai.createbigcannons.fabric.munitions.fluid_shell;

import com.simibubi.create.foundation.fluid.SmartFluidTank;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTransferable;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellProjectile;

import java.util.List;

public class FluidShellBlockEntity extends AbstractFluidShellBlockEntity implements FluidTransferable {

	protected FluidTank tank;

	public FluidShellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.tank = new SmartFluidTank(getFluidShellCapacity(), this::onFluidStackChanged);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("FluidContent", this.tank.writeToNBT(new CompoundTag()));
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.tank.readFromNBT(tag.getCompound("FluidContent"));
	}

	@Override
	protected void setFluidShellStack(FluidShellProjectile shell) {
		FluidStack fstack = this.tank.getFluid();
		shell.setFluidStack(fstack.isEmpty()
				? EndFluidStack.EMPTY
				: new EndFluidStack(fstack.getFluid(), (int) fstack.getAmount(), fstack.getOrCreateTag()));
	}

	@Nullable
	@Override
	public Storage<FluidVariant> getFluidStorage(@Nullable Direction face) {
		return face == this.getBlockState().getValue(BlockStateProperties.FACING) && this.fuze.isEmpty() ? this.tank : null;
	}

	protected void onFluidStackChanged(FluidStack newStack) {
		if (this.level != null && !this.level.isClientSide) this.notifyUpdate();
	}

	@Override
	protected void addFluidToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		this.containedFluidTooltip(tooltip, isPlayerSneaking, this.tank);
	}

}
