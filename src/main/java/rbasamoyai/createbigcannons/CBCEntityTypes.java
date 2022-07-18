package rbasamoyai.createbigcannons;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.OrientedContraptionEntityRenderer;
import com.simibubi.create.repack.registrate.util.entry.EntityEntry;

import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.CannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.ShotProjectile;

public class CBCEntityTypes {

	public static final EntityEntry<PitchOrientedContraptionEntity> PITCH_ORIENTED_CONTRAPTION = CreateBigCannons.registrate()
			.entity("pitch_contraption", PitchOrientedContraptionEntity::new, MobCategory.MISC)
			.properties(b -> b.setTrackingRange(5)
					.setUpdateInterval(3)
					.setShouldReceiveVelocityUpdates(true)
					.fireImmune())
			.properties(AbstractContraptionEntity::build)
			.renderer(() -> OrientedContraptionEntityRenderer::new)
			.register();
	
	public static final EntityEntry<ShotProjectile> SHOT = CreateBigCannons.registrate()
			.entity("shot", ShotProjectile::new, MobCategory.MISC)
			.properties(AbstractCannonProjectile::build)
			.renderer(() -> CannonProjectileRenderer::new)
			.register();
	
	public static void register() {}
	
}
