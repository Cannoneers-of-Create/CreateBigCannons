package rbasamoyai.createbigcannons.datagen.values;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

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
	public void run(CachedOutput cache) throws IOException {
		this.generateData();

		JsonObject obj = new JsonObject();
		this.write(obj);

		Path path = this.gen.getOutputFolder().resolve("data/" + this.modid + "/" + this.folder + "/" + this.name + ".json");
//		String s1 = SHA1.hashUnencodedChars(s);
//		if (!Objects.equals(SHA1.hashUnencodedChars(path), s1) || !Files.exists(path)) {
//			Files.createDirectories(path.getParent());
//			BufferedWriter writer = Files.newBufferedWriter(path);
//
//			try {
//				writer.write(s);
//			} catch (Throwable throwable) {
//				if (writer != null) {
//					try {
//						writer.close();
//					} catch (Throwable throwable1) {
//						throwable.addSuppressed(throwable1);
//					}
//				}
//				throw throwable;
//			}
//
//			if (writer != null) {
//				writer.close();
//			}
//		}
//		cache.writeIfNeeded(path, s1);
	}

	protected abstract void generateData();

	protected abstract void write(JsonObject obj);

}
