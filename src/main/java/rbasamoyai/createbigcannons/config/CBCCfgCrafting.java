package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgCrafting extends ConfigBase {

	public final ConfigInt maxCannonCastHeight = i(32, 1, "maxCannonCastHeight", Comments.maxCannonCastHeight);
	public final ConfigInt maxCannonDrillLength = i(32, 1, "maxCannonDrillLength", Comments.maxCannonDrillLength);
	
	@Override public String getName() { return "crafting"; }
	
	private static class Comments {
		static String maxCannonCastHeight = "Maximum height of a single cannon cast that can be built.";
		static String maxCannonDrillLength = "Maximum length of a cannon drill that can be built.";
	}

}
