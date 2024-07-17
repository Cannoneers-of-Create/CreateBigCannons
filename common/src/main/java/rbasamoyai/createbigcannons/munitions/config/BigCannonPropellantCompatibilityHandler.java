package rbasamoyai.createbigcannons.munitions.config;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.registries.BuiltInRegistries;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
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
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class BigCannonPropellantCompatibilityHandler {

	public static final Map<Block, BigCannonPropellantCompatibilities> PROPERTIES_MAP = new Reference2ObjectOpenHashMap<>();

	public static class ReloadListener extends CBCJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		protected ReloadListener() { super(GSON, "big_cannon_propellant_compatibility"); }

		@Override
		protected void apply(Multimap<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
			PROPERTIES_MAP.clear();

			for (Map.Entry<ResourceLocation, JsonElement> e : object.entries()) {
				ResourceLocation loc = e.getKey();
				Block block = CBCRegistryUtils.getOptionalBlock(loc).orElseThrow(() -> {
					return new JsonSyntaxException("Unknown block '" + loc + "'");
				});
				JsonElement el = e.getValue();
				if (!el.isJsonObject()) continue;
				JsonObject obj = el.getAsJsonObject();
				boolean replaceValues = GsonHelper.getAsBoolean(obj, "replace", false);
				BigCannonPropellantCompatibilities oldProperties = PROPERTIES_MAP.get(block);
				BigCannonPropellantCompatibilities newProperties = BigCannonPropellantCompatibilities.fromJson(obj, oldProperties, replaceValues);
				PROPERTIES_MAP.put(block, newProperties);
			}
		}
	}

	public static BigCannonPropellantCompatibilities getCompatibilities(Block block) {
		return PROPERTIES_MAP.getOrDefault(block, BigCannonPropellantCompatibilities.DEFAULT);
	}

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(PROPERTIES_MAP.size());
		for (Map.Entry<Block, BigCannonPropellantCompatibilities> entry : PROPERTIES_MAP.entrySet()) {
			buf.writeResourceLocation(CBCRegistryUtils.getBlockLocation(entry.getKey()));
			entry.getValue().writeBuf(buf);
		}
	}

	public static void readBuf(FriendlyByteBuf buf) {
		PROPERTIES_MAP.clear();
		int sz = buf.readVarInt();

		for (int i = 0; i < sz; ++i) {
			PROPERTIES_MAP.put(CBCRegistryUtils.getBlock(buf.readResourceLocation()), BigCannonPropellantCompatibilities.readBuf(buf));
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
