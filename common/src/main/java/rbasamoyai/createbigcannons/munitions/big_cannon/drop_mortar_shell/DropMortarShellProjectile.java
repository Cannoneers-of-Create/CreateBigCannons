package rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;

public class DropMortarShellProjectile extends FuzedBigCannonProjectile<DropMortarShellProperties> {

	public DropMortarShellProjectile(EntityType<? extends DropMortarShellProjectile> type, Level level) {
		super(type, level);
	}

	@Override public BlockState getRenderedBlockState() { return Blocks.AIR.defaultBlockState(); }

	@Override
	protected void detonate() {
		DropMortarShellProperties properties = this.getProperties();
		Vec3 pos = this.position();
		this.level().explode(null, this.indirectArtilleryFire(), null, pos.x, pos.y, pos.z,
			properties == null ? 0 : properties.entityDamagingExplosionPower(), false,
			Level.ExplosionInteraction.NONE);
		this.level().explode(null, this.indirectArtilleryFire(), null, pos.x, pos.y, pos.z,
			properties == null ? 0 : properties.blockDamagingExplosionPower(), false,
			CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		this.discard();
	}

}
