package rbasamoyai.createbigcannons.munitions.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.base.CBCJsonResourceReloadListener;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

public class BigCannonPropellantPropertiesHandler {

	public static final Map<Block, BigCannonPropellantProperties> PROPERTIES_MAP = new HashMap<>();

	public static class ReloadListener extends CBCJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		protected ReloadListener() { super(GSON, "big_cannon_propellant_properties"); }

		@Override
		protected void apply(Multimap<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
			PROPERTIES_MAP.clear();

			for (Map.Entry<ResourceLocation, JsonElement> e : object.entries()) {
				ResourceLocation loc = e.getKey();
				Block block = BuiltInRegistries.BLOCK.getOptional(loc).orElseThrow(() -> {
					return new JsonSyntaxException("Unknown block '" + loc + "'");
				});
				JsonElement el = e.getValue();
				if (!el.isJsonObject()) continue;
				JsonObject obj = el.getAsJsonObject();
				boolean replaceStats = GsonHelper.getAsBoolean(obj, "replace_stats", false);
				boolean replacePropellant = GsonHelper.getAsBoolean(obj, "replace_propellant_compat", false);
				BigCannonPropellantProperties oldProperties = PROPERTIES_MAP.get(block);
				BigCannonPropellantProperties newProperties = BigCannonPropellantProperties.fromJson(obj, oldProperties, replaceStats, replacePropellant);
				PROPERTIES_MAP.put(block, newProperties);
			}
		}
	}

	public static BigCannonPropellantProperties getProperties(Block block) { return PROPERTIES_MAP.getOrDefault(block, BigCannonPropellantProperties.DEFAULT); }

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(PROPERTIES_MAP.size());
		for (Map.Entry<Block, BigCannonPropellantProperties> entry : PROPERTIES_MAP.entrySet()) {
			buf.writeUtf(BuiltInRegistries.BLOCK.getKey(entry.getKey()).toString());
			entry.getValue().writeBuf(buf);
		}
	}

	public static void readBuf(FriendlyByteBuf buf) {
		PROPERTIES_MAP.clear();
		int sz = buf.readVarInt();

		for (int i = 0; i < sz; ++i) {
			PROPERTIES_MAP.put(BuiltInRegistries.BLOCK.get(new ResourceLocation(buf.readUtf())), BigCannonPropellantProperties.readBuf(buf));
		}
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundBigCannonPropellantPropertiesPacket(), player);
	}

	public record ClientboundBigCannonPropellantPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundBigCannonPropellantPropertiesPacket() { this(null); }

		public static ClientboundBigCannonPropellantPropertiesPacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundBigCannonPropellantPropertiesPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}
	}

}
