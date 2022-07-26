package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgCannons extends ConfigBase {

	public final ConfigInt maxCannonLength = i(32, 3, "maxCannonLength", Comments.maxCannonLength);
	
	public CBCCfgCannons() {
		super();
	}
	
	@Override public String getName() { return "cannons"; }
	
	private static class Comments {
		static String maxCannonLength = "Maximum length of cannons that can be built.";
	}

}
