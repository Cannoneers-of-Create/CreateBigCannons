package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgCrafting extends ConfigBase {

	public final ConfigInt maxCannonCastHeight = i(32, 1, "maxCannonCastHeight", Comments.maxCannonCastHeight);
	public final ConfigInt maxCannonDrillLength = i(32, 1, "maxCannonDrillLength", Comments.maxCannonDrillLength);
	public final ConfigInt maxCannonBuilderLength = i(32, 1, "maxCannonBuilderLength", Comments.maxCannonBuilderLength);
	public final ConfigInt maxCannonBuilderRange = i(32, 2, "maxCannonBuilderRange", Comments.maxCannonBuilderRange);
	public final ConfigInt builtUpCannonHeatingTime = i(6000, 0, "builtUpCannonHeatingTime", Comments.builtUpCannonHeatingTime);
	
	@Override public String getName() { return "crafting"; }
	
	private static class Comments {
		static String maxCannonCastHeight = "Maximum height of a single cannon cast that can be built.";
		static String maxCannonDrillLength = "Maximum length of a Cannon Drill that can be built.";
		static String maxCannonBuilderLength = "Maximum length of a Cannon Builder that can be built.";
		static String maxCannonBuilderRange = "Maximum reach of a Cannon Builder.";
		static String builtUpCannonHeatingTime = "Time a built-up cannon block needs to be heated for until it transforms into its finished form.";
	}

}
