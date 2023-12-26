package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;

public class SmokeShellProjectile extends FuzedBigCannonProjectile<SmokeShellProperties> {

	public SmokeShellProjectile(EntityType<? extends SmokeShellProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void detonate() {
		SmokeShellProperties properties = this.getProperties();
		this.level().explode(null, this.getX(), this.getY(), this.getZ(), 2, Level.ExplosionInteraction.NONE);
		if (properties != null) {
			SmokeEmitterEntity smoke = CBCEntityTypes.SMOKE_EMITTER.create(this.level());
			smoke.setPos(this.position());
			smoke.setDuration(properties.smokeDuration());
			smoke.setSize(properties.smokeScale());
			this.level().addFreshEntity(smoke);
		}
		this.discard();
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SMOKE_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
