package rbasamoyai.createbigcannons.neoforge.remix;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.items.IItemHandler;

public interface CBCHasIItemHandlerBlockEntity {
    @Nullable IItemHandler getItemHandler(Direction side);
}
