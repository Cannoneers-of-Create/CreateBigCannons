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
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.ExtendsCannonMount;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.YawControllerBlockEntity;
import rbasamoyai.createbigcannons.neoforge.remix.CBCHasIItemHandlerBlockEntity;

@Mixin(YawControllerBlockEntity.class)
public abstract class YawControllerBlockEntityMixin extends KineticBlockEntity implements ExtendsCannonMount, CBCHasIItemHandlerBlockEntity {

	YawControllerBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

    @Nullable
    @Override
    public IItemHandler getItemHandler(Direction side) {
        CannonMountBlockEntity cannonMount = this.getCannonMount();
        return cannonMount == null ? null : ((CBCHasIItemHandlerBlockEntity) cannonMount).getItemHandler(side);
    }

}
