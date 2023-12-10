package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgCannons extends ConfigBase {

	public final ConfigInt maxCannonLength = i(64, 3, "maxCannonLength", Comments.maxCannonLength);
	public final ConfigFloat barrelSpreadReduction = f(1.0f, 0.0f, "barrelSpreadReduction", Comments.barrelSpreadReduction);
	public final ConfigInt quickfiringBreechLoadingCooldown = i(40, 0, "quickfiringBreechLoadingCooldown", Comments.quickfiringBreechLoadingCooldown);
	public final ConfigInt quickfiringBreechOpeningCooldown = i(5, 0, "quickfiringBreechOpeningCooldown", Comments.quickfiringBreechOpeningCooldown);
	public final ConfigFloat bigCannonRecoilScale = f(4f, 0, "bigCannonRecoilScale");
	public final ConfigFloat autocannonRecoilScale = f(0.5f, 0, "autocannonRecoilScale");

	public final ConfigGroup dropMortar = group(0, "dropMortar", "Drop Mortar");
	public final ConfigInt dropMortarDelay = i(5, 0, "dropMortarDelay", Comments.dropMortarDelay);
	public final ConfigInt dropMortarItemCooldown = i(40, 0, "dropMortarItemCooldown", Comments.dropMortarItemCooldown);

	public final ConfigGroup loadingTools = group(0, "loadingTools", "Loading Tools");
	public final ConfigBool deployersCanUseLoadingTools = b(false, "deployersCanUseLoadingTools", Comments.deployersCanUseLoadingTools);
	public final ConfigInt ramRodReach = i(5, 1, "ramRodReach", Comments.ramRodReach);
	public final ConfigInt ramRodStrength = i(3, 1, "ramRodStrength", Comments.ramRodStrength);
	public final ConfigInt wormReach = i(5, 1, "wormReach", Comments.wormReach);
	public final ConfigFloat loadingToolHungerConsumption = f(2.5f, 0, "loadingToolHungerConsumption", Comments.loadingToolHungerConsumption);
	public final ConfigInt loadingToolCooldown = i(20, 0, "loadingToolCooldown", Comments.loadingToolCooldown);

	public final ConfigGroup carriage = group(0, "carriage", "Cannon Carriages");
	public final ConfigFloat carriageSpeed = f(0.04f, 0.04f, 1.0f, "carriageSpeed", Comments.carriageSpeed);
	public final ConfigFloat carriageTurnRate = f(1f, 0.1f, 10f, "carriageTurnRate", Comments.carriageTurnRate);
	public final ConfigBool cannonWeightAffectsCarriageSpeed = b(true, "cannonWeightAffectsCarriageSpeed");

	public final ConfigGroup displayLink = group(0, "displayLink", "Display Link Info");
	public final ConfigBool shouldDisplayCannonRotation = b(true, "shouldDisplayCannonRotation");
	public final ConfigBool shouldDisplayContainedMunitions = b(true, "shouldDisplayContainedMunitions");

	public CBCCfgCannons() {
		super();
	}

	@Override public String getName() { return "cannons"; }

	private static class Comments {
		static String maxCannonLength = "Maximum length of cannons that can be built.";
		static String barrelSpreadReduction = "How much each cannon barrel reduces the spread of a fired projectile passing through.";
		static String deployersCanUseLoadingTools = "If deployers can use loading tools.";
		static String ramRodReach = "How many blocks inside a cannon a Ram Rod can reach.";
		static String ramRodStrength = "Maximum amount of munition blocks a Ram Rod can push.";
		static String wormReach = "How many blocks inside a cannon a Worm can reach.";
		static String loadingToolHungerConsumption = "How many hunger/saturation points it takes to move one munition block a distance of one block using loading tools.";
		static String loadingToolCooldown = "How many ticks before a player can use a manual loading tool again. There are 20 ticks in 1 second.";
		static String carriageSpeed = "How fast the carriage is, in blocks per tick.";
		static String carriageTurnRate = "How fast the carriage turns, in degrees per tick.";
		static String quickfiringBreechLoadingCooldown = "Time when the Quickfiring Breech cannot be loaded by Mechanical Arms.";
		static String quickfiringBreechOpeningCooldown = "Time it takes for the Quickfiring Breech to fully open/close, during which it cannot be loaded.";
		static String dropMortarDelay = "Time in ticks between inserting a munition into a drop mortar and the drop mortar firing it. There are 20 ticks in 1 second.";
		static String dropMortarItemCooldown = "Length in ticks that the player has to wait for before inserting the same munition type into a drop mortar again. There are 20 ticks in 1 second.";
	}

}
