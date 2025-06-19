package rbasamoyai.createbigcannons.munitions.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

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
					ResourceKey<Level> dimension = ResourceKey.create(CBCRegistryUtils.getDimensionRegistryKey(), entry.getKey());
					DimensionMunitionProperties properties = DimensionMunitionProperties.fromJson(element.getAsJsonObject(), entityLoc.toString());
					DIMENSIONS.put(dimension, properties);
				} catch (Exception e) {

				}
            }
        }
    }

	public static DimensionMunitionProperties getProperties(ResourceKey<Level> dimension) { return DIMENSIONS.getOrDefault(dimension, DEFAULT); }
    public static DimensionMunitionProperties getProperties(Level level) { return getProperties(level.dimension()); }

	public static void writeBuf(RegistryFriendlyByteBuf buf, ClientboundSyncDimensionMunitionPropertiesPacket pkt) {
		buf.writeVarInt(pkt.dimensions.size());
		for (Map.Entry<ResourceKey<Level>, DimensionMunitionProperties> entry : pkt.dimensions.entrySet()) {
			buf.writeResourceLocation(entry.getKey().location());
			entry.getValue().toNetwork(buf);
		}
	}

	public static ClientboundSyncDimensionMunitionPropertiesPacket readBuf(RegistryFriendlyByteBuf buf) {
		int sz = buf.readVarInt();
        Map<ResourceKey<Level>, DimensionMunitionProperties> dimensions = new HashMap<>();
		for (int i = 0; i < sz; ++i) {
			ResourceLocation loc = buf.readResourceLocation();
			ResourceKey<Level> key = ResourceKey.create(CBCRegistryUtils.getDimensionRegistryKey(), loc);
			DimensionMunitionProperties properties = DimensionMunitionProperties.fromNetwork(buf);
            dimensions.put(key, properties);
		}
        return new ClientboundSyncDimensionMunitionPropertiesPacket(dimensions);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundSyncDimensionMunitionPropertiesPacket(), server);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundSyncDimensionMunitionPropertiesPacket(), player);
	}

    // TODO c6 playtest
	public record ClientboundSyncDimensionMunitionPropertiesPacket(Map<ResourceKey<Level>, DimensionMunitionProperties> dimensions) implements RootPacket {
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncDimensionMunitionPropertiesPacket> STREAM_CODEC =
            StreamCodec.of(DimensionMunitionPropertiesHandler::writeBuf, DimensionMunitionPropertiesHandler::readBuf);

		public ClientboundSyncDimensionMunitionPropertiesPacket() { this(new HashMap<>(DIMENSIONS)); }

		@Override
		public void handle(Executor exec, PacketListener listener, Player player) {
            DIMENSIONS.clear();
            DIMENSIONS.putAll(this.dimensions);
		}
	}

}
