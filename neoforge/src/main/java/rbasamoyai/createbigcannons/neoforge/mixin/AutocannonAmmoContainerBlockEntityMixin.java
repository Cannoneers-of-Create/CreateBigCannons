package rbasamoyai.createbigcannons.neoforge.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlockEntity;
import rbasamoyai.createbigcannons.neoforge.munitions.autocannon.AutocannonAmmoContainerInterface;
import rbasamoyai.createbigcannons.neoforge.remix.CBCHasIItemHandlerBlockEntity;

@Mixin(AutocannonAmmoContainerBlockEntity.class)
public abstract class AutocannonAmmoContainerBlockEntityMixin extends BlockEntity implements CBCHasIItemHandlerBlockEntity {

	private IItemHandler inventory;

	AutocannonAmmoContainerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

    @Override
	public IItemHandler getItemHandler(Direction side) {
		return this.inventory == null ? this.inventory = new AutocannonAmmoContainerInterface((AutocannonAmmoContainerBlockEntity) (Object) this) : this.inventory;
	}

}
