package rbasamoyai.createbigcannons.index;

import com.simibubi.create.content.contraptions.components.structureMovement.OrientedContraptionEntityRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.cannon_control.carriage.AbstractCannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageRenderer;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.multiloader.EntityTypeConfigurator;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.autocannon.ap_round.APAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell.APShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot.APShotProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlob;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.Grapeshot;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.he_shell.HEShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot.SolidShotProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone.TrafficConeProjectile;

import java.util.function.Consumer;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

public class CBCEntityTypes {

	public static final EntityEntry<AbstractPitchOrientedContraptionEntity> PITCH_ORIENTED_CONTRAPTION = REGISTRATE
			.entity("pitch_contraption", IndexPlatform::makePitchContraption, MobCategory.MISC)
			.properties(configure(c -> c.trackingRange(16)
					.updateInterval(3)
					.updateVelocity(true)
					.fireImmune()
					.size(1, 1)))
			.renderer(() -> OrientedContraptionEntityRenderer::new)
			.register();

	public static final EntityEntry<SolidShotProjectile> SHOT = cannonProjectile("shot", SolidShotProjectile::new);
	public static final EntityEntry<HEShellProjectile> HE_SHELL = cannonProjectile("he_shell", HEShellProjectile::new, "High Explosive (HE) Shell");
	public static final EntityEntry<ShrapnelShellProjectile> SHRAPNEL_SHELL = cannonProjectile("shrapnel_shell", ShrapnelShellProjectile::new);
	public static final EntityEntry<GrapeshotCannonProjectile> BAG_OF_GRAPESHOT = cannonProjectile("bag_of_grapeshot", GrapeshotCannonProjectile::new);
	public static final EntityEntry<APShotProjectile> AP_SHOT = cannonProjectile("ap_shot", APShotProjectile::new, "Armor Piercing (AP) Shot");
	public static final EntityEntry<TrafficConeProjectile> TRAFFIC_CONE = cannonProjectile("traffic_cone", TrafficConeProjectile::new);
	public static final EntityEntry<APShellProjectile> AP_SHELL = cannonProjectile("ap_shell", APShellProjectile::new, "Armor Piercing (AP) Shell");
	public static final EntityEntry<FluidShellProjectile> FLUID_SHELL = cannonProjectile("fluid_shell", FluidShellProjectile::new);
	public static final EntityEntry<MortarStoneProjectile> MORTAR_STONE = cannonProjectile("mortar_stone", MortarStoneProjectile::new);
	
	public static final EntityEntry<Shrapnel> SHRAPNEL = REGISTRATE
			.entity("shrapnel", Shrapnel::new, MobCategory.MISC)
			.properties(shrapnel())
			.renderer(() -> ShrapnelRenderer::new)
			.register();
	
	public static final EntityEntry<Grapeshot> GRAPESHOT = REGISTRATE
			.entity("grapeshot", Grapeshot::new, MobCategory.MISC)
			.properties(shrapnel())
			.renderer(() -> GrapeshotRenderer::new)
			.register();
	
	public static final EntityEntry<FluidBlob> FLUID_BLOB = REGISTRATE
			.entity("fluid_blob", FluidBlob::new, MobCategory.MISC)
			.properties(shrapnel())
			.renderer(() -> NoopRenderer::new)
			.register();

	public static final EntityEntry<AbstractCannonCarriageEntity> CANNON_CARRIAGE = REGISTRATE
			.entity("cannon_carriage", IndexPlatform::makeCannonCarriage, MobCategory.MISC)
			.properties(configure(c -> c.trackingRange(8)
					.fireImmune()
					.updateVelocity(true)
					.size(1.5f, 1.5f)))
			.renderer(() -> CannonCarriageRenderer::new)
			.register();
    public static final EntityEntry<APAutocannonProjectile> AP_AUTOCANNON = autocannonProjectile("ap_autocannon", APAutocannonProjectile::new, "Armor Piercing (AP) Autocannon Round");
	public static final EntityEntry<FlakAutocannonProjectile> FLAK_AUTOCANNON = autocannonProjectile("flak_autocannon", FlakAutocannonProjectile::new, "Flak Autocannon Round");


    private static <T extends AbstractBigCannonProjectile> EntityEntry<T> cannonProjectile(String id, EntityFactory<T> factory) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(cannonProperties())
				.renderer(() -> BigCannonProjectileRenderer::new)
				.register();
	}
	
	private static <T extends AbstractBigCannonProjectile> EntityEntry<T> cannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(cannonProperties())
				.renderer(() -> BigCannonProjectileRenderer::new)
				.lang(enUSdiffLang)
				.register();
	}

	private static <T extends AbstractAutocannonProjectile> EntityEntry<T> autocannonProjectile(String id, EntityFactory<T> factory) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(autocannonProperties())
				.renderer(() -> AutocannonProjectileRenderer::new)
				.register();
	}

	private static <T extends AbstractAutocannonProjectile> EntityEntry<T> autocannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(autocannonProperties())
				.renderer(() -> AutocannonProjectileRenderer::new)
				.lang(enUSdiffLang)
				.register();
	}
	
	public static void register() {}

	private static <T> NonNullConsumer<T> configure(Consumer<EntityTypeConfigurator> cons) {
		return b -> cons.accept(EntityTypeConfigurator.of(b));
	}

	private static <T> NonNullConsumer<T> autocannonProperties() {
		return configure(c -> c.size(0.2f, 0.2f)
				.fireImmune()
				.updateInterval(1)
				.updateVelocity(false) // Mixin ServerEntity to not track motion
				.trackingRange(16));
	}

	private static <T> NonNullConsumer<T> cannonProperties() {
		return configure(c -> c.size(0.8f, 0.8f)
				.fireImmune()
				.updateInterval(1)
				.updateVelocity(false) // Ditto
				.trackingRange(16));
	}

	private static <T> NonNullConsumer<T> shrapnel() {
		return configure(c -> c.size(0.8f, 0.8f)
				.fireImmune()
				.updateInterval(1)
				.updateVelocity(true)
				.trackingRange(16));
	}
	
}
