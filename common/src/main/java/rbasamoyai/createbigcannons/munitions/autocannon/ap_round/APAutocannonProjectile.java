package rbasamoyai.createbigcannons.munitions.autocannon.ap_round;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileProperties;

public class APAutocannonProjectile extends AbstractAutocannonProjectile<AutocannonProjectileProperties> {

    public APAutocannonProjectile(EntityType<? extends APAutocannonProjectile> type, Level level) {
        super(type, level);
    }

}
