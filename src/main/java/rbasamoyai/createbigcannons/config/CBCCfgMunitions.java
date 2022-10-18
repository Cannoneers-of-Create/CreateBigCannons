package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

import net.minecraft.world.level.Explosion.BlockInteraction;

public class CBCCfgMunitions extends ConfigBase {

	public final ConfigBool invulProjectileHurt = b(false, "invulnerableAfterProjectileHurt", Comments.invulnerableAfterProjectileHurt);
	public final ConfigFloat heShellPower = f(10, 0, "heShellPower", Comments.heShellPower);
	public final ConfigFloat apShellPower = f(5, 0, "apShellPower", Comments.apShellPower);
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
	
	@Override public String getName() { return "munitions"; }

	private static class Comments {
		static String invulnerableAfterProjectileHurt = "If an entity should be invulnerable for a while after being hit by a mod projectile.";
		static String heShellPower = "How powerful the High Explosive (HE) shell is. For reference, a block of TNT has an explosion power of 4.";
		static String apShellPower = "How powerful the Armor Piercing (AP) shell is. For reference, a block of TNT has an explosion power of 4.";
		static String[] damageRestriction = new String[] { "The extent to which cannon projectiles can damage surrounding blocks.",
				"ALL_DAMAGE - projectiles will destroy anything they hit, if applicable. Explosive projectiles will destroy blocks on detonation.",
				"NO_EXPLOSIVE_DAMAGE - projectiles will destroy anything they hit, if applicable. Explosive projectiles will only harm entities on detonation.",
				"NO_DAMAGE - projectiles will not destroy anything they hit, and will only deal entity damage. Explosive projectiles will only harm entities on detonation."
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
