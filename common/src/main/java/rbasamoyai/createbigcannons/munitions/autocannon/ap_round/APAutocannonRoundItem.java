package rbasamoyai.createbigcannons.munitions.autocannon.ap_round;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;

public class APAutocannonRoundItem extends AutocannonRoundItem {

    public APAutocannonRoundItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractAutocannonProjectile<?> getAutocannonProjectile(ItemStack stack, Level level) {
        return CBCEntityTypes.AP_AUTOCANNON.create(level);
    }

	@Override
	public EntityType<? extends PropertiesMunitionEntity<? extends AutocannonProjectileProperties>> getEntityType(ItemStack stack) {
		return CBCEntityTypes.AP_AUTOCANNON.get();
	}

}
