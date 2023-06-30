package rbasamoyai.createbigcannons.forge.mixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.Shadow;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(CannonMountBlockEntity.class)
public abstract class CannonMountMixin extends KineticBlockEntity {

	CannonMountMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@Shadow
	protected PitchOrientedContraptionEntity mountedContraption;

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.mountedContraption != null) {
			return this.mountedContraption.getCapability(cap, side).cast();
		}
		return super.getCapability(cap, side);
	}

}
