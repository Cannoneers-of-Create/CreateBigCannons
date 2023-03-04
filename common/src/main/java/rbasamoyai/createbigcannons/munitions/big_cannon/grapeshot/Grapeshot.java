package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.CBCDamageSource;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;

public class Grapeshot extends Shrapnel {

	public Grapeshot(EntityType<? extends Grapeshot> type, Level level) {
		super(type, level);
	}

	@Override protected float getProjectileMass() { return 3; }

	private static final DamageSource GRAPESHOT = new CBCDamageSource(CreateBigCannons.MOD_ID + ".grapeshot");
	@Override protected DamageSource getDamageSource() { return GRAPESHOT; }

}
