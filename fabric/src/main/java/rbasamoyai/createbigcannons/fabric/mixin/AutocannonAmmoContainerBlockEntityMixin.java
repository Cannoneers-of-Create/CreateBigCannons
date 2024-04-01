package rbasamoyai.createbigcannons.fabric.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.fabric.munitions.autocannon.AutocannonAmmoContainerInterface;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlockEntity;

@Mixin(AutocannonAmmoContainerBlockEntity.class)
public abstract class AutocannonAmmoContainerBlockEntityMixin extends BlockEntity implements SidedStorageBlockEntity {

	@Unique private Storage<ItemVariant> inventory;

	AutocannonAmmoContainerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	@Nullable
	public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
		return this.inventory == null ? this.inventory = new AutocannonAmmoContainerInterface((AutocannonAmmoContainerBlockEntity) (Object) this) : this.inventory;
	}

}
