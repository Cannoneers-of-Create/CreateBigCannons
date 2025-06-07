package rbasamoyai.createbigcannons.neoforge.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.neoforge.remix.CBCHasIItemHandlerBlockEntity;

@Mixin(CannonMountBlockEntity.class)
public abstract class CannonMountBlockEntityMixin extends SmartBlockEntity implements CBCHasIItemHandlerBlockEntity {

	CannonMountBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@Shadow
	protected PitchOrientedContraptionEntity mountedContraption;

    @Nullable
    @Override
    public IItemHandler getItemHandler(Direction side) { // TODO c6 playtest
        return this.mountedContraption != null ? this.mountedContraption.getCapability(Capabilities.ItemHandler.ENTITY) : null;
    }

}
