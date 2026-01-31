package rbasamoyai.createbigcannons.config;

public class CBCCfgCommon extends CBCConfigBase {

	public final ConfigGroup common = group(0, "common", Comments.common);
	
	@Override public String getName() { return "common"; }

	private static class Comments {
		static String common = "Common config file for Create Big Cannons.";
	}
	
}
