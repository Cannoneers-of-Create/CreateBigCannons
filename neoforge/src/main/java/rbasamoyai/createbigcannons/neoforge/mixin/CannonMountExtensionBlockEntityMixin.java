package rbasamoyai.createbigcannons.neoforge.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountExtensionBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.ExtendsCannonMount;
import rbasamoyai.createbigcannons.neoforge.remix.CBCHasIItemHandlerBlockEntity;

@Mixin(CannonMountExtensionBlockEntity.class)
public abstract class CannonMountExtensionBlockEntityMixin extends KineticBlockEntity implements ExtendsCannonMount, CBCHasIItemHandlerBlockEntity {

	CannonMountExtensionBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

    @Override
    @Nullable
    public IItemHandler getItemHandler(Direction side) {
        CannonMountBlockEntity cannonMount = this.getCannonMount();
        return cannonMount == null ? null : ((CBCHasIItemHandlerBlockEntity) cannonMount).getItemHandler(side);
    }

}
