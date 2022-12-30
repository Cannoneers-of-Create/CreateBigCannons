package rbasamoyai.createbigcannons.munitions.shrapnel;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.FuzedCannonProjectile;

public class ShrapnelShellProjectile extends FuzedCannonProjectile {

	public ShrapnelShellProjectile(EntityType<? extends ShrapnelShellProjectile> type, Level level) {
		super(type, level);
		this.setBreakthroughPower((byte) 1);
	}
	
	@Override
	protected void detonate() {
		Vec3 oldDelta = this.getDeltaMovement();
		this.level.explode(null, this.getX(), this.getY(), this.getZ(), 2.0f, CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		this.setDeltaMovement(oldDelta);
		int count = CBCConfigs.SERVER.munitions.shrapnelCount.get();
		float spread = CBCConfigs.SERVER.munitions.shrapnelSpread.getF();
		float damage = CBCConfigs.SERVER.munitions.shrapnelDamage.getF();
		Shrapnel.spawnShrapnelBurst(this.level, CBCEntityTypes.SHRAPNEL.get(), this.position(), this.getDeltaMovement(), count, spread, damage);
		this.discard();
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SHRAPNEL_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
