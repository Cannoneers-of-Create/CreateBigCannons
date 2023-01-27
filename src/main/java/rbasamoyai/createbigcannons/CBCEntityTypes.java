package rbasamoyai.createbigcannons;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.OrientedContraptionEntityRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageRenderer;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell.APShellProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.autocannon.ap_round.APAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot.APShotProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlob;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.Grapeshot;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.he_shell.HEShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot.SolidShotProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelShellProjectile;
import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

public class CBCEntityTypes {

	public static final EntityEntry<PitchOrientedContraptionEntity> PITCH_ORIENTED_CONTRAPTION = REGISTRATE
			.entity("pitch_contraption", PitchOrientedContraptionEntity::new, MobCategory.MISC)
			.properties(b -> b.setTrackingRange(16)
					.setUpdateInterval(3)
					.setShouldReceiveVelocityUpdates(true)
					.fireImmune())
			.properties(AbstractContraptionEntity::build)
			.renderer(() -> OrientedContraptionEntityRenderer::new)
			.register();

	public static final EntityEntry<SolidShotProjectile> SHOT = cannonProjectile("shot", SolidShotProjectile::new);
	public static final EntityEntry<HEShellProjectile> HE_SHELL = cannonProjectile("he_shell", HEShellProjectile::new, "High Explosive (HE) Shell");
	public static final EntityEntry<ShrapnelShellProjectile> SHRAPNEL_SHELL = cannonProjectile("shrapnel_shell", ShrapnelShellProjectile::new);
	public static final EntityEntry<GrapeshotCannonProjectile> BAG_OF_GRAPESHOT = cannonProjectile("bag_of_grapeshot", GrapeshotCannonProjectile::new);
	public static final EntityEntry<APShotProjectile> AP_SHOT = cannonProjectile("ap_shot", APShotProjectile::new);
	public static final EntityEntry<APShellProjectile> AP_SHELL = cannonProjectile("ap_shell", APShellProjectile::new, "Armor Piercing (AP) Shell");
	public static final EntityEntry<FluidShellProjectile> FLUID_SHELL = cannonProjectile("fluid_shell", FluidShellProjectile::new);
	public static final EntityEntry<MortarStoneProjectile> MORTAR_STONE = cannonProjectile("mortar_stone", MortarStoneProjectile::new);
	
	public static final EntityEntry<Shrapnel> SHRAPNEL = REGISTRATE
			.entity("shrapnel", Shrapnel::new, MobCategory.MISC)
			.properties(Shrapnel::build)
			.renderer(() -> ShrapnelRenderer::new)
			.register();
	
	public static final EntityEntry<Grapeshot> GRAPESHOT = REGISTRATE
			.entity("grapeshot", Grapeshot::new, MobCategory.MISC)
			.properties(Shrapnel::build)
			.renderer(() -> GrapeshotRenderer::new)
			.register();
	
	public static final EntityEntry<FluidBlob> FLUID_BLOB = REGISTRATE
			.entity("fluid_blob", FluidBlob::new, MobCategory.MISC)
			.properties(Shrapnel::build)
			.renderer(() -> NoopRenderer::new)
			.register();

	public static final EntityEntry<CannonCarriageEntity> CANNON_CARRIAGE = REGISTRATE
			.entity("cannon_carriage", CannonCarriageEntity::new, MobCategory.MISC)
			.properties(CannonCarriageEntity::build)
			.renderer(() -> CannonCarriageRenderer::new)
			.register();
    public static final EntityEntry<APAutocannonProjectile> AP_AUTOCANNON = autocannonProjectile("ap_autocannon", APAutocannonProjectile::new, "Armor Piercing (AP) Autocannon Round");
	public static final EntityEntry<FlakAutocannonProjectile> FLAK_AUTOCANNON = autocannonProjectile("flak_autocannon", FlakAutocannonProjectile::new, "Flak Autocannon Round");


    private static <T extends AbstractBigCannonProjectile> EntityEntry<T> cannonProjectile(String id, EntityFactory<T> factory) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(AbstractCannonProjectile::build)
				.renderer(() -> BigCannonProjectileRenderer::new)
				.register();
	}
	
	private static <T extends AbstractBigCannonProjectile> EntityEntry<T> cannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(AbstractCannonProjectile::build)
				.renderer(() -> BigCannonProjectileRenderer::new)
				.lang(enUSdiffLang)
				.register();
	}

	private static <T extends AbstractAutocannonProjectile> EntityEntry<T> autocannonProjectile(String id, EntityFactory<T> factory) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(AbstractAutocannonProjectile::buildAutocannon)
				.renderer(() -> AutocannonProjectileRenderer::new)
				.register();
	}

	private static <T extends AbstractAutocannonProjectile> EntityEntry<T> autocannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(AbstractAutocannonProjectile::buildAutocannon)
				.renderer(() -> AutocannonProjectileRenderer::new)
				.lang(enUSdiffLang)
				.register();
	}
	
	public static void register() {}
	
}
