package rbasamoyai.createbigcannons.fabric.mixin;

import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemTransferable;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(CannonMountBlockEntity.class)
public abstract class CannonMountMixin extends KineticBlockEntity implements ItemTransferable {

	CannonMountMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@Shadow
	protected PitchOrientedContraptionEntity mountedContraption;

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
		return this.mountedContraption instanceof ItemTransferable transferable ? transferable.getItemStorage(face) : null;
	}


}
