package rbasamoyai.createbigcannons.config;

import rbasamoyai.createbigcannons.cannonmount.CannonPlumeParticle.PlumeSetting;

public class CBCCfgClient extends CBCConfigBase {

	public final ConfigGroup client = group(0, "client", Comments.client);
	
	public final ConfigEnum<PlumeSetting> showCannonPlumes = e(PlumeSetting.DEFAULT, "showCannonPlumes", Comments.showCannonPlumes);
	public final ConfigInt fluidBlobParticleCount = i(20, 0, 1000, "fluidBlobParticleCount", Comments.fluidBlobParticleCount);
	
	@Override public String getName() { return "client"; }

	private static class Comments {
		static String client = "Client-side config for Create Big Cannons.";
		static String[] showCannonPlumes = new String[] { "How cannon plumes display when a cannon is fired",
				"Off - No particles are spawned when firing a cannon. Least laggy option, but it may be harder to detect if a projectile has squibbed in the barrel (no smoke in \"pop and no smoke\".)",
				"Legacy - Cannons spawn vanilla particles (mostly campfire smoke) when firing. The classic look, but spawns many particles and may cause high lag.",
				"Default - Cannons spawn cannon smoke particles, designed to give a grand impression while keeping the spawned particle count low compared to Legacy."
		};
		static String fluidBlobParticleCount = "How many particles are in a Fluid Blob of any size.";
	}
	
}
