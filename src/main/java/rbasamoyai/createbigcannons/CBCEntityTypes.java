package rbasamoyai.createbigcannons;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.OrientedContraptionEntityRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageRenderer;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.CannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.apshell.APShellProjectile;
import rbasamoyai.createbigcannons.munitions.fluidshell.FluidBlob;
import rbasamoyai.createbigcannons.munitions.fluidshell.FluidShellProjectile;
import rbasamoyai.createbigcannons.munitions.grapeshot.Grapeshot;
import rbasamoyai.createbigcannons.munitions.grapeshot.GrapeshotCannonProjectile;
import rbasamoyai.createbigcannons.munitions.grapeshot.GrapeshotRenderer;
import rbasamoyai.createbigcannons.munitions.heshell.HEShellProjectile;
import rbasamoyai.createbigcannons.munitions.mortarstone.MortarStoneProjectile;
import rbasamoyai.createbigcannons.munitions.shot.ShotProjectile;
import rbasamoyai.createbigcannons.munitions.shrapnel.Shrapnel;
import rbasamoyai.createbigcannons.munitions.shrapnel.ShrapnelRenderer;
import rbasamoyai.createbigcannons.munitions.shrapnel.ShrapnelShellProjectile;

public class CBCEntityTypes {

	public static final EntityEntry<PitchOrientedContraptionEntity> PITCH_ORIENTED_CONTRAPTION = CreateBigCannons.registrate()
			.entity("pitch_contraption", PitchOrientedContraptionEntity::new, MobCategory.MISC)
			.properties(b -> b.setTrackingRange(16)
					.setUpdateInterval(3)
					.setShouldReceiveVelocityUpdates(true)
					.fireImmune())
			.properties(AbstractContraptionEntity::build)
			.renderer(() -> OrientedContraptionEntityRenderer::new)
			.register();
	
	public static final EntityEntry<ShotProjectile> SHOT = cannonProjectile("shot", ShotProjectile::new);
	public static final EntityEntry<HEShellProjectile> HE_SHELL = cannonProjectile("he_shell", HEShellProjectile::new, "High Explosive (HE) Shell");
	public static final EntityEntry<ShrapnelShellProjectile> SHRAPNEL_SHELL = cannonProjectile("shrapnel_shell", ShrapnelShellProjectile::new);
	public static final EntityEntry<GrapeshotCannonProjectile> BAG_OF_GRAPESHOT = cannonProjectile("bag_of_grapeshot", GrapeshotCannonProjectile::new);
	public static final EntityEntry<APShellProjectile> AP_SHELL = cannonProjectile("ap_shell", APShellProjectile::new, "Armor Piercing (AP) Shell");
	public static final EntityEntry<FluidShellProjectile> FLUID_SHELL = cannonProjectile("fluid_shell", FluidShellProjectile::new);
	public static final EntityEntry<MortarStoneProjectile> MORTAR_STONE = cannonProjectile("mortar_stone", MortarStoneProjectile::new);
	
	public static final EntityEntry<Shrapnel> SHRAPNEL = CreateBigCannons.registrate()
			.entity("shrapnel", Shrapnel::new, MobCategory.MISC)
			.properties(Shrapnel::build)
			.renderer(() -> ShrapnelRenderer::new)
			.register();
	
	public static final EntityEntry<Grapeshot> GRAPESHOT = CreateBigCannons.registrate()
			.entity("grapeshot", Grapeshot::new, MobCategory.MISC)
			.properties(Shrapnel::build)
			.renderer(() -> GrapeshotRenderer::new)
			.register();
	
	public static final EntityEntry<FluidBlob> FLUID_BLOB = CreateBigCannons.registrate()
			.entity("fluid_blob", FluidBlob::new, MobCategory.MISC)
			.properties(Shrapnel::build)
			.renderer(() -> NoopRenderer::new)
			.register();

	public static final EntityEntry<CannonCarriageEntity> CANNON_CARRIAGE = CreateBigCannons.registrate()
			.entity("cannon_carriage", CannonCarriageEntity::new, MobCategory.MISC)
			.properties(CannonCarriageEntity::build)
			.renderer(() -> CannonCarriageRenderer::new)
			.register();
	
	private static <T extends AbstractCannonProjectile> EntityEntry<T> cannonProjectile(String id, EntityFactory<T> factory) {
		return CreateBigCannons.registrate()
				.entity(id, factory, MobCategory.MISC)
				.properties(AbstractCannonProjectile::build)
				.renderer(() -> CannonProjectileRenderer::new)
				.register();
	}
	
	private static <T extends AbstractCannonProjectile> EntityEntry<T> cannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang) {
		return CreateBigCannons.registrate()
				.entity(id, factory, MobCategory.MISC)
				.properties(AbstractCannonProjectile::build)
				.renderer(() -> CannonProjectileRenderer::new)
				.lang(enUSdiffLang)
				.register();
	}
	
	public static void register() {}
	
}
