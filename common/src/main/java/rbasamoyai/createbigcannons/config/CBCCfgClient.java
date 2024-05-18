package rbasamoyai.createbigcannons.config;

public class CBCCfgClient extends CBCConfigBase {

	public final ConfigGroup client = group(0, "client", Comments.client);

	public final ConfigBool showAutocannonPlumes = b(true, "showAutocannonPlumes");
	public final ConfigBool showBigCannonPlumes = b(true, "showBigCannonPlumes");
	public final ConfigBool showDropMortarPlumes = b(true, "showDropMortarPlumes");
	public final ConfigInt fluidBlobParticleCount = i(20, 0, 1000, "fluidBlobParticleCount", Comments.fluidBlobParticleCount);

	@Override public String getName() { return "client"; }

	private static class Comments {
		static String client = "Client-side config for Create Big Cannons.";
		static String fluidBlobParticleCount = "How many particles are in a Fluid Blob of any size.";
	}

}
