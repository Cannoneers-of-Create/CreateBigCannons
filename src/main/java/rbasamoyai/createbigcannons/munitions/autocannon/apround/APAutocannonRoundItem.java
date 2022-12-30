package rbasamoyai.createbigcannons.munitions.autocannon.apround;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class APAutocannonRoundItem extends AutocannonRoundItem {

    public APAutocannonRoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
        return CBCEntityTypes.AP_AUTOCANNON.create(level);
    }

}
