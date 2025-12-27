package rbasamoyai.createbigcannons.block_hit_effects;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class ProjectileHitEffectsHandler {

	private static final Map<EntityType<?>, ProjectileHitEffect> PROJECTILE_MAP = new Reference2ObjectOpenHashMap<>();
	private static final ProjectileHitEffect DEFAULT = new ProjectileHitEffect(1, 3, -0.5f, 3.5f);

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		ReloadListener() { super(GSON, "projectile_hit_effects"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			PROJECTILE_MAP.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					EntityType<?> entityType = CBCRegistryUtils.getOptionalEntityType(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown block '" + loc + "'");
					});
					PROJECTILE_MAP.put(entityType, ProjectileHitEffect.fromJson(el.getAsJsonObject()));
				} catch (Exception e) {
					CreateBigCannons.LOGGER.warn("Exception loading projectile hit effects: {}", e.getMessage());
				}
			}
		}
	}

	public static ProjectileHitEffect getProperties(Entity entity) { return getProperties(entity.getType()); }
	public static ProjectileHitEffect getProperties(EntityType<?> entityType) { return PROJECTILE_MAP.getOrDefault(entityType, DEFAULT); }

}
