package rbasamoyai.createbigcannons.cannons.autocannon.material;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

public class AutocannonMaterialPropertiesHandler {

	public static final Map<AutocannonMaterial, AutocannonMaterialProperties> PROPERTIES = new Reference2ObjectOpenHashMap<>();

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

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(PROPERTIES.size());
		for (Map.Entry<AutocannonMaterial, AutocannonMaterialProperties> entry : PROPERTIES.entrySet()) {
			buf.writeUtf(entry.getKey().name().toString());
			entry.getValue().writeBuf(buf);
		}
	}

	public static void readBuf(FriendlyByteBuf buf) {
		PROPERTIES.clear();
		int sz = buf.readVarInt();

		for (int i = 0; i < sz; ++i) {
			PROPERTIES.put(AutocannonMaterial.fromName(new ResourceLocation(buf.readUtf())), AutocannonMaterialProperties.fromBuf(buf));
		}
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundAutocannonMaterialPropertiesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundAutocannonMaterialPropertiesPacket(), server);
	}

	public record ClientboundAutocannonMaterialPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundAutocannonMaterialPropertiesPacket() { this(null); }

		public static ClientboundAutocannonMaterialPropertiesPacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundAutocannonMaterialPropertiesPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @javax.annotation.Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}
	}

}
