package rbasamoyai.createbigcannons.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

/**
 * Copied from SimpleJsonResourceReloadListener, with some tweaks to support Multimap; using similar to tag loading
 */
public abstract class CBCJsonResourceReloadListener extends SimplePreparableReloadListener<Multimap<ResourceLocation, JsonElement>> {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final int PATH_SUFFIX_LENGTH = ".json".length();
	private final Gson gson;
	private final String directory;

	public CBCJsonResourceReloadListener(Gson gson, String directory) {
		this.gson = gson;
		this.directory = directory;
	}

	@Override
	protected Multimap<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
		Multimap<ResourceLocation, JsonElement> map = HashMultimap.create();
		int i = this.directory.length() + 1;

		for(ResourceLocation loco : resourceManager.listResources(this.directory, path -> path.endsWith(".json"))) {
			String string = loco.getPath();
			ResourceLocation loc2 = new ResourceLocation(loco.getNamespace(), string.substring(i, string.length() - PATH_SUFFIX_LENGTH));
			try {
				Resource resource = resourceManager.getResource(loco);
				try {
					InputStream inputStream = resource.getInputStream();
					try {
						Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
						try {
							JsonElement jsonElement = GsonHelper.fromJson(this.gson, reader, JsonElement.class);
							if (jsonElement != null) {
								map.put(loc2, jsonElement);
							} else {
								LOGGER.error("Couldn't load data file {} from {} as it's null or empty", loc2, loco);
							}
						} catch (Throwable var17) {
							try {
								reader.close();
							} catch (Throwable var16) {
								var17.addSuppressed(var16);
							}
							throw var17;
						}
						reader.close();
					} catch (Throwable var18) {
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (Throwable var15) {
								var18.addSuppressed(var15);
							}
						}
						throw var18;
					}
					if (inputStream != null) inputStream.close();
				} catch (Throwable var19) {
					if (resource != null) {
						try {
							resource.close();
						} catch (Throwable var14) {
							var19.addSuppressed(var14);
						}
					}
					throw var19;
				}
				if (resource != null) resource.close();
			} catch (IllegalArgumentException | IOException | JsonParseException var20) {
				LOGGER.error("Couldn't parse data file {} from {}", loc2, loco, var20);
			}
		}
		return map;
	}

}
