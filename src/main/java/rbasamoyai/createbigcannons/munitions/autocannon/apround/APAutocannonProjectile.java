package rbasamoyai.createbigcannons.munitions.autocannon.apround;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;

public class APAutocannonProjectile extends AbstractAutocannonProjectile {

    public APAutocannonProjectile(EntityType<? extends APAutocannonProjectile> type, Level level) {
        super(type, level);
        this.setBreakthroughPower((byte) 3);
    }

}
