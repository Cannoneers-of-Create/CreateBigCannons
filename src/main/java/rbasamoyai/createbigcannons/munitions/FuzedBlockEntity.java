package rbasamoyai.createbigcannons.munitions;

import com.simibubi.create.foundation.tileEntity.SyncedTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class FuzedBlockEntity extends SyncedTileEntity {

	protected ItemStack fuze = ItemStack.EMPTY;
	private LazyOptional<IItemHandler> fuzeOptional;
	
	public FuzedBlockEntity(BlockEntityType<? extends FuzedBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side == this.getBlockState().getValue(BlockStateProperties.FACING)) {
			if (this.fuzeOptional == null) {
				this.fuzeOptional = LazyOptional.of(this::createHandler);
			}
			return this.fuzeOptional.cast();
		}
		return super.getCapability(cap, side);
	}
	
	public void setFuze(ItemStack stack) { this.fuze = stack; }
	public ItemStack getFuze() { return this.fuze; }
	
	private IItemHandlerModifiable createHandler() {
		return new FuzeItemHandler(this);
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if (this.fuzeOptional != null) {
			this.fuzeOptional.invalidate();
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (!this.fuze.isEmpty()) {
			tag.put("Fuze", this.fuze.serializeNBT());
		}
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.fuze = tag.contains("Fuze", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Fuze")) : ItemStack.EMPTY;
	}
	
}
