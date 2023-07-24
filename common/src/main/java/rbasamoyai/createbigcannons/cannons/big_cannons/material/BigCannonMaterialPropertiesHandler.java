package rbasamoyai.createbigcannons.cannons.big_cannons.material;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.createbigcannons.network.RootPacket;

public class BigCannonMaterialPropertiesHandler {

	public static final Map<BigCannonMaterial, BigCannonMaterialProperties> PROPERTIES = new HashMap<>();

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
					PROPERTIES.put(material, BigCannonMaterialProperties.fromJson(el.getAsJsonObject()));
				} catch (Exception e) {

				}
			}
			int x = 0;
		}
	}

	public static BigCannonMaterialProperties getMaterial(BigCannonMaterial material) { return PROPERTIES.get(material); }

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(PROPERTIES.size());
		for (Map.Entry<BigCannonMaterial, BigCannonMaterialProperties> entry : PROPERTIES.entrySet()) {
			buf.writeUtf(entry.getKey().name().toString());
			entry.getValue().writeBuf(buf);
		}
	}

	public static void readBuf(FriendlyByteBuf buf) {
		PROPERTIES.clear();
		int sz = buf.readVarInt();

		for (int i = 0; i < sz; ++i) {
			PROPERTIES.put(BigCannonMaterial.fromName(new ResourceLocation(buf.readUtf())), BigCannonMaterialProperties.fromBuf(buf));
		}
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundBigCannonMaterialPropertiesPacket(), player);
	}

	public record ClientboundBigCannonMaterialPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundBigCannonMaterialPropertiesPacket() { this(null); }

		public static MunitionPropertiesHandler.ClientboundMunitionPropertiesPacket copyOf(FriendlyByteBuf buf) {
			return new MunitionPropertiesHandler.ClientboundMunitionPropertiesPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}
	}

}
