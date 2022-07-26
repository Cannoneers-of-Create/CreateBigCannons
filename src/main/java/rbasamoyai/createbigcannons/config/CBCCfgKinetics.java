package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgKinetics extends ConfigBase {

	public final ConfigInt maxLoaderLength = i(32, 3, "maxLoaderLength", Comments.maxLoaderLength);
	
	@Override public String getName() { return "kinetics"; }
	
	private static class Comments {
		static String maxLoaderLength = "Maximum length of cannon loaders that can be built.";
	}

}
