package rbasamoyai.createbigcannons.cannons.autocannon.material;

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

public class AutocannonMaterialPropertiesHandler {

	private static final Map<AutocannonMaterial, AutocannonMaterialProperties> PROPERTIES = new Reference2ObjectOpenHashMap<>();

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		public ReloadListener() { super(GSON, "autocannon_materials"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
			PROPERTIES.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					AutocannonMaterial material = AutocannonMaterial.fromName(entry.getKey());
					PROPERTIES.put(material, AutocannonMaterialProperties.fromJson(el.getAsJsonObject()));
				} catch (Exception e) {

				}
			}
		}
	}

	public static AutocannonMaterialProperties getMaterial(AutocannonMaterial material) { return PROPERTIES.get(material); }

	public static void writeBuf(RegistryFriendlyByteBuf buf, ClientboundAutocannonMaterialPropertiesPacket pkt) {
		buf.writeVarInt(pkt.properties.size());
		for (Map.Entry<AutocannonMaterial, AutocannonMaterialProperties> entry : pkt.properties.entrySet()) {
			buf.writeResourceLocation(entry.getKey().name());
			entry.getValue().writeBuf(buf);
		}
	}

	public static ClientboundAutocannonMaterialPropertiesPacket readBuf(RegistryFriendlyByteBuf buf) {
        Map<AutocannonMaterial, AutocannonMaterialProperties> properties = new Reference2ObjectOpenHashMap<>();
		int sz = buf.readVarInt();
		for (int i = 0; i < sz; ++i)
			properties.put(AutocannonMaterial.fromName(buf.readResourceLocation()), AutocannonMaterialProperties.fromBuf(buf));
        return new ClientboundAutocannonMaterialPropertiesPacket(properties);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundAutocannonMaterialPropertiesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundAutocannonMaterialPropertiesPacket(), server);
	}

    // TODO c6 playtest
	public record ClientboundAutocannonMaterialPropertiesPacket(Map<AutocannonMaterial, AutocannonMaterialProperties> properties) implements RootPacket {
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAutocannonMaterialPropertiesPacket> STREAM_CODEC =
            StreamCodec.of(AutocannonMaterialPropertiesHandler::writeBuf, AutocannonMaterialPropertiesHandler::readBuf);

		public ClientboundAutocannonMaterialPropertiesPacket() { this(new Reference2ObjectOpenHashMap<>(PROPERTIES)); }

		@Override
		public void handle(Executor exec, PacketListener listener, Player player) {
			PROPERTIES.clear();
            PROPERTIES.putAll(this.properties);
		}
	}

}
