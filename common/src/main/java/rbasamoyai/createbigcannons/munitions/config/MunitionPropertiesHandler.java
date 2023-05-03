package rbasamoyai.createbigcannons.munitions.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class MunitionPropertiesHandler {

    public static Map<EntityType<?>, MunitionProperties> PROJECTILES = new HashMap<>();
    private static final MunitionProperties DEFAULT = new MunitionProperties(0, 0, 0, true, false, null);

    public static class ReloadListener extends SimpleJsonResourceReloadListener {

        private static final Gson GSON = new Gson();

        public static final ReloadListener INSTANCE = new ReloadListener();

        protected ReloadListener() {
            super(GSON, "munition_properties");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
            cleanUp();

            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                JsonElement element = entry.getValue();
                if (element.isJsonObject()) {
                    try {
                        for (Map.Entry<String, JsonElement> jsonEntry : element.getAsJsonObject().entrySet()) {
                            if (jsonEntry.getValue().isJsonObject()) {
                                String s = jsonEntry.getKey();
                                MunitionProperties properties = MunitionProperties.fromJson(jsonEntry.getValue().getAsJsonObject(), s);
                                EntityType<?> type = Registry.ENTITY_TYPE.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
                                    return new JsonSyntaxException("Unknown entity type '" + jsonEntry.getKey() + "'");
                                });
                                PROJECTILES.put(type, properties);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    public static void cleanUp() {
        PROJECTILES.clear();
    }

    public static MunitionProperties getProperties(Entity entity) {
        return PROJECTILES.getOrDefault(entity.getType(), DEFAULT);
    }

}
