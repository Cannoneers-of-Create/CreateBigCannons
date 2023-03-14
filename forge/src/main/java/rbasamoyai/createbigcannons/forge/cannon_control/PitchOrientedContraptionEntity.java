package rbasamoyai.createbigcannons.forge.cannon_control;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;

public class PitchOrientedContraptionEntity extends AbstractPitchOrientedContraptionEntity {

	private LazyOptional<IItemHandler> itemOptional;

	public PitchOrientedContraptionEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (this.itemOptional == null) this.itemOptional = this.contraption instanceof ItemCannon cannon ? cannon.getItemOptional() : LazyOptional.empty();
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
