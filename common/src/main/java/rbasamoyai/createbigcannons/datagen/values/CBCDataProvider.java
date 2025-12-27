package rbasamoyai.createbigcannons.datagen.values;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class CBCDataProvider implements DataProvider {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	protected final String modid;
	protected final String folder;
	private final PackOutput output;

	protected CBCDataProvider(String modid, PackOutput output, String folder) {
		this.modid = modid;
		this.output = output;
		this.folder = folder;
	}

	@Override
	public final CompletableFuture<?> run(CachedOutput cache) {
		Map<ResourceLocation, JsonObject> dataToWrite = new LinkedHashMap<>();
		this.generateData(dataToWrite::put);

		return CompletableFuture.allOf(dataToWrite.entrySet().stream()
			.map(entry -> {
				ResourceLocation loc = entry.getKey();
				Path path = this.output.getOutputFolder()
					.resolve("data/" + loc.getNamespace() + "/" + this.folder + "/" + loc.getPath() + ".json");
				String s = GSON.toJson(entry.getValue());
				return DataProvider.saveStable(cache, entry.getValue(), path);
			})
			.toArray(i -> new CompletableFuture[i]));
	}

	protected abstract void generateData(BiConsumer<ResourceLocation, JsonObject> cons);

}
