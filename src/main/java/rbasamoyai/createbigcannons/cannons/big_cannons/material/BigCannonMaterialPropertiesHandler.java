package rbasamoyai.createbigcannons.cannons.big_cannons.material;

import java.util.Map;
import java.util.concurrent.Executor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

public class BigCannonMaterialPropertiesHandler {

	private static final Map<BigCannonMaterial, BigCannonMaterialProperties> PROPERTIES = new Reference2ObjectOpenHashMap<>();

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		public ReloadListener() { super(GSON, "big_cannon_materials"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
			PROPERTIES.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					BigCannonMaterial material = BigCannonMaterial.fromName(entry.getKey());
					PROPERTIES.put(material, BigCannonMaterialProperties.fromJson(entry.getKey(), el.getAsJsonObject()));
				} catch (Exception e) {

				}
			}
		}
	}

	public static BigCannonMaterialProperties getMaterial(BigCannonMaterial material) { return PROPERTIES.get(material); }

	public static void writeBuf(RegistryFriendlyByteBuf buf, ClientboundBigCannonMaterialPropertiesPacket pkt) {
		buf.writeVarInt(pkt.properties.size());
		for (Map.Entry<BigCannonMaterial, BigCannonMaterialProperties> entry : pkt.properties.entrySet()) {
			buf.writeResourceLocation(entry.getKey().name());
			entry.getValue().writeBuf(buf);
		}
	}

	public static ClientboundBigCannonMaterialPropertiesPacket readBuf(RegistryFriendlyByteBuf buf) {
		int sz = buf.readVarInt();
        Map<BigCannonMaterial, BigCannonMaterialProperties> properties = new Reference2ObjectOpenHashMap<>();
		for (int i = 0; i < sz; ++i)
			properties.put(BigCannonMaterial.fromName(buf.readResourceLocation()), BigCannonMaterialProperties.fromBuf(buf));
        return new ClientboundBigCannonMaterialPropertiesPacket(properties);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundBigCannonMaterialPropertiesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundBigCannonMaterialPropertiesPacket(), server);
	}

    // TODO c6 playtest
	public record ClientboundBigCannonMaterialPropertiesPacket(Map<BigCannonMaterial, BigCannonMaterialProperties> properties) implements RootPacket {
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBigCannonMaterialPropertiesPacket> STREAM_CODEC =
            StreamCodec.of(BigCannonMaterialPropertiesHandler::writeBuf, BigCannonMaterialPropertiesHandler::readBuf);

		public ClientboundBigCannonMaterialPropertiesPacket() { this(new Reference2ObjectOpenHashMap<>(PROPERTIES)); }

		@Override
		public void handle(Executor exec, PacketListener listener, Player player) {
            PROPERTIES.clear();
            PROPERTIES.putAll(this.properties);
		}
	}

}
