package rbasamoyai.createbigcannons.crafting.boring;

import java.util.List;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class CannonDrillBlockEntity extends AbstractCannonDrillBlockEntity {

	protected FluidTank lubricant;

	public CannonDrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.lubricant = new SmartFluidTank(1000, this::onFluidStackChanged).setValidator(fs -> fs.getFluid() == Fluids.WATER);
	}

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent evt) {
        evt.registerBlockEntity(Capabilities.FluidHandler.BLOCK, CBCBlockEntities.CANNON_DRILL.get(), (be, dir) -> {
            if (dir == null)
                return null;
            Direction facing = be.getBlockState().getValue(BlockStateProperties.FACING);
            boolean alongFirst = be.getBlockState().getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
            Direction.Axis pipeAxis = switch (facing.getAxis()) {
                case X -> alongFirst ? Direction.Axis.Z : Direction.Axis.Y;
                case Y -> alongFirst ? Direction.Axis.Z : Direction.Axis.X;
                default -> alongFirst ? Direction.Axis.Y : Direction.Axis.X;
            };
            return pipeAxis == dir.getAxis() ? ((CannonDrillBlockEntity) be).lubricant : null;
        });
    }

	protected void onFluidStackChanged(FluidStack newStack) {
		if (this.hasLevel() && !this.getLevel().isClientSide) {
			this.notifyUpdate();
		}
	}

	@Override
	protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(compound, registries, clientPacket);
		this.lubricant.readFromNBT(registries, compound.getCompound("FluidContent"));
	}

	@Override
	protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(compound, registries, clientPacket);
		compound.put("FluidContent", this.lubricant.writeToNBT(registries, new CompoundTag()));
	}

	@Override
	protected boolean drainLubricant(int drainSpeed) {
		return this.lubricant.drain(drainSpeed, IFluidHandler.FluidAction.EXECUTE).getAmount() < drainSpeed;
	}

	@Override
	protected void addFluidInfoToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		this.containedFluidTooltip(tooltip, isPlayerSneaking, this.lubricant);
	}

}
