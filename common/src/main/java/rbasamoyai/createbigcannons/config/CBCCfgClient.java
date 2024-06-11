package rbasamoyai.createbigcannons.config;

public class CBCCfgClient extends CBCConfigBase {

	public final ConfigGroup client = group(0, "client", Comments.client);

	public final ConfigBool showAutocannonPlumes = b(true, "showAutocannonPlumes");
	public final ConfigBool showDropMortarPlumes = b(true, "showDropMortarPlumes");
	public final ConfigInt fluidBlobParticleCount = i(20, 0, 1000, "fluidBlobParticleCount", Comments.fluidBlobParticleCount);

	public final ConfigGroup cannonMountGoggleTooltip = group(1, "cannonMountGoggleTooltip", "Cannon Mount Goggle Tooltip");
	public final ConfigInt cannonMountAngleGoggleTooltipPrecision = i(2, 0, 4, "anglePrecision", Comments.cannonMountAngleTooltipPrecision);
	public final ConfigBool use180180RangeForYaw = b(false, "use180_180RangeForYaw", Comments.use180180RangeForYaw);

	public final ConfigGroup screenShake = group(1, "screenShake", "Screen Shake");
	public final ConfigFloat cannonScreenShakeIntensity = f(1.3f, 0, 2, "cannonScreenShakeIntensity");
	public final ConfigFloat cannonScreenShakeSpringiness = f(0.08f, 0.01f, 2f, "cannonScreenShakeSpringiness");
	public final ConfigFloat cannonScreenShakeDecay = f(0.3f, 0.01f, 2f, "cannonScreenShakeDecay");

	public final ConfigGroup bigCannonPlumes = group(1, "bigCannonPlumes", "Big Cannon Plumes");
	public final ConfigBool showBigCannonPlumes = b(true, "showBigCannonPlumes");
	public final ConfigBool showExtraBigCannonSmoke = b(true, "showExtraSmoke");
	public final ConfigBool showExtraBigCannonFlames = b(true, "showExtraFlames");

	public final ConfigGroup flakClouds = group(1, "flakClouds", "Flak Clouds");
	public final ConfigBool showFlakClouds = b(true, "showFlakClouds");
	public final ConfigBool showExtraFlakCloudFlames = b(true, "showExtraFlames");
	public final ConfigBool showExtraFlakCloudShockwave = b(true, "showExtraShockwave");
	public final ConfigBool showExtraFlakTrails = b(true, "showExtraTrails");

	public final ConfigGroup shrapnelClouds = group(1, "shrapnelClouds", "Shrapnel Clouds");
	public final ConfigBool showShrapnelClouds = b(true, "showShrapnelClouds");
	public final ConfigBool showExtraShrapnelCloudFlames = b(true, "showExtraFlames");
	public final ConfigBool showExtraShrapnelCloudShockwave = b(true, "showExtraShockwave");

	public final ConfigGroup fluidShellClouds = group(1, "fluidShellClouds", "Fluid Shell Clouds");
	public final ConfigBool showFluidShellClouds = b(true, "showFluidShellClouds");
	public final ConfigBool showExtraFluidShellCloudFlames = b(true, "showExtraFlames");
	public final ConfigBool showExtraFluidShellCloudShockwave = b(true, "showExtraShockwave");

	@Override public String getName() { return "client"; }

	private static class Comments {
		static String client = "Client-side config for Create Big Cannons.";
		static String fluidBlobParticleCount = "How many particles are in a Fluid Blob of any size.";
		static String cannonMountAngleTooltipPrecision = "How many digits are after the angle decimal point on a cannon mount goggle tooltip.";
		static String use180180RangeForYaw = "If true, the yaw angle on goggles ranges from +180 to -180\u00ba. If false, it ranges from 0 to +360\u00ba.";
	}

}
