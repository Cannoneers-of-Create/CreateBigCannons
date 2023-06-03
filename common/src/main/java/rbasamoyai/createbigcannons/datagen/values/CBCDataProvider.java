package rbasamoyai.createbigcannons.datagen.values;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.slf4j.Logger;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

public abstract class CBCDataProvider implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	private final DataGenerator gen;
	protected final String modid;
	protected final String name;
	protected final String folder;

	protected CBCDataProvider(String modid, String name, DataGenerator gen, String folder) {
		this.modid = modid;
		this.name = name;
		this.gen = gen;
		this.folder = folder;
	}

	protected CBCDataProvider(String modid, DataGenerator gen, String folder) {
		this(modid, "default", gen, folder);
	}

	@Override
	public final void run(CachedOutput cache) throws IOException {
		this.generateData();

		JsonObject obj = new JsonObject();
		this.write(obj);

		Path path = this.gen.getOutputFolder().resolve("data/" + this.modid + "/" + this.folder + "/" + this.name + ".json");
		String s = GSON.toJson(obj);
		if (!Objects.equals(cache.hashCode(), Hashing.sha1().hashBytes(s.getBytes(StandardCharsets.UTF_8))) || !Files.exists(path)) {
			Files.createDirectories(path.getParent());
			if (!Files.exists(path.toAbsolutePath())) Files.createFile(path.toAbsolutePath());
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
		}
		cache.writeIfNeeded(path, s.getBytes(StandardCharsets.UTF_8), Hashing.sha1().hashBytes(s.getBytes(StandardCharsets.UTF_8)));

	}

	protected abstract void generateData();

	protected abstract void write(JsonObject obj);

}
