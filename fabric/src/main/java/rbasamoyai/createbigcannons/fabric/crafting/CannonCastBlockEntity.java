package rbasamoyai.createbigcannons.fabric.crafting;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTransferable;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class CannonCastBlockEntity extends AbstractCannonCastBlockEntity implements FluidTransferable {

	protected FluidTank fluid;
	protected FluidStack leakage = FluidStack.EMPTY;

	public CannonCastBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.fluid = new SmartFluidTank(1, this::onFluidStackChanged);
	}

	@Nullable
	@Override
	public Storage<FluidVariant> getFluidStorage(@Nullable Direction face) {
		return face == Direction.UP ? this.fluid : null;
	}

	protected void onFluidStackChanged(FluidStack stack) {
		if (!this.hasLevel()) return;

		for (int yOffset = 0; yOffset < this.height; yOffset++) {
			for (int xOffset = 0; xOffset < 3; xOffset++) {
				for (int zOffset = 0; zOffset < 3; zOffset++) {
					BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
					AbstractCannonCastBlockEntity castAt = ConnectivityHandler.partAt(this.getType(), this.level, pos);
					if (castAt == null) continue;
					this.level.updateNeighbourForOutputSignal(pos, castAt.getBlockState().getBlock());
				}
			}
		}

		if (!this.level.isClientSide) {
			this.notifyUpdate();
		}

		if (this.isVirtual()) {
			if (this.fluidLevel == null) {
				this.fluidLevel = LerpedFloat.linear().startWithValue(this.getFillState());
			}
			this.fluidLevel.chase(this.getFillState(), 0.5f, LerpedFloat.Chaser.EXP);
		}
	}

	@Override
	protected void updateFluids(CompoundTag tag) {
		this.fluid.setCapacity(this.calculateCapacityFromStructure());
		this.fluid.readFromNBT(tag.getCompound("FluidContent"));
		this.leakage = tag.contains("Leakage") ? FluidStack.loadFluidStackFromNBT(tag.getCompound("Leakage")) : FluidStack.EMPTY;
	}

	@Override
	protected void updateFluidClient() {
		this.fluid.setCapacity(this.calculateCapacityFromStructure());
	}

	@Override
	protected void writeFluidToTag(CompoundTag tag) {
		tag.put("FluidContent", this.fluid.writeToNBT(new CompoundTag()));
		if (!this.leakage.isEmpty()) tag.put("Leakage", this.leakage.writeToNBT(new CompoundTag()));
	}

	@Override
	protected void leakContents() {
		FluidStack fstack = TransferUtil.extractAnyFluid(this.fluid, 20);
		if (!fstack.isEmpty()) {
			if (this.leakage.isEmpty()) {
				this.leakage = fstack;
			} else {
				this.leakage.setAmount(this.leakage.getAmount() + fstack.getAmount());
			}
		}
		if (this.leakage.getAmount() >= 1250) {
			net.minecraft.world.level.material.Fluid leakFluid = this.leakage.getFluid();
			this.level.setBlock(this.worldPosition.below(), leakFluid.defaultFluidState().createLegacyBlock(), 11);
			this.leakage.setAmount(this.leakage.getAmount() - 1000);
		}
	}

	@Override
	protected boolean canStartCasting() {
		return this.fluid.getFluidAmount() >= this.fluid.getCapacity() && !this.fluid.isEmpty();
	}

	@Override
	protected void addStructureCapacityToController(AbstractCannonCastBlockEntity controller) {
		if (controller instanceof CannonCastBlockEntity cController) {
			cController.fluid.setCapacity(cController.fluid.getCapacity() + this.castShape.fluidSize());
		}
	}

	@Override
	protected void reInitTank() {
		this.fluid = new SmartFluidTank(this.castShape.fluidSize(), this::onFluidStackChanged);
	}

	@Override
	protected void mergeControllerAndOtherFluids(AbstractCannonCastBlockEntity controller, AbstractCannonCastBlockEntity other) {
		if (controller instanceof CannonCastBlockEntity cController && other instanceof CannonCastBlockEntity cOther) {
			cController.fluid.setCapacity(cController.fluid.getCapacity() + this.castShape.fluidSize());
			TransferUtil.insertFluid(cController.fluid, TransferUtil.extractAnyFluid(cOther.fluid, cOther.fluid.getCapacity()));
			cOther.fluid = new FluidTank(1);
		}
	}

	@Override
	protected void onDestroyCenterCast() {
		CannonCastBlockEntity controller = (CannonCastBlockEntity) this.getControllerTE();
		int thisIndex = this.worldPosition.getY() - controller.worldPosition.getY();

		controller.height -= 1;
		int capacityUpTo = controller.structure.subList(0, Mth.clamp(thisIndex, 0, controller.structure.size()))
				.stream()
				.map(CannonCastShape::fluidSize)
				.reduce(Integer::sum)
				.orElseGet(() -> 0);
		long leakAmount = Mth.clamp(controller.fluid.getFluidAmount() - capacityUpTo, 0, this.castShape.fluidSize());
		FluidStack addLeak = TransferUtil.extractAnyFluid(controller.fluid, leakAmount);
		controller.fluid.setCapacity(Math.max(1, controller.fluid.getCapacity() - this.castShape.fluidSize()));
		FluidStack remaining = controller.fluid.getFluid();

		if (controller == this && this.height > 0) {
			if (this.level.getBlockEntity(this.worldPosition.above()) instanceof CannonCastBlockEntity otherCast) {
				otherCast.controllerPos = null;
				otherCast.height = this.height;
				otherCast.structure = getStructureFromPoint(this.level, this.worldPosition.above(), this.height);
				otherCast.fluid = new SmartFluidTank(otherCast.calculateCapacityFromStructure(), otherCast::onFluidStackChanged);
				TransferUtil.insertFluid(otherCast.fluid, remaining);
				otherCast.updatePotentialCastsAbove();
				otherCast.notifyUpdate();
			}
		} else {
			int oldHeight = controller.height;
			controller.height = thisIndex;
			controller.structure = controller.structure.subList(0, Mth.clamp(thisIndex, 0, controller.structure.size()));
			controller.fluid = new SmartFluidTank(controller.calculateCapacityFromStructure(), controller::onFluidStackChanged);
			long firstRemaining = remaining.getAmount() - TransferUtil.insertFluid(controller.fluid, remaining);
			if (!remaining.isEmpty()) remaining.setAmount(firstRemaining);
			controller.updateRecipes = true;
			controller.notifyUpdate();

			if (this.level.getBlockEntity(this.worldPosition.above()) instanceof CannonCastBlockEntity otherCast) {
				otherCast.controllerPos = null;
				otherCast.height = oldHeight - controller.height;
				otherCast.structure = getStructureFromPoint(this.level, this.worldPosition.above(), otherCast.height);
				otherCast.fluid = new SmartFluidTank(otherCast.calculateCapacityFromStructure(), otherCast::onFluidStackChanged);
				TransferUtil.insertFluid(otherCast.fluid, remaining);
				otherCast.updatePotentialCastsAbove();
				otherCast.updateRecipes = true;
				otherCast.notifyUpdate();
			}
		}

		if (this.castShape.isLarge()) {
			for (BlockPos pos : BlockPos.betweenClosed(this.worldPosition.offset(-1, 0, -1), this.worldPosition.offset(1, 0, 1))) {
				if (CBCBlocks.CANNON_CAST.has(this.level.getBlockState(pos)))
					this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
			}
		}

		if (!addLeak.isEmpty() && addLeak.getAmount() >= 1000) {
			this.level.setBlock(this.worldPosition, addLeak.getFluid().defaultFluidState().createLegacyBlock(), 11);
		}
	}

	@Override
	public float getFillState() {
		return 0;
	}

	@Override protected void refreshCap() {}

	@Override
	protected boolean testWithFluid(CannonCastingRecipe recipe) {
		CannonCastBlockEntity cController = ((CannonCastBlockEntity) this.getControllerTE());
		if (cController.fluid.getFluid().isEmpty()) return false;
		return recipe.ingredient().test(cController.fluid.getFluid());
	}

	public FluidTank getTank() { return this.fluid; }

}
