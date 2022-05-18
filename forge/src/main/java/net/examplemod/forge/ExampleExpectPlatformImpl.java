package net.examplemod.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ExampleExpectPlatformImpl {
	public static Path configDir() {
		return FMLPaths.CONFIGDIR.get();
	}
}
