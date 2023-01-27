package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public abstract class AbstractBigCannonProjectile extends AbstractCannonProjectile {

	protected AbstractBigCannonProjectile(EntityType<? extends AbstractCannonProjectile> type, Level level) { super(type, level); }

	public abstract BlockState getRenderedBlockState();

}
