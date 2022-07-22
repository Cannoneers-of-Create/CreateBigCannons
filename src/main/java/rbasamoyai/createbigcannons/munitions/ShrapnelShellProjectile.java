package rbasamoyai.createbigcannons.munitions;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;

public class ShrapnelShellProjectile extends FuzedCannonProjectile {

	public ShrapnelShellProjectile(EntityType<? extends ShrapnelShellProjectile> type, Level level) {
		super(type, level);
		this.setBreakthroughPower((byte) 1);
	}
	
	@Override
	protected void detonate() {
		Vec3 oldDelta = this.getDeltaMovement();
		this.level.explode(null, this.getX(), this.getY(), this.getZ(), 2.0f, Explosion.BlockInteraction.DESTROY);
		this.setDeltaMovement(oldDelta);
		Shrapnel.spawnShrapnelBurst(this.level, this.position(), this.getDeltaMovement(), 50, 0.25f);
		this.discard();
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SHRAPNEL_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
