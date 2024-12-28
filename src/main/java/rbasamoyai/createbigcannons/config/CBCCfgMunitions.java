package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

import net.minecraft.world.level.Level.ExplosionInteraction;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;

public class CBCCfgMunitions extends ConfigBase {

	public final ConfigBool projectilesCanBounce = b(true, "projectilesCanBounce", Comments.projectilesCanBounce);
	public final ConfigFloat baseProjectileBounceChance = f(0.33f, 0, 1, "baseProjectileBounceChance");
	public final ConfigFloat baseProjectileFluidBounceChance = f(0.9f, 0, 1, "baseProjectileFluidBounceChance");
	public final ConfigFloat minVelocityForPenetrationBonus = f(1, 0, "minimumVelocityForPenetrationBonus", "[in Meters per Tick]", Comments.minVelocityForPenetrationBonus);
	public final ConfigFloat penetrationBonusScale = f(0.1f, 0, "penetrationBonusScale");

	public final ConfigEnum<GriefState> damageRestriction = e(GriefState.ALL_DAMAGE, "damageRestriction", Comments.damageRestriction);
	public final ConfigBool projectilesChangeSurroundings = b(true, "projectilesChangeSurroundings");

	public final ConfigGroup chunkloading = group(0, "chunkloading", "Chunkloading");
	public final ConfigBool projectilesCanChunkload = b(true, "projectilesCanChunkload");
	public final ConfigBool smokeCloudsCanChunkload = b(true, "smokeCloudsCanChunkload");

	public final ConfigGroup bigCannonMunitions = group(0, "bigCannonMunitions", "Big Cannon Munitions");
	public final ConfigEnum<AbstractBigCannonProjectile.TrailType> bigCannonTrailType = e(AbstractBigCannonProjectile.TrailType.SHORT, "trailType");
	public final ConfigBool allBigCannonProjectilesAreTracers = b(false, "allBigCannonProjectilesAreTracers", Comments.allBigCannonProjectilesAreTracers);
	public final ConfigInt quickFiringBreechItemPickupDelay = i(20, 0, 100, "quickFiringBreechItemPickupDelay", "[in Ticks]");
	public final ConfigBool quickFiringBreechItemGoesToInventory = b(false, "quickFiringBreechItemGoesToInventory");


	public final ConfigGroup fuzes = group(0, "fuzes", "Projectile Fuzes");
	public final ConfigFloat impactFuzeDetonationChance = f(0.67f, 0, 1, "impactFuzeDetonationChance", Comments.impactFuzeDetonationChance);
	public final ConfigInt impactFuzeDurability = i(3, -1, "impactFuzeDurability", Comments.impactFuzeDurability);
	public final ConfigInt proximityFuzeArmingTime = i(5, 0, "proximityFuzeArmingTime", Comments.proximityFuzeArmingTime);
	public final ConfigInt proximityFuzeScale = i(5, 1, 10, "proximityFuzeScale", Comments.proximityFuzeScale);
	public final ConfigFloat proximityFuzeSpacing = f(1.5f, 0.5f, 2f, "proximityFuzeSpacing", Comments.proximityFuzeSpacing);

	public final ConfigGroup groupedMunitions = group(0, "groupedMunitions", "Grouped Munitions");
	public final ConfigFloat fluidBlobBlockAffectChance = f(0.5f, 0, 1, "fluidBlobBlockEffectChance", Comments.fluidBlobBlockAffectChance);

	public final ConfigGroup propellant = group(0, "propellant", "Propellant");

	public final ConfigGroup autocannonMunitions = group(0, "autocannonMunitions", "Autocannon Munitions");
	public final ConfigEnum<AbstractAutocannonProjectile.TrailType> autocannonTrailType = e(AbstractAutocannonProjectile.TrailType.SHORT, "trailType");
	public final ConfigBool allAutocannonProjectilesAreTracers = b(false, "allAutocannonProjectilesAreTracers", Comments.allAutocannonProjectilesAreTracers);
	public final ConfigInt ammoContainerAutocannonRoundCapacity = i(16, 1, 128, "autocannonAmmoContainerAutocannonRoundCapacity", Comments.ammoContainerAutocannonRoundCapacity);
	public final ConfigInt ammoContainerMachineGunRoundCapacity = i(64, 1, 128, "autocannonAmmoContainerMachineGunRoundCapacity", Comments.ammoContainerMachineGunRoundCapacity);

	@Override public String getName() { return "munitions"; }

	private static class Comments {
		static String projectilesCanBounce = "If projectiles can bounce, ricochet, and be deflected.";
		static String[] damageRestriction = new String[] { "The extent to which cannon projectiles can damage surrounding blocks.",
				"All Damage - projectiles will destroy anything they hit, if applicable. Explosive projectiles will destroy blocks on detonation.",
				"No Explosive Damage - projectiles will destroy anything they hit, if applicable. Explosive projectiles will only harm entities on detonation.",
				"No Damage - projectiles will not destroy anything they hit, and will only deal entity damage. Explosive projectiles will only harm entities on detonation."
		};
		static String impactFuzeDetonationChance = "Chance that the Impact Fuze/Delayed Impact Fuze will detonate on hitting something. 0 is 0% (never), 1 is 100% (always).";
		static String impactFuzeDurability = "How many blocks the Impact Fuze/Delayed Impact Fuze can hit before breaking. Set to -1 to never break.";
		static String[] proximityFuzeArmingTime = new String[] {
				"[in Ticks]",
				"Time it takes for a proximity fuze to arm itself.",
				"After the fuze has been in the air for the specified arming time, it will detonate when it gets close enough to a block or entity." };
		static String proximityFuzeScale = "Scale of the area covered by the Proximity Fuze. Larger number means wider area covered";
		static String[] proximityFuzeSpacing = new String[] {
				"[in Blocks]",
				"Spacing of the detection points of the Proximity Fuze."
		};
		static String fluidBlobBlockAffectChance = "The chance of a fluid blob affecting a block in its area of effect (AOE). 0 is 0% (never), 1 is 100% (always).";
		static String allAutocannonProjectilesAreTracers = "Makes all shot autocannon projectiles tracers regardless if the item had a tracer tip applied. Emulates legacy behavior.";
		static String allBigCannonProjectilesAreTracers = "Makes all shot big cannon projectiles tracers regardless if the item had a tracer tip applied.";
		static String ammoContainerAutocannonRoundCapacity = "How many autocannon rounds the Autocannon Ammo Container can store.";
		static String ammoContainerMachineGunRoundCapacity = "How many machine gun rounds the Autocannon Ammo Container can store.";
		static String minVelocityForPenetrationBonus = "The minimum velocity necessary to activate the penetration bonus.";
	}

	public enum GriefState {
		ALL_DAMAGE(ExplosionInteraction.BLOCK),
		NO_EXPLOSIVE_DAMAGE(ExplosionInteraction.NONE),
		NO_DAMAGE(ExplosionInteraction.NONE);

		private final ExplosionInteraction explosiveInteraction;

		GriefState(ExplosionInteraction explosiveInteraction) {
			this.explosiveInteraction = explosiveInteraction;
		}

		public ExplosionInteraction explosiveInteraction() { return this.explosiveInteraction; }
	}

}
