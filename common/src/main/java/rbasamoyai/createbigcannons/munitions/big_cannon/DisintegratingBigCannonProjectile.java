package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class DisintegratingBigCannonProjectile<T extends BigCannonProjectileProperties> extends AbstractBigCannonProjectile<T> {

	protected DisintegratingBigCannonProjectile(EntityType<? extends DisintegratingBigCannonProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide) {
			this.disintegrate();
			this.discard();
		}
	}

	protected abstract void disintegrate();

}
