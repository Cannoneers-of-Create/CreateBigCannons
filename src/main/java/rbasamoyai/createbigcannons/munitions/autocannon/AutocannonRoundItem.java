package rbasamoyai.createbigcannons.munitions.autocannon;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CBCItems;

public abstract class AutocannonRoundItem extends Item {

    protected AutocannonRoundItem(Properties properties) {
        super(properties);
    }

    public abstract AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level);

    public ItemStack getCreativeTabCartridgeItem() {
        ItemStack stack = CBCItems.AUTOCANNON_CARTRIDGE.asStack();
        AutocannonCartridgeItem.writeProjectile(this.getDefaultInstance(), stack);
        return stack;
    }

}
