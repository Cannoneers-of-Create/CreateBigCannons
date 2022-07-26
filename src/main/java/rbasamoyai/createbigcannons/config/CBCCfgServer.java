package rbasamoyai.createbigcannons.config;

public class CBCCfgServer extends CBCConfigBase {

	public final ConfigGroup server = group(0, "server", Comments.server);
	
	public final CBCCfgFailure failure = nested(0, CBCCfgFailure::new, Comments.failure);
	public final CBCCfgMunitions munitions = nested(0, CBCCfgMunitions::new, Comments.munitions);
	
	@Override public String getName() { return "server"; }

	private static class Comments {
		static String[] server = new String[] {
				"Server-side config for Create Big Cannons.",
				"",
				"Various terms used in this config file:",
				"Squib ratio -> The ratio between the powder charges fired and the barrel length travelled.",
				"  The barrel which the fired projectile was contained in is counted towards the travelled length.",
				"  If the squib ratio for a cannon material is exceeded, the projectile may get stuck, causing",
				"  catastrophic failure if collided into on a following shot. The cannon does not emit a plume", 
				"  and \"pops\" instead of \"booming\", or \"pop and no smoke\"." };
		static String failure = "These values affect the extent of cannon failure.";
		static String munitions = "These values affect the characteristics of cannon munitions.";
	}
	
}
