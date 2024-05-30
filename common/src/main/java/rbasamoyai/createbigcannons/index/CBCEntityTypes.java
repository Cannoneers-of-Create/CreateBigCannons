package rbasamoyai.createbigcannons.index;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import java.util.function.Consumer;

import com.simibubi.create.content.contraptions.render.OrientedContraptionEntityRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageRenderer;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.multiloader.EntityTypeConfigurator;
import rbasamoyai.createbigcannons.munitions.BaseProjectileProperties;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.autocannon.ap_round.APAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.bullet.MachineGunProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell.APShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot.APShotProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell.DropMortarShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell.DropMortarShellProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell.DropMortarShellRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlob;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.Grapeshot;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotBagProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotBagProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.he_shell.HEShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelShellProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeEmitterEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeShellProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeShellProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot.SolidShotProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone.TrafficConeProjectile;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;
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

	public static final EntityEntry<SolidShotProjectile> SHOT = cannonProjectile("shot", SolidShotProjectile::new, CBCMunitionPropertiesSerializers.INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<HEShellProjectile> HE_SHELL = cannonProjectile("he_shell", HEShellProjectile::new, "High Explosive (HE) Shell", CBCMunitionPropertiesSerializers.COMMON_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<ShrapnelShellProjectile> SHRAPNEL_SHELL = cannonProjectile("shrapnel_shell", ShrapnelShellProjectile::new, new ShrapnelShellProperties.Serializer());
	public static final EntityEntry<GrapeshotBagProjectile> BAG_OF_GRAPESHOT = cannonProjectile("bag_of_grapeshot", GrapeshotBagProjectile::new, new GrapeshotBagProperties.Serializer());
	public static final EntityEntry<APShotProjectile> AP_SHOT = cannonProjectile("ap_shot", APShotProjectile::new, "Armor Piercing (AP) Shot", CBCMunitionPropertiesSerializers.INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<TrafficConeProjectile> TRAFFIC_CONE = cannonProjectile("traffic_cone", TrafficConeProjectile::new, CBCMunitionPropertiesSerializers.INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<APShellProjectile> AP_SHELL = cannonProjectile("ap_shell", APShellProjectile::new, "Armor Piercing (AP) Shell", CBCMunitionPropertiesSerializers.COMMON_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<FluidShellProjectile> FLUID_SHELL = cannonProjectile("fluid_shell", FluidShellProjectile::new, new FluidShellProperties.Serializer());
	public static final EntityEntry<SmokeShellProjectile> SMOKE_SHELL = cannonProjectile("smoke_shell", SmokeShellProjectile::new, new SmokeShellProperties.Serializer());
	public static final EntityEntry<MortarStoneProjectile> MORTAR_STONE = cannonProjectile("mortar_stone", MortarStoneProjectile::new, new MortarStoneProperties.Serializer());

	public static final EntityEntry<DropMortarShellProjectile> DROP_MORTAR_SHELL = REGISTRATE
		.entity("drop_mortar_shell", DropMortarShellProjectile::new, MobCategory.MISC)
		.properties(cannonProperties())
		.renderer(() -> DropMortarShellRenderer::new)
		.tag(RPLTags.PRECISE_MOTION)
		.onRegister(type -> MunitionPropertiesHandler.registerPropertiesSerializer(type, new DropMortarShellProperties.Serializer()))
		.register();

	public static final EntityEntry<Shrapnel> SHRAPNEL = REGISTRATE
		.entity("shrapnel", Shrapnel::new, MobCategory.MISC)
		.properties(shrapnel())
		.renderer(() -> ShrapnelRenderer::new)
		.onRegister(type -> MunitionPropertiesHandler.registerPropertiesSerializer(type, CBCMunitionPropertiesSerializers.BASE_PROJECTILE))
		.register();

	public static final EntityEntry<Grapeshot> GRAPESHOT = REGISTRATE
		.entity("grapeshot", Grapeshot::new, MobCategory.MISC)
		.properties(shrapnel())
		.renderer(() -> GrapeshotRenderer::new)
		.onRegister(type -> MunitionPropertiesHandler.registerPropertiesSerializer(type, CBCMunitionPropertiesSerializers.BASE_PROJECTILE))
		.register();

	public static final EntityEntry<FluidBlob> FLUID_BLOB = REGISTRATE
		.entity("fluid_blob", FluidBlob::new, MobCategory.MISC)
		.properties(shrapnel())
		.renderer(() -> NoopRenderer::new)
		.onRegister(type -> MunitionPropertiesHandler.registerPropertiesSerializer(type, CBCMunitionPropertiesSerializers.BASE_PROJECTILE))
		.register();

	public static final EntityEntry<SmokeEmitterEntity> SMOKE_EMITTER = REGISTRATE
		.entity("smoke_emitter", SmokeEmitterEntity::new, MobCategory.MISC)
		.properties(configure(c -> c.trackingRange(4)
			.fireImmune()
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
	public static final EntityEntry<APAutocannonProjectile> AP_AUTOCANNON = autocannonProjectile("ap_autocannon", APAutocannonProjectile::new, "Armor Piercing (AP) Autocannon Round", CBCMunitionPropertiesSerializers.INERT_AUTOCANNON_PROJECTILE);
	public static final EntityEntry<FlakAutocannonProjectile> FLAK_AUTOCANNON = autocannonProjectile("flak_autocannon", FlakAutocannonProjectile::new, "Flak Autocannon Round", new FlakAutocannonProjectileProperties.Serializer());
	public static final EntityEntry<MachineGunProjectile> MACHINE_GUN_BULLET = autocannonProjectile("machine_gun_bullet", MachineGunProjectile::new, CBCMunitionPropertiesSerializers.INERT_AUTOCANNON_PROJECTILE);


	private static <P extends BigCannonProjectileProperties, T extends AbstractBigCannonProjectile<P>> EntityEntry<T>
		cannonProjectile(String id, EntityFactory<T> factory, MunitionPropertiesSerializer<P> ser) {
		return REGISTRATE
			.entity(id, factory, MobCategory.MISC)
			.properties(cannonProperties())
			.renderer(() -> BigCannonProjectileRenderer::new)
			.tag(RPLTags.PRECISE_MOTION)
			.onRegister(type -> MunitionPropertiesHandler.registerPropertiesSerializer(type, ser))
			.register();
	}

	private static <P extends BigCannonProjectileProperties, T extends AbstractBigCannonProjectile<P>> EntityEntry<T>
		cannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang, MunitionPropertiesSerializer<P> ser) {
		return REGISTRATE
			.entity(id, factory, MobCategory.MISC)
			.properties(cannonProperties())
			.renderer(() -> BigCannonProjectileRenderer::new)
			.lang(enUSdiffLang)
			.tag(RPLTags.PRECISE_MOTION)
			.onRegister(type -> MunitionPropertiesHandler.registerPropertiesSerializer(type, ser))
			.register();
	}

	private static <P extends BaseProjectileProperties, T extends AbstractAutocannonProjectile<P>> EntityEntry<T>
		autocannonProjectile(String id, EntityFactory<T> factory, MunitionPropertiesSerializer<P> ser) {
		return REGISTRATE
			.entity(id, factory, MobCategory.MISC)
			.properties(autocannonProperties())
			.renderer(() -> AutocannonProjectileRenderer::new)
			.tag(RPLTags.PRECISE_MOTION)
			.onRegister(type -> MunitionPropertiesHandler.registerPropertiesSerializer(type, ser))
			.register();
	}

	private static <P extends BaseProjectileProperties, T extends AbstractAutocannonProjectile<P>> EntityEntry<T>
		autocannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang, MunitionPropertiesSerializer<P> ser) {
		return REGISTRATE
			.entity(id, factory, MobCategory.MISC)
			.properties(autocannonProperties())
			.renderer(() -> AutocannonProjectileRenderer::new)
			.lang(enUSdiffLang)
			.tag(RPLTags.PRECISE_MOTION)
			.onRegister(type -> MunitionPropertiesHandler.registerPropertiesSerializer(type, ser))
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
