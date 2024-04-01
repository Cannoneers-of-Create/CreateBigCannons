package rbasamoyai.createbigcannons.fabric.munitions.fluid_shell;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Pair;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellProjectile;

public class FluidShellBlockEntity extends AbstractFluidShellBlockEntity implements SidedStorageBlockEntity {

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
		if (this.getLevel() != null && !this.getLevel().isClientSide) this.notifyUpdate();
	}

	@Override
	protected void addFluidToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		this.containedFluidTooltip(tooltip, isPlayerSneaking, this.tank);
	}

	@Override
	public boolean tryEmptyItemIntoTE(Level worldIn, Player player, InteractionHand handIn, ItemStack heldItem, Direction side) {
		if (this.hasFuze() || !GenericItemEmptying.canItemBeEmptied(worldIn, heldItem)) return false;
		if (worldIn.isClientSide) return true;

		Pair<FluidStack, ItemStack> emptyingResult = GenericItemEmptying.emptyItem(worldIn, heldItem, true);
		FluidStack fluidStack = emptyingResult.getFirst();

		try (Transaction t = TransferUtil.getTransaction()) {
			try (Transaction nested = t.openNested()) {
				if (!fluidStack.isEmpty() && fluidStack.getAmount() != this.tank.insert(fluidStack.getType(), fluidStack.getAmount(), nested)) return false;
			}
			ItemStack copyOfHeld = heldItem.copy();
			emptyingResult = GenericItemEmptying.emptyItem(worldIn, copyOfHeld, false);
			TransferUtil.insertFluid(this.tank, fluidStack);
			t.commit();
			this.notifyUpdate();
			if (!player.isCreative()) {
				if (copyOfHeld.isEmpty())
					player.setItemInHand(handIn, emptyingResult.getSecond());
				else {
					player.setItemInHand(handIn, copyOfHeld);
					player.getInventory().placeItemBackInInventory(emptyingResult.getSecond());
				}
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
		long requiredAmountForItem = GenericItemFilling.getRequiredAmountForItem(level, heldItem, fluid.copy());
		if (requiredAmountForItem == -1 || requiredAmountForItem > fluid.getAmount()) return false;

		if (player.isCreative()) heldItem = heldItem.copy();
		ItemStack out = GenericItemFilling.fillItem(level, requiredAmountForItem, heldItem, fluid.copy());

		TransferUtil.extract(this.tank, fluid.getType(), requiredAmountForItem);

		if (!player.isCreative()) player.getInventory().placeItemBackInInventory(out);
		this.notifyUpdate();
		return true;
	}

}
