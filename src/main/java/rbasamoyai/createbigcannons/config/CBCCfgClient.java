package rbasamoyai.createbigcannons.config;

public class CBCCfgClient extends CBCConfigBase {

	public final ConfigGroup client = group(0, "client", Comments.client);
	
	public final ConfigBool showCannonPlumes = b(true, "showCannonPlumes", Comments.showCannonPlumes);
	public final ConfigInt fluidBlobParticleCount = i(20, 0, 1000, "fluidBlobParticleCount", Comments.fluidBlobParticleCount);
	
	@Override public String getName() { return "client"; }

	private static class Comments {
		static String client = "Client-side config for Create Big Cannons.";
		static String[] showCannonPlumes = new String[] {
				"Generate cannon plumes when a cannon is fired.",
				"Set to false if cannon plumes cause performance issues, as they generate many particles.",
				"However, it may be harder to detect if a projectile has squibbed in the barrel (no smoke in \"pop and no smoke\".)" };
		static String fluidBlobParticleCount = "How many particles are in a Fluid Blob of any size.";
	}
	
}
