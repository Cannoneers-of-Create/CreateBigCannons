package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.createmod.catnip.data.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

public class FluidShellBlockEntity extends AbstractFluidShellBlockEntity {

	protected FluidTank tank;

	public FluidShellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.tank = new SmartFluidTank(getFluidShellCapacity(), this::onFluidStackChanged);
	}

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent evt) {
        evt.registerBlockEntity(Capabilities.FluidHandler.BLOCK, CBCBlockEntities.FLUID_SHELL.get(), (be, dir) -> {
            if (dir != be.getBlockState().getValue(BlockStateProperties.FACING) || be.hasFuze())
                return null;
            return ((FluidShellBlockEntity) be).tank;
        });
    }

	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put("FluidContent", this.tank.writeToNBT(registries, new CompoundTag()));
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.tank.readFromNBT(registries, tag.getCompound("FluidContent"));
	}

    @Override
    public void writeSafe(CompoundTag tag, HolderLookup.Provider registries) {
        super.writeSafe(tag, registries);
        tag.put("FluidContent", this.tank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        if (this.level != null)
            this.tank.readFromNBT(this.level.registryAccess(), componentInput.getOrDefault(CBCDataComponents.FLUID_CONTENT, CustomData.EMPTY).copyTag());
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (this.level != null)
            components.set(CBCDataComponents.FLUID_CONTENT, CustomData.of(this.tank.writeToNBT(this.level.registryAccess(), new CompoundTag())));
    }

    @Override
    protected void writeSafeComponentsForItemRequirement(PatchedDataComponentMap safeComponents) {
        super.writeSafeComponentsForItemRequirement(safeComponents);
        if (this.level != null) {
            CompoundTag fluidTag = this.tank.writeToNBT(this.level.registryAccess(), new CompoundTag());
            if (!fluidTag.isEmpty())
                safeComponents.set(CBCDataComponents.FLUID_CONTENT, CustomData.of(fluidTag));
        }
    }

    @Override
	protected void setFluidShellStack(FluidShellProjectile shell) {
		FluidStack fstack = this.tank.getFluid();
		shell.setFluidStack(fstack.isEmpty()
			? EndFluidStack.EMPTY
			: new EndFluidStack(fstack.getFluid(), fstack.getAmount(), fstack.getComponentsPatch()));
	}

	@Override
	public boolean tryEmptyItemIntoTE(Level worldIn, Player player, InteractionHand handIn, ItemStack heldItem, Direction side) {
		if (this.hasFuze() || !GenericItemEmptying.canItemBeEmptied(worldIn, heldItem)) return false;
		if (worldIn.isClientSide) return true;

		Pair<FluidStack, ItemStack> emptyingResult = GenericItemEmptying.emptyItem(worldIn, heldItem, true);
		FluidStack fluidStack = emptyingResult.getFirst();

		if (fluidStack.getAmount() != this.tank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE)) return false;

		ItemStack copyOfHeld = heldItem.copy();
		emptyingResult = GenericItemEmptying.emptyItem(worldIn, copyOfHeld, false);
		this.tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

		if (!player.isCreative()) {
			if (copyOfHeld.isEmpty())
				player.setItemInHand(handIn, emptyingResult.getSecond());
			else {
				player.setItemInHand(handIn, copyOfHeld);
				player.getInventory().placeItemBackInInventory(emptyingResult.getSecond());
			}
		}
		return true;
	}

	@Override
	public boolean tryFillItemFromTE(Level level, Player player, InteractionHand handIn, ItemStack heldItem, Direction side) {
		if (this.hasFuze() || !GenericItemFilling.canItemBeFilled(level, heldItem)) return false;
		if (level.isClientSide) return true;

		FluidStack fluid = this.tank.getFluid();
		if (fluid.isEmpty()) return false;
		int requiredAmountForItem = GenericItemFilling.getRequiredAmountForItem(level, heldItem, fluid.copy());
		if (requiredAmountForItem == -1 || requiredAmountForItem > fluid.getAmount()) return false;

		if (player.isCreative()) heldItem = heldItem.copy();
		ItemStack out = GenericItemFilling.fillItem(level, requiredAmountForItem, heldItem, fluid.copy());

		FluidStack copy = fluid.copy();
		copy.setAmount(requiredAmountForItem);
		this.tank.drain(copy, IFluidHandler.FluidAction.EXECUTE);

		if (!player.isCreative()) player.getInventory().placeItemBackInInventory(out);
		this.notifyUpdate();
		return true;
	}

    protected void onFluidStackChanged(FluidStack newStack) {
		if (this.getLevel() != null && !this.getLevel().isClientSide) this.notifyUpdate();
	}

	@Override
	protected void addFluidToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		this.containedFluidTooltip(tooltip, isPlayerSneaking, this.tank);
	}

    @Override
    public void setFluidShellItemFluidData(ItemStack stack, HolderLookup.Provider registries) {
        CustomData.set(CBCDataComponents.FLUID_CONTENT, stack, this.tank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    public void readFluidDataFromFluidShellItem(ItemStack stack, HolderLookup.Provider registries) {
        if (stack.has(CBCDataComponents.FLUID_CONTENT)) {
            this.tank.readFromNBT(registries, stack.get(CBCDataComponents.FLUID_CONTENT).copyTag());
        }
    }

}
