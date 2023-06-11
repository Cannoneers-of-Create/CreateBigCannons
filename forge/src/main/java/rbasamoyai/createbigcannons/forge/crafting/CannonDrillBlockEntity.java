package rbasamoyai.createbigcannons.forge.crafting;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class CannonDrillBlockEntity extends AbstractCannonDrillBlockEntity {

	protected FluidTank lubricant;
	private LazyOptional<IFluidHandler> fluidOptional;

	public CannonDrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.lubricant = new SmartFluidTank(IndexPlatform.convertFluid(1000), this::onFluidStackChanged).setValidator(fs -> fs.getFluid() == Fluids.WATER);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
			boolean alongFirst = this.getBlockState().getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
			Direction.Axis pipeAxis = switch (facing.getAxis()) {
				case X -> alongFirst ? Direction.Axis.Z : Direction.Axis.Y;
				case Y -> alongFirst ? Direction.Axis.Z : Direction.Axis.X;
				default -> alongFirst ? Direction.Axis.Y : Direction.Axis.X;
			};
			if (side != null && pipeAxis == side.getAxis()) {
				return this.getFluidOptional().cast();
			}
		}
		return super.getCapability(cap, side);
	}

	private LazyOptional<IFluidHandler> getFluidOptional() {
		if (this.fluidOptional == null) {
			this.fluidOptional = LazyOptional.of(() -> this.lubricant);
		}
		return this.fluidOptional;
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if (this.fluidOptional != null) this.fluidOptional.invalidate();
	}

	protected void onFluidStackChanged(FluidStack newStack) {
		if (this.hasLevel() && !this.level.isClientSide) {
			this.notifyUpdate();
		}
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		this.lubricant.readFromNBT(compound.getCompound("FluidContent"));
	}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.put("FluidContent", this.lubricant.writeToNBT(new CompoundTag()));
	}

	@Override
	protected boolean drainLubricant(int drainSpeed) {
		return this.lubricant.drain(drainSpeed, IFluidHandler.FluidAction.EXECUTE).getAmount() < drainSpeed;
	}

	@Override
	protected void addFluidInfoToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		this.containedFluidTooltip(tooltip, isPlayerSneaking, this.getFluidOptional());
	}

}
