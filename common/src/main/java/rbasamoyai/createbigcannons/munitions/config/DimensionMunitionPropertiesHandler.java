package rbasamoyai.createbigcannons.munitions.config;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;

public class DimensionMunitionPropertiesHandler {

    public static Map<ResourceKey<Level>, DimensionMunitionProperties> DIMENSIONS = new HashMap<>();
	private static final DimensionMunitionProperties DEFAULT = new DimensionMunitionProperties(1, 1);

    public static class ReloadListener extends SimpleJsonResourceReloadListener {

        private static final Gson GSON = new Gson();

        public static final ReloadListener INSTANCE = new ReloadListener();

        protected ReloadListener() {
            super(GSON, "dimension_munition_properties");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
            DIMENSIONS.clear();

            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                JsonElement element = entry.getValue();
                if (!element.isJsonObject()) continue;
				try {
					ResourceLocation entityLoc = entry.getKey();
					ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, entry.getKey());
					DimensionMunitionProperties properties = DimensionMunitionProperties.fromJson(element.getAsJsonObject(), entityLoc.toString());
					DIMENSIONS.put(dimension, properties);
				} catch (Exception e) {

				}
            }
			int x = 0;
        }
    }

	public static DimensionMunitionProperties getProperties(ResourceKey<Level> dimension) { return DIMENSIONS.getOrDefault(dimension, DEFAULT); }
    public static DimensionMunitionProperties getProperties(Level level) { return getProperties(level.dimension()); }

}
