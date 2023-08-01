package rbasamoyai.createbigcannons.datagen.values;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.slf4j.Logger;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

public abstract class CBCDataProvider implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	protected final String modid;
	protected final String folder;
	private final DataGenerator gen;

	protected CBCDataProvider(String modid, DataGenerator gen, String folder) {
		this.modid = modid;
		this.gen = gen;
		this.folder = folder;
	}

	@Override
	public final void run(CachedOutput cache) throws IOException {
		Map<ResourceLocation, JsonObject> dataToWrite = new LinkedHashMap<>();
		this.generateData(dataToWrite::put);

		for (Map.Entry<ResourceLocation, JsonObject> entry : dataToWrite.entrySet()) {
			ResourceLocation loc = entry.getKey();
			Path path = this.gen.getOutputFolder()
				.resolve("data/" + loc.getNamespace() + "/" + this.folder + "/" + loc.getPath() + ".json");
			String s = GSON.toJson(entry.getValue());
			if (!Files.exists(path.toAbsolutePath())) {
				Files.createFile(path.toAbsolutePath());
			}
			Files.createDirectories(path.getParent());
			BufferedWriter writer = Files.newBufferedWriter(path);

			try {
				writer.write(s);
			} catch (Throwable throwable) {
				if (writer != null) {
					try {
						writer.close();
					} catch (Throwable throwable1) {
						throwable.addSuppressed(throwable1);
					}
				}
				throw throwable;
			}

			if (writer != null) {
				writer.close();
			}
			cache.writeIfNeeded(path, s.getBytes(StandardCharsets.UTF_8), Hashing.sha1().hashBytes(s.getBytes(
				StandardCharsets.UTF_8)));
		}
	}

	protected abstract void generateData(BiConsumer<ResourceLocation, JsonObject> cons);

}
