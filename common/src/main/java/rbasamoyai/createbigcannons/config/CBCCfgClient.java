package rbasamoyai.createbigcannons.config;

public class CBCCfgClient extends CBCConfigBase {

	public final ConfigGroup client = group(0, "client", Comments.client);

	public final ConfigBool showAutocannonPlumes = b(true, "showAutocannonPlumes");
	public final ConfigBool showDropMortarPlumes = b(true, "showDropMortarPlumes");
	public final ConfigInt fluidBlobParticleCount = i(20, 0, 1000, "fluidBlobParticleCount", Comments.fluidBlobParticleCount);
	public final ConfigInt cannonMountAngleGoggleTooltipPrecision = i(2, 0, 4, "cannonMountAngleGoggleTooltipPrecision", Comments.cannonMountAngleTooltipPrecision);

	public final ConfigGroup screenShake = group(1, "screenShake", "Screen Shake");
	public final ConfigFloat cannonScreenShakeIntensity = f(1, 0, 2, "cannonScreenShakeIntensity");

	public final ConfigGroup bigCannonPlumes = group(1, "bigCannonPlumes", "Big Cannon Plumes");
	public final ConfigBool showBigCannonPlumes = b(true, "showBigCannonPlumes");
	public final ConfigBool showExtraBigCannonSmoke = b(true, "showExtraSmoke");
	public final ConfigBool showExtraBigCannonFlames = b(true, "showExtraFlames");

	@Override public String getName() { return "client"; }

	private static class Comments {
		static String client = "Client-side config for Create Big Cannons.";
		static String fluidBlobParticleCount = "How many particles are in a Fluid Blob of any size.";
		static String cannonMountAngleTooltipPrecision = "How many digits are after the angle decimal point on a cannon mount goggle tooltip.";
	}

}
