package rbasamoyai.createbigcannons.munitions.fluidshell;

import java.util.List;

import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.FuzedBlockEntity;

public class FluidShellBlockEntity extends FuzedBlockEntity {

	protected FluidTank tank;
	private LazyOptional<IFluidHandler> fluidOptional;
	
	public FluidShellBlockEntity(BlockEntityType<? extends FluidShellBlockEntity> type, BlockPos pos, BlockState state) {
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
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == this.getBlockState().getValue(BlockStateProperties.FACING) && this.fuze.isEmpty()) {
			return this.getFluidOptional().cast();
		}
		return super.getCapability(cap, side);
	}
	
	public static int getFluidShellCapacity() {
		return CBCConfigs.SERVER.munitions.fluidShellCapacity.get();
	}
	
	public LazyOptional<IFluidHandler> getFluidOptional() {
		if (this.fluidOptional == null) {
			this.fluidOptional = LazyOptional.of(() -> this.tank);
		}
		return this.fluidOptional;
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if (this.fluidOptional != null) {
			this.fluidOptional.invalidate();
		}
	}
	
	protected void onFluidStackChanged(FluidStack newStack) {
		if (!this.hasLevel()) return;
		if (!this.level.isClientSide) {
			this.notifyUpdate();
		}
	}
	
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		this.containedFluidTooltip(tooltip, isPlayerSneaking, this.getFluidOptional());
		return true;
	}

}
