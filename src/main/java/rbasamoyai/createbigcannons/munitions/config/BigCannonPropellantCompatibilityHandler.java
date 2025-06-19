package rbasamoyai.createbigcannons.munitions.config;

import java.util.Map;
import java.util.concurrent.Executor;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
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

	public static void writeBuf(FriendlyByteBuf buf, ClientboundBigCannonPropellantCompatibilitiesPacket pkt) {
		buf.writeVarInt(pkt.properties.size());
		for (Map.Entry<Block, BigCannonPropellantCompatibilities> entry : pkt.properties.entrySet()) {
			buf.writeResourceLocation(CBCRegistryUtils.getBlockLocation(entry.getKey()));
			entry.getValue().writeBuf(buf);
		}
	}

	public static ClientboundBigCannonPropellantCompatibilitiesPacket readBuf(FriendlyByteBuf buf) {
		int sz = buf.readVarInt();
        Map<Block, BigCannonPropellantCompatibilities> properties = new Reference2ObjectOpenHashMap<>();
		for (int i = 0; i < sz; ++i)
			properties.put(CBCRegistryUtils.getBlock(buf.readResourceLocation()), BigCannonPropellantCompatibilities.readBuf(buf));
        return new ClientboundBigCannonPropellantCompatibilitiesPacket(properties);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundBigCannonPropellantCompatibilitiesPacket(), player);
	}

    // TODO c6 playtest
	public record ClientboundBigCannonPropellantCompatibilitiesPacket(Map<Block, BigCannonPropellantCompatibilities> properties) implements RootPacket {
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBigCannonPropellantCompatibilitiesPacket> STREAM_CODEC =
            StreamCodec.of(BigCannonPropellantCompatibilityHandler::writeBuf, BigCannonPropellantCompatibilityHandler::readBuf);

		public ClientboundBigCannonPropellantCompatibilitiesPacket() { this(new Reference2ObjectOpenHashMap<>(PROPERTIES_MAP)); }

		@Override
		public void handle(Executor exec, PacketListener listener, Player player) {
			PROPERTIES_MAP.clear();
            PROPERTIES_MAP.putAll(this.properties);
		}
	}

}
