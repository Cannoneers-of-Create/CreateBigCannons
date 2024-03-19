package rbasamoyai.createbigcannons.forge.mixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import rbasamoyai.createbigcannons.forge.munitions.autocannon.AutocannonAmmoContainerInterface;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlockEntity;

@Mixin(AutocannonAmmoContainerBlockEntity.class)
public abstract class AutocannonAmmoContainerBlockEntityMixin extends BlockEntity {

	private IItemHandler inventory;
	private LazyOptional<IItemHandler> itemOptional;

	AutocannonAmmoContainerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Unique
	private IItemHandler createItemHandler() {
		return this.inventory == null ? this.inventory = new AutocannonAmmoContainerInterface((AutocannonAmmoContainerBlockEntity) (Object) this) : this.inventory;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == cap) {
			if (this.itemOptional == null)
				this.itemOptional = LazyOptional.of(this::createItemHandler);
			return this.itemOptional.cast();
		}
		return super.getCapability(cap, side);
	}

}
