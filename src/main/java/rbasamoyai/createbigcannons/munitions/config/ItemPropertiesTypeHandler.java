package rbasamoyai.createbigcannons.munitions.config;

import javax.annotation.Nonnull;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ItemPropertiesTypeHandler<PROPERTIES> extends PropertiesTypeHandler<Item, PROPERTIES> {

	@Nonnull public final PROPERTIES getPropertiesOf(ItemStack itemStack) { return this.getPropertiesOf(itemStack.getItem()); }

}
