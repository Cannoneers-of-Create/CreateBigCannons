package rbasamoyai.createbigcannons.forge.mixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.contraptions.OrientedContraptionEntity;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.forge.mixin_interface.GetItemStorage;

@Mixin(PitchOrientedContraptionEntity.class)
public class PitchOrientedContraptionEntityMixin extends OrientedContraptionEntity {

	private LazyOptional<IItemHandler> itemOptional;

	PitchOrientedContraptionEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (this.itemOptional == null)
				this.itemOptional = this.contraption instanceof GetItemStorage storage ? storage.getItemStorage() : LazyOptional.empty();
			return this.itemOptional.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if (this.itemOptional != null) this.itemOptional.invalidate();
	}

}
