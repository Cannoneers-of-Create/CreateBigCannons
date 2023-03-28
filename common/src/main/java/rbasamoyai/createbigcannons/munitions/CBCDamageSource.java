package rbasamoyai.createbigcannons.munitions;

import net.minecraft.world.damagesource.DamageSource;

public class CBCDamageSource extends DamageSource {

	public CBCDamageSource(String id) {
		super(id);
	}

	@Override public DamageSource bypassArmor() {
		return super.bypassArmor();
	}
	@Override public DamageSource setIsFire() { return super.setIsFire(); }

}
