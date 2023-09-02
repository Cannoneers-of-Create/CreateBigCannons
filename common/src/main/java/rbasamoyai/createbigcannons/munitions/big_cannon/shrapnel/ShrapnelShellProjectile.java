package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.config.ShrapnelProperties;

public class ShrapnelShellProjectile extends FuzedBigCannonProjectile {

	public ShrapnelShellProjectile(EntityType<? extends ShrapnelShellProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void detonate() {
		Vec3 oldDelta = this.getDeltaMovement();
		this.level().explode(null, this.indirectArtilleryFire(), null, this.getX(), this.getY(), this.getZ(), 2.0f, false,
			CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		this.setDeltaMovement(oldDelta);
		ShrapnelProperties properties = this.getProperties().shrapnel();
		Shrapnel.spawnShrapnelBurst(this.level(), CBCEntityTypes.SHRAPNEL.get(), this.position(), this.getDeltaMovement(),
				properties.count(), properties.spread(), (float) properties.damage());
		this.discard();
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SHRAPNEL_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
