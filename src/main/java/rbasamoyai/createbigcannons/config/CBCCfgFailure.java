package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgFailure extends ConfigBase {

	public final ConfigBool disableAllFailure = b(false, "disableAllCannonFailure", Comments.disableAllFailure);
	public final ConfigFloat squibChance = f(0.25f, 0.0f, 1.0f, "squibChance", Comments.squibChance);
	public final ConfigFloat barrelChargeBurstChance = f(0.2f, 0.0f, 1.0f, "barrelChargeBurstChance", Comments.barrelChargeBurstChance);
	public final ConfigFloat failureExplosionPower = f(2, 0, "failureExplosionPower", Comments.failureExplosionPower);
	public final ConfigFloat overloadBurstChance = f(0.5f, 0.0f, 1.0f, "overloadBurstChance", Comments.overloadBurstChance);
	public final ConfigFloat interruptedIgnitionChance = f(0.33f, 0.0f, 1.0f, "interruptedIgnitionChance", Comments.interruptedIgnitionChance);
	
	@Override public String getName() { return "failure"; }
	
	private static class Comments {
		static String disableAllFailure = "If true, cannons cannot fail whatsoever. Equivalent to setting all the chances below to zero.";
		static String squibChance = "Chance that a fired projectile will get stuck in a barrel after exceeding the squib ratio. 0 is 0%, 1 is 100%.";
		static String[] barrelChargeBurstChance = new String[] {
				"Chance that a cannon will fail if a Powder Charge is ignited in a \"barrel\"-type cannon block. 0 is 0%, 1 is 100%.",
				"This chance can be affected by stronger and weaker charge blocks." };
		static String failureExplosionPower = "How strong the explosion of a catastrophic failure is. Scaled by the amount of charges used in the shot.";
		static String overloadBurstChance = "Chance that a cannon loaded with more Powder Charges that it can handle will fail. 0 is 0%, 1 is 100%.";
		static String interruptedIgnitionChance = "Chance that a load with gaps between the loaded Powder Charges will not completely combust. 0 is 0%, 1 is 100%.";
	}

}
