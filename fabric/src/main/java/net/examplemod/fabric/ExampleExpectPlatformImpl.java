package net.examplemod.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ExampleExpectPlatformImpl {
	public static Path configDir() {
		return FabricLoader.getInstance().getConfigDir();
	}
}
