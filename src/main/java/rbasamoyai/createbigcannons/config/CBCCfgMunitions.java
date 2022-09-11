package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgMunitions extends ConfigBase {

	public final ConfigBool invulProjectileHurt = b(false, "invulnerableAfterProjectileHurt", Comments.invulnerableAfterProjectileHurt);
	public final ConfigFloat heShellPower = f(10, 0, "heShellPower", Comments.heShellPower);
	
	public final ConfigGroup fuzes = group(0, "fuzes", "Projectile Fuzes"); 
	public final ConfigFloat impactFuzeDetonationChance = f(0.67f, 0, 1, "impactFuzeDetonationChance", Comments.impactFuzeDetonationChance);
	
	public final ConfigGroup groupedMunitions = group(0, "groupedMunitions", "Grouped Munitions");
	public final ConfigInt shrapnelCount = i(50, 1, "shrapnelCount", Comments.shrapnelCount);
	public final ConfigFloat shrapnelSpread = f(0.25f, 0.01f, "shrapnelSpread", Comments.shrapnelSpread);
	public final ConfigFloat shrapnelDamage = f(10.0f, 0.0f, "shrapnelDamage", Comments.shrapnelDamage);
	
	public final ConfigInt grapeshotCount = i(25, 1, "grapeshotCount", Comments.grapeshotCount);
	public final ConfigFloat grapeshotSpread = f(0.25f, 0.01f, "grapeshotSpread", Comments.grapeshotSpread);
	public final ConfigFloat grapeshotDamage = f(19.0f, 0.0f, "grapeshotDamage", Comments.grapeshotDamage);
	
	@Override public String getName() { return "munitions"; }

	private static class Comments {
		static String invulnerableAfterProjectileHurt = "If an entity should be invulnerable for a while after being hit by a mod projectile.";
		static String heShellPower = "How powerful the High Explosive (HE) shell is. For reference, a block of TNT has an explosion power of 4.";
		static String impactFuzeDetonationChance = "Chance that the Impact Fuze will detonate on hitting something. 0 is 0%, 1 is 100%.";
		static String shrapnelCount = "Amount of shrapnel bullets that a Shrapnel Shell releases on detonation.";
		static String shrapnelSpread = "How much shrapnel bullets spread on release.";
		static String shrapnelDamage = "How much damage a shrapnel bullet does.";
		static String grapeshotCount = "Amount of grapeshot rounds that a Bag of Grapeshot releases.";
		static String grapeshotSpread = "How much grapeshot rounds spread on release.";
		static String grapeshotDamage = "How much damage a grapeshot round does.";
	}
	
}
