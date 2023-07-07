package rbasamoyai.createbigcannons.fabric.crafting;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
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
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class CannonDrillBlockEntity extends AbstractCannonDrillBlockEntity implements FluidTransferable {

	protected FluidTank lubricant;

	public CannonDrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.lubricant = new SmartFluidTank(IndexPlatform.convertFluid(1000), this::onFluidStackChanged).setValidator(fs -> fs.getFluid() == Fluids.WATER);
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
		return TransferUtil.extractAnyFluid(this.lubricant, drainSpeed).getAmount() < drainSpeed;
	}

	@Override
	protected void addFluidInfoToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		this.containedFluidTooltip(tooltip, isPlayerSneaking, this.lubricant);
	}

	@Nullable
	@Override
	public Storage<FluidVariant> getFluidStorage(@Nullable Direction face) {
		Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
		boolean alongFirst = this.getBlockState().getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
		Direction.Axis pipeAxis = switch (facing.getAxis()) {
			case X -> alongFirst ? Direction.Axis.Z : Direction.Axis.Y;
			case Y -> alongFirst ? Direction.Axis.Z : Direction.Axis.X;
			default -> alongFirst ? Direction.Axis.Y : Direction.Axis.X;
		};
		return face != null && pipeAxis == face.getAxis() ? this.lubricant : null;
	}

}
