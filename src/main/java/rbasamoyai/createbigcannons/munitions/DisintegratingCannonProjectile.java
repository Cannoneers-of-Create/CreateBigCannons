package rbasamoyai.createbigcannons.munitions;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class DisintegratingCannonProjectile extends AbstractCannonProjectile {
	
	protected DisintegratingCannonProjectile(EntityType<? extends DisintegratingCannonProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level.isClientSide) {
			this.disintegrate();
			this.discard();
		}
	}
	
	protected abstract void disintegrate();

}
