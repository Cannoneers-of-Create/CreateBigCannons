package rbasamoyai.createbigcannons.index;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import rbasamoyai.createbigcannons.CreateBigCannons;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.simibubi.create.AllSoundEvents.SoundEntry;
import static com.simibubi.create.AllSoundEvents.SoundEntryBuilder;

public class CBCSoundEvents {

	public static final Map<ResourceLocation, SoundEntry> ALL = new HashMap<>();

	public static final SoundEntry
		FIRE_BIG_CANNON = create("fire_big_cannon").subtitle("Big cannon fires")
				.playExisting(SoundEvents.GENERIC_EXPLODE, 20.0f, 0.0f)
				.playExisting(SoundEvents.GENERIC_EXPLODE, 20.0f, 0.02f)
				.playExisting(SoundEvents.GENERIC_EXPLODE, 20.0f, 0.04f)
				.category(SoundSource.BLOCKS)
				.build(),

		FIRE_AUTOCANNON = create("fire_autocannon").subtitle("Autocannon fires")
				.playExisting(SoundEvents.GENERIC_EXPLODE, 4.0f, 2.0f)
				.category(SoundSource.BLOCKS)
				.build(),

		PLACE_AUTOCANNON_AMMO_CONTAINER = create("place_autocannon_ammo_container").noSubtitle()
				.playExisting(SoundEvents.ARMOR_EQUIP_IRON, 0.25f, 1.0f)
				.category(SoundSource.BLOCKS)
				.build();

	private static SoundEntryBuilder create(String id) {
		return new CBCSoundEntryBuilder(CreateBigCannons.resource(id));
	}

	public static void prepare() {
		for (SoundEntry entry : ALL.values())
			entry.prepare();
	}

	public static void register(Consumer<SoundEntry> cons) {
		for (SoundEntry entry : ALL.values())
			cons.accept(entry);
	}

	public static void registerLangEntries() {
		for (SoundEntry entry : ALL.values()) {
			if (entry.hasSubtitle())
				CreateBigCannons.REGISTRATE.addRawLang(entry.getSubtitleKey(), entry.getSubtitle());
		}
	}

	public static class CBCSoundEntryBuilder extends SoundEntryBuilder {
		public CBCSoundEntryBuilder(ResourceLocation id) {
			super(id);
		}

		@Override
		public SoundEntry build() {
			SoundEntry entry = super.build();
			ALL.put(entry.getId(), entry);
			return entry;
		}
	}

	public static SoundEntryProvider provider(DataGenerator generator) {
		return new SoundEntryProvider(generator);
	}

	private static class SoundEntryProvider implements DataProvider {
		private DataGenerator generator;

		public SoundEntryProvider(DataGenerator generator) {
			this.generator = generator;
		}

		@Override
		public void run(HashCache cache) throws IOException {
			generate(this.generator.getOutputFolder(), cache);
		}

		@Override
		public String getName() {
			return "Create Big Cannons custom sounds";
		}

		public void generate(Path path, HashCache cache) {
			Gson GSON = (new GsonBuilder()).setPrettyPrinting()
					.disableHtmlEscaping()
					.create();
			path = path.resolve("assets/" + CreateBigCannons.MOD_ID);

			try {
				JsonObject json = new JsonObject();
				ALL.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByKey())
						.forEach(entry -> {
							entry.getValue()
									.write(json);
						});
				DataProvider.save(GSON, cache, json, path.resolve("sounds.json"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
