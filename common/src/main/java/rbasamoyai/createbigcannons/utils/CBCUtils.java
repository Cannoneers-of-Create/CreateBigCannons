package rbasamoyai.createbigcannons.utils;

import net.minecraft.resources.ResourceLocation;

public class CBCUtils {

	/**
	 * Alias method for easier porting to 1.21+.
	 *
	 * @param namespace the namespace
	 * @param path the path
	 * @return Resource location of form {@code <namespace>:<path>}
	 */
	public static ResourceLocation location(String namespace, String path) {
		return new ResourceLocation(namespace, path);
	}

	/**
	 * Alias method for easier porting to 1.21+.
	 *
	 * @param location String resource location of form {@code "<namespace>:<path>"}
	 * @return Resource location of form {@code <namespace>:<path>}
	 */
	public static ResourceLocation location(String location) {
		return new ResourceLocation(location);
	}

	private CBCUtils() {}

}
