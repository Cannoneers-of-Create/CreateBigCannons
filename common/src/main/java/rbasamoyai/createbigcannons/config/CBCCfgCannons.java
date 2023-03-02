package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgCannons extends ConfigBase {

	public final ConfigInt maxCannonLength = i(64, 3, "maxCannonLength", Comments.maxCannonLength);
	public final ConfigFloat powderChargeSpread = f(2.0f, 0.0f, "powderChargeSpread", Comments.powderChargeSpread);
	public final ConfigFloat barrelSpreadReduction = f(1.0f, 0.0f, "barrelSpreadReduction", Comments.barrelSpreadReduction);
	public final ConfigInt weakBreechStrength = i(4, -1, "slidingBreechStrength", Comments.weakBreechStrength);

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
	
	public CBCCfgCannons() {
		super();
	}
	
	@Override public String getName() { return "cannons"; }
	
	private static class Comments {
		static String maxCannonLength = "Maximum length of cannons that can be built.";
		static String powderChargeSpread = "How much each Powder Charges used affects the spread of a fired projectile.";
		static String barrelSpreadReduction = "How much each cannon barrel reduces the spread of a fired projectile passing through.";
		static String weakBreechStrength = "Maximum amount of Powder Charges that weak breech blocks can safely handle. Set to -1 to disable this behavior.";
		static String deployersCanUseLoadingTools = "If deployers can use loading tools.";
		static String ramRodReach = "How many blocks inside a cannon a Ram Rod can reach.";
		static String ramRodStrength = "Maximum amount of munition blocks a Ram Rod can push.";
		static String wormReach = "How many blocks inside a cannon a Worm can reach.";
		static String loadingToolHungerConsumption = "How many hunger/saturation points it takes to move one munition block a distance of one block using loading tools.";
		static String loadingToolCooldown = "How many ticks before a player can use a manual loading tool again. There are 20 ticks in 1 second.";
		static String carriageSpeed = "How fast the carriage is, in blocks per tick.";
		static String carriageTurnRate = "How fast the carriage turns, in degrees per tick.";
	}

}
