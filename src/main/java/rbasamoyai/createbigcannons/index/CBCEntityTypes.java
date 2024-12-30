package rbasamoyai.createbigcannons.index;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import java.util.function.Consumer;

import com.simibubi.create.content.contraptions.render.OrientedContraptionEntityRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageRenderer;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.multiloader.EntityTypeConfigurator;
import rbasamoyai.createbigcannons.munitions.GasCloudEntity;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.autocannon.ap_round.APAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.bullet.MachineGunProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakBurst;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell.APShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot.APShotProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell.DropMortarShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell.DropMortarShellRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotBagProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotBurst;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotBurstRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.he_shell.HEShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.PrimedPropellant;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.PrimedPropellantRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelBurst;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelBurstRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeEmitterEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot.SolidShotProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone.TrafficConeProjectile;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler;
import rbasamoyai.ritchiesprojectilelib.RPLTags;

public class CBCEntityTypes {

	public static final EntityEntry<PitchOrientedContraptionEntity> PITCH_ORIENTED_CONTRAPTION = REGISTRATE
		.entity("pitch_contraption", PitchOrientedContraptionEntity::new, MobCategory.MISC)
		.properties(configure(c -> c.trackingRange(16)
			.updateInterval(3)
			.updateVelocity(true)
			.fireImmune()
			.size(1, 1)))
		.renderer(() -> OrientedContraptionEntityRenderer::new)
		.register();

	public static final EntityEntry<SolidShotProjectile> SHOT = cannonProjectile("shot", SolidShotProjectile::new, CBCMunitionPropertiesHandlers.INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<HEShellProjectile> HE_SHELL = cannonProjectile("he_shell", HEShellProjectile::new, "High Explosive (HE) Shell", CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<ShrapnelShellProjectile> SHRAPNEL_SHELL = cannonProjectile("shrapnel_shell", ShrapnelShellProjectile::new, CBCMunitionPropertiesHandlers.SHRAPNEL_SHELL);
	public static final EntityEntry<GrapeshotBagProjectile> BAG_OF_GRAPESHOT = cannonProjectile("bag_of_grapeshot", GrapeshotBagProjectile::new, CBCMunitionPropertiesHandlers.BAG_OF_GRAPESHOT);
	public static final EntityEntry<APShotProjectile> AP_SHOT = cannonProjectile("ap_shot", APShotProjectile::new, "Armor Piercing (AP) Shot", CBCMunitionPropertiesHandlers.INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<TrafficConeProjectile> TRAFFIC_CONE = cannonProjectile("traffic_cone", TrafficConeProjectile::new, CBCMunitionPropertiesHandlers.INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<APShellProjectile> AP_SHELL = cannonProjectile("ap_shell", APShellProjectile::new, "Armor Piercing (AP) Shell", CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<FluidShellProjectile> FLUID_SHELL = cannonProjectile("fluid_shell", FluidShellProjectile::new, CBCMunitionPropertiesHandlers.FLUID_SHELL);
	public static final EntityEntry<SmokeShellProjectile> SMOKE_SHELL = cannonProjectile("smoke_shell", SmokeShellProjectile::new, CBCMunitionPropertiesHandlers.SMOKE_SHELL);
	public static final EntityEntry<MortarStoneProjectile> MORTAR_STONE = cannonProjectile("mortar_stone", MortarStoneProjectile::new, CBCMunitionPropertiesHandlers.MORTAR_STONE);

	public static final EntityEntry<DropMortarShellProjectile> DROP_MORTAR_SHELL = REGISTRATE
		.entity("drop_mortar_shell", DropMortarShellProjectile::new, MobCategory.MISC)
		.properties(cannonProperties())
		.renderer(() -> DropMortarShellRenderer::new)
		.tag(RPLTags.PRECISE_MOTION)
		.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, CBCMunitionPropertiesHandlers.DROP_MORTAR_SHELL))
		.register();

	public static final EntityEntry<ShrapnelBurst> SHRAPNEL_BURST = REGISTRATE
		.entity("shrapnel_burst", ShrapnelBurst::new, MobCategory.MISC)
		.properties(shrapnel())
		.renderer(() -> ShrapnelBurstRenderer::new)
		.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, CBCMunitionPropertiesHandlers.PROJECTILE_BURST))
		.register();

	public static final EntityEntry<FlakBurst> FLAK_BURST = REGISTRATE
		.entity("flak_burst", FlakBurst::new, MobCategory.MISC)
		.properties(shrapnel())
		.renderer(() -> ShrapnelBurstRenderer::new)
		.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, CBCMunitionPropertiesHandlers.PROJECTILE_BURST))
		.register();

	public static final EntityEntry<GrapeshotBurst> GRAPESHOT_BURST = REGISTRATE
		.entity("grapeshot_burst", GrapeshotBurst::new, MobCategory.MISC)
		.properties(shrapnel())
		.renderer(() -> GrapeshotBurstRenderer::new)
		.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, CBCMunitionPropertiesHandlers.PROJECTILE_BURST))
		.register();

	public static final EntityEntry<FluidBlobBurst> FLUID_BLOB_BURST = REGISTRATE
		.entity("fluid_blob_burst", FluidBlobBurst::new, MobCategory.MISC)
		.properties(shrapnel())
		.renderer(() -> NoopRenderer::new)
		.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, CBCMunitionPropertiesHandlers.PROJECTILE_BURST))
		.register();

	public static final EntityEntry<SmokeEmitterEntity> SMOKE_EMITTER = REGISTRATE
		.entity("smoke_emitter", SmokeEmitterEntity::new, MobCategory.MISC)
		.properties(configure(c -> c.fireImmune()
			.trackingRange(16)
			.size(0, 0)))
		.renderer(() -> NoopRenderer::new)
		.register();

	public static final EntityEntry<GasCloudEntity> GAS_CLOUD = REGISTRATE
		.entity("gas_cloud", GasCloudEntity::new, MobCategory.MISC)
		.properties(configure(c -> c.fireImmune()
			.trackingRange(16)
			.size(0, 0)))
		.renderer(() -> NoopRenderer::new)
		.register();

	public static final EntityEntry<CannonCarriageEntity> CANNON_CARRIAGE = REGISTRATE
		.entity("cannon_carriage", CannonCarriageEntity::new, MobCategory.MISC)
		.properties(configure(c -> c.trackingRange(8)
			.fireImmune()
			.updateVelocity(true)
			.size(1.5f, 1.5f)))
		.renderer(() -> CannonCarriageRenderer::new)
		.register();
	public static final EntityEntry<APAutocannonProjectile> AP_AUTOCANNON = autocannonProjectile("ap_autocannon", APAutocannonProjectile::new, "Armor Piercing (AP) Autocannon Round", CBCMunitionPropertiesHandlers.INERT_AUTOCANNON_PROJECTILE);
	public static final EntityEntry<FlakAutocannonProjectile> FLAK_AUTOCANNON = autocannonProjectile("flak_autocannon", FlakAutocannonProjectile::new, "Flak Autocannon Round", CBCMunitionPropertiesHandlers.FLAK_AUTOCANNON);
	public static final EntityEntry<MachineGunProjectile> MACHINE_GUN_BULLET = autocannonProjectile("machine_gun_bullet", MachineGunProjectile::new, CBCMunitionPropertiesHandlers.INERT_AUTOCANNON_PROJECTILE);

	public static final EntityEntry<PrimedPropellant> PRIMED_PROPELLANT = REGISTRATE
		.entity("primed_propellant", PrimedPropellant::new, MobCategory.MISC)
		.properties(configure(c -> c.fireImmune()
			.size(0.98F, 0.98F)
			.trackingRange(10)
			.updateInterval(10)))
		.renderer(() -> PrimedPropellantRenderer::new)
		.register();


	private static <T extends AbstractBigCannonProjectile> EntityEntry<T>
		cannonProjectile(String id, EntityFactory<T> factory, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
			.entity(id, factory, MobCategory.MISC)
			.properties(cannonProperties())
			.renderer(() -> BigCannonProjectileRenderer::new)
			.tag(RPLTags.PRECISE_MOTION)
			.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
			.register();
	}

	private static <T extends AbstractBigCannonProjectile> EntityEntry<T>
		cannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
			.entity(id, factory, MobCategory.MISC)
			.properties(cannonProperties())
			.renderer(() -> BigCannonProjectileRenderer::new)
			.lang(enUSdiffLang)
			.tag(RPLTags.PRECISE_MOTION)
			.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
			.register();
	}

	private static <T extends AbstractAutocannonProjectile> EntityEntry<T>
		autocannonProjectile(String id, EntityFactory<T> factory, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
			.entity(id, factory, MobCategory.MISC)
			.properties(autocannonProperties())
			.renderer(() -> AutocannonProjectileRenderer::new)
			.tag(RPLTags.PRECISE_MOTION)
			.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
			.register();
	}

	private static <T extends AbstractAutocannonProjectile> EntityEntry<T>
		autocannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
			.entity(id, factory, MobCategory.MISC)
			.properties(autocannonProperties())
			.renderer(() -> AutocannonProjectileRenderer::new)
			.lang(enUSdiffLang)
			.tag(RPLTags.PRECISE_MOTION)
			.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
			.register();
	}

	public static void register() {
	}

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
