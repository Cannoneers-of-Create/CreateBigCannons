package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

import net.minecraft.world.level.Explosion.BlockInteraction;

public class CBCCfgMunitions extends ConfigBase {

	public final ConfigBool invulProjectileHurt = b(false, "invulnerableAfterProjectileHurt", Comments.invulnerableAfterProjectileHurt);
	public final ConfigFloat heShellPower = f(10, 0, "heShellPower", Comments.heShellPower);
	public final ConfigFloat apShellPower = f(4, 0, "apShellPower", Comments.apShellPower);
	public final ConfigFloat mortarStonePower = f(4, 0, "mortarStonePower", Comments.mortarStonePower);
	public final ConfigFloat maxMortarStoneCharges = f(2, -1, "maximumMortarStonePowderCharges", Comments.maxMortarStoneCharges);
	public final ConfigEnum<GriefState> damageRestriction = e(GriefState.ALL_DAMAGE, "damageRestriction", Comments.damageRestriction);
	
	public final ConfigGroup fuzes = group(0, "fuzes", "Projectile Fuzes"); 
	public final ConfigFloat impactFuzeDetonationChance = f(0.67f, 0, 1, "impactFuzeDetonationChance", Comments.impactFuzeDetonationChance);
	public final ConfigInt proximityFuzeArmingTime = i(20, 0, "proximityFuzeArmingTime", Comments.proximityFuzeArmingTime);
	
	public final ConfigGroup groupedMunitions = group(0, "groupedMunitions", "Grouped Munitions");
	public final ConfigInt shrapnelCount = i(50, 1, "shrapnelCount", Comments.shrapnelCount);
	public final ConfigFloat shrapnelSpread = f(0.25f, 0.01f, "shrapnelSpread", Comments.shrapnelSpread);
	public final ConfigFloat shrapnelDamage = f(10.0f, 0.0f, "shrapnelDamage", Comments.shrapnelDamage);
	public final ConfigFloat shrapnelVulnerableBreakChance = f(0.33f, 0, 1, "shrapnelVulernableBreakChance", Comments.shrapnelVulnerableBreakChance);
	
	public final ConfigInt grapeshotCount = i(25, 1, "grapeshotCount", Comments.grapeshotCount);
	public final ConfigFloat grapeshotSpread = f(0.25f, 0.01f, "grapeshotSpread", Comments.grapeshotSpread);
	public final ConfigFloat grapeshotDamage = f(19.0f, 0.0f, "grapeshotDamage", Comments.grapeshotDamage);
	public final ConfigFloat grapeshotVulnerableBreakChance = f(0.33f, 0, 1, "grapeshotVulernableBreakChance", Comments.grapeshotVulnerableBreakChance);
	
	public final ConfigInt fluidShellCapacity = i(2000, 1, "fluidShellCapacity", Comments.fluidShellCapacity);
	public final ConfigInt mbPerFluidBlob = i(250, 125, "millibucketsPerFluidBlob", Comments.mbPerFluidBlob);
	public final ConfigFloat fluidBlobSpread = f(1f, 0.01f, "fluidBlobSpread", Comments.fluidBlobSpread);
	public final ConfigInt mbPerAoeRadius = i(125, 125, "millibucketsPerAreaOfEffectRadius", Comments.mbPerAoeRadius);
	public final ConfigFloat fluidBlobBlockAffectChance = f(0.5f, 0, 1, "fluidBlobBlockEffectChance", Comments.fluidBlobBlockAffectChance);
	
	@Override public String getName() { return "munitions"; }

	private static class Comments {
		static String invulnerableAfterProjectileHurt = "If an entity should be invulnerable for a while after being hit by a mod projectile.";
		static String heShellPower = "How powerful the High Explosive (HE) shell is. For reference, a block of TNT has an explosion power of 4.";
		static String apShellPower = "How powerful the Armor Piercing (AP) shell is. For reference, a block of TNT has an explosion power of 4.";
		static String mortarStonePower = "How powerful the Mortar Stone is. For reference, a block of TNT has an explosion power of 4.";
		static String maxMortarStoneCharges = "How many Powder Charges a Mortar Stone can handle before breaking. Set to less than 0 to make Mortar Stones unbreakable.";
		static String[] damageRestriction = new String[] { "The extent to which cannon projectiles can damage surrounding blocks.",
				"All Damage - projectiles will destroy anything they hit, if applicable. Explosive projectiles will destroy blocks on detonation.",
				"No Explosive Damage - projectiles will destroy anything they hit, if applicable. Explosive projectiles will only harm entities on detonation.",
				"No Damage - projectiles will not destroy anything they hit, and will only deal entity damage. Explosive projectiles will only harm entities on detonation."
		};
		static String impactFuzeDetonationChance = "Chance that the Impact Fuze will detonate on hitting something. 0 is 0% (never), 1 is 100% (always).";
		static String[] proximityFuzeArmingTime = new String[] { "Time it takes for a proximity fuze to arm itself in ticks.",
				"(For reference, there are 20 ticks in 1 second.)",
				"After the fuze has been in the air for the specified arming time, it will detonate when it gets close enough to a block or entity." };
		static String shrapnelCount = "Amount of shrapnel bullets that a Shrapnel Shell releases on detonation.";
		static String shrapnelSpread = "How much shrapnel bullets spread on release.";
		static String shrapnelDamage = "How much damage a shrapnel bullet does.";
		static String shrapnelVulnerableBreakChance = "Chance that blocks that may be destroyed by shrapnel will be so. 0 is 0% (never), 1 is 100% (always).";
		static String grapeshotCount = "Amount of grapeshot rounds that a Bag of Grapeshot releases.";
		static String grapeshotSpread = "How much grapeshot rounds spread on release.";
		static String grapeshotDamage = "How much damage a grapeshot round does.";
		static String grapeshotVulnerableBreakChance = "Chance that blocks that may be destroyed by grapeshot will be so. 0 is 0% (never), 1 is 100% (always).";
		static String fluidShellCapacity = "How much fluid in millibuckets (mB) a Fluid Shell can contain.";
		static String[] mbPerFluidBlob = new String[] { "The amount of fluid in millibuckets (mB) required to spawn a Fluid Blob on Fluid Shell detonation.",
				"Any remaining fluid after spawning will be rounded up to one additional projectile."
		};
		static String fluidBlobSpread = "How much Fluid Blobs spread on release.";
		static String mbPerAoeRadius = "How many millibuckets (mB) of fluid in a Fluid Blob are required to increase its area of effect (AOE) by one block in each direction.";
		static String fluidBlobBlockAffectChance = "The chance of a fluid blob affecting a block in its area of effect (AOE). 0 is 0% (never), 1 is 100% (always).";
	}
	
	public enum GriefState {
		ALL_DAMAGE(BlockInteraction.DESTROY),
		NO_EXPLOSIVE_DAMAGE(BlockInteraction.NONE),
		NO_DAMAGE(BlockInteraction.NONE);
		
		private final BlockInteraction explosiveInteraction;
		
		private GriefState(BlockInteraction explosiveInteraction) {
			this.explosiveInteraction = explosiveInteraction;
		}
		
		public BlockInteraction explosiveInteraction() { return this.explosiveInteraction; }
	}
	
}
