package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;
import java.util.function.Predicate;

import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FuzedBlockEntity extends BigCannonProjectileBlockEntity {

    protected ItemStack fuze = ItemStack.EMPTY;

	public FuzedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Fuze", this.getFuze().saveOptional(registries));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.setFuze(ItemStack.parseOptional(registries, tag.getCompound("Fuze")));
    }

    @Override
    public void writeSafe(CompoundTag tag, HolderLookup.Provider registries) {
        super.writeSafe(tag, registries);
        tag.put("Fuze", this.getFuze().saveOptional(registries));
    }

    @Override
    protected void writeSafeComponents(PatchedDataComponentMap safeComponents) {
        super.writeSafeComponents(safeComponents);
        ItemStack fuze = this.getFuze();
        if (fuze.isEmpty()) {
            safeComponents.set(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
        } else {
            safeComponents.set(CBCDataComponents.FUZE, ItemContainerContents.fromItems(List.of(fuze)));
        }
    }

    @Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		CreateLang.builder("block")
			.translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze")
			.style(ChatFormatting.YELLOW)
			.forGoggles(tooltip);
        ItemStack fuze = this.getFuze();
		if (!fuze.isEmpty() && fuze.getItem() instanceof FuzeItem fuzeItem) {
			CreateLang.builder()
				.add(fuzeItem.getDescription().copy())
				.style(ChatFormatting.GREEN)
				.forGoggles(tooltip, 1);
			fuzeItem.addExtraInfo(tooltip, isPlayerSneaking, fuze);
		} else {
			CreateLang.builder("block")
				.translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze.none")
				.style(ChatFormatting.DARK_GRAY)
				.forGoggles(tooltip, 1);
		}
		return true;
	}

	@Override public int getContainerSize() { return 2; }

	@Override public boolean isEmpty() { return super.isEmpty() && !this.hasFuze(); }

	@Override public ItemStack getItem(int slot) { return slot == 1 ? this.getFuze() : super.getItem(slot); }

	public ItemStack getFuze() { return this.fuze.copy(); }

    public void setFuze(ItemStack itemStack) { this.fuze = itemStack == null ? ItemStack.EMPTY : itemStack.copy(); }

	public boolean hasFuze() { return !this.getFuze().isEmpty(); }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.fuze = componentInput.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY).copyOne();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(CBCDataComponents.FUZE, ItemContainerContents.fromItems(List.of(this.fuze)));
    }

    @Override
	public ItemStack removeItem(int slot, int amount) {
		if (slot == 1 && amount > 0) {
            ItemStack originalCopy = this.getFuze(); // internally, ItemContainerContents#getStackInSlot copies
            ItemStack result = originalCopy.split(amount);
            this.setFuze(originalCopy);
            return result;
        }
		return super.removeItem(slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (slot == 1) {
            ItemStack fuze = this.getFuze();
			if (fuze.isEmpty())
				return ItemStack.EMPTY;
			this.setFuze(ItemStack.EMPTY);
			return fuze;
		}
		return super.removeItemNoUpdate(slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot == 1) {
			this.setFuze(stack);
			if (stack.getCount() > this.getMaxStackSize())
				stack.setCount(this.getMaxStackSize());
			this.setChanged();
			return;
		}
		super.setItem(slot, stack);
	}

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == 1) {
            return stack.getItem() instanceof FuzeItem && this.getFuze().isEmpty();
        }
        return super.canPlaceItem(index, stack);
    }

    @Override
	public void clearContent() {
		this.setFuze(ItemStack.EMPTY);
		super.clearContent();
	}

    public void tick() {
        if (this.level == null)
            return;
        final ItemStack fuze = this.getFuze();
        if (this.canDetonate(fz -> fz.onBlockTick(fuze, level, this.worldPosition, this.getBlockState()))) {
            this.detonate();
            this.setRemoved();
        }
        this.setFuze(fuze);
    }

    protected final boolean canDetonate(Predicate<FuzeItem> cons) {
        return this.level != null && !this.level.isClientSide && !this.isRemoved()
            && this.getFuze().getItem() instanceof FuzeItem fuzeItem && cons.test(fuzeItem);
    }

    protected void detonate() {
        if (this.level == null)
            return;
        BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof FuzedProjectileBlock<?,?> fuzedBlock)) {
            this.level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);
            return;
        }
        fuzedBlock.detonateProjectileOnTheSpot(this.level, this.worldPosition, state, state.getValue(FuzedProjectileBlock.FACING));
    }

}
