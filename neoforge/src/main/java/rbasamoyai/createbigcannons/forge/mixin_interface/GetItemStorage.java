package rbasamoyai.createbigcannons.forge.mixin_interface;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public interface GetItemStorage {
	@Nonnull LazyOptional<IItemHandler> getItemStorage();
}
