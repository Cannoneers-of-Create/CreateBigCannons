package rbasamoyai.createbigcannons.forge.cannon_control;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public interface ItemCannon {

	LazyOptional<IItemHandler> getItemOptional();

}