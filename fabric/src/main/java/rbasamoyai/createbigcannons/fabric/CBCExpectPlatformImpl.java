package rbasamoyai.createbigcannons.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class CBCExpectPlatformImpl {
	public static String platformName() {
		return FabricLoader.getInstance().isModLoaded("quilt_loader") ? "Quilt" : "Fabric";
	}
}
