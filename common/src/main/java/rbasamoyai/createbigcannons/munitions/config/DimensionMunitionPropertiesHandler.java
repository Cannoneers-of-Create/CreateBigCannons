package rbasamoyai.createbigcannons.munitions.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

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
        }
    }

	public static DimensionMunitionProperties getProperties(ResourceKey<Level> dimension) { return DIMENSIONS.getOrDefault(dimension, DEFAULT); }
    public static DimensionMunitionProperties getProperties(Level level) { return getProperties(level.dimension()); }

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(DIMENSIONS.size());
		for (Map.Entry<ResourceKey<Level>, DimensionMunitionProperties> entry : DIMENSIONS.entrySet()) {
			buf.writeResourceLocation(entry.getKey().location());
			entry.getValue().toNetwork(buf);
		}
	}

	public static void readBuf(FriendlyByteBuf buf) {
		DIMENSIONS.clear();
		int sz = buf.readVarInt();
		for (int i = 0; i < sz; ++i) {
			ResourceLocation loc = buf.readResourceLocation();
			ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, loc);
			DimensionMunitionProperties properties = DimensionMunitionProperties.fromNetwork(buf);
			DIMENSIONS.put(key, properties);
		}
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundSyncDimensionMunitionPropertiesPacket(), server);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundSyncDimensionMunitionPropertiesPacket(), player);
	}

	public record ClientboundSyncDimensionMunitionPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundSyncDimensionMunitionPropertiesPacket() { this(null); }

		public static ClientboundSyncDimensionMunitionPropertiesPacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundSyncDimensionMunitionPropertiesPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}
	}

}
