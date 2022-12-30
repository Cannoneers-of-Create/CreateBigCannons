package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgKinetics extends ConfigBase {

	public final ConfigInt maxLoaderLength = i(64, 3, "maxLoaderLength", Comments.maxLoaderLength);
	public final CBCCfgStress stress = nested(1, CBCCfgStress::new, Comments.stress);
	
	@Override public String getName() { return "kinetics"; }
	
	private static class Comments {
		static String maxLoaderLength = "Maximum length of cannon loaders that can be built.";
		static String stress = "These values affect the stress of Create Big Cannons' mechanical blocks.";
	}

}
