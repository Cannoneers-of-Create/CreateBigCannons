package rbasamoyai.createbigcannons.cannons.big_cannons.breeches;

import java.util.Map;
import java.util.concurrent.Executor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class BigCannonBreechStrengthHandler {

	private static final Map<Block, Integer> BREECH_STRENGTHS = new Reference2IntOpenHashMap<>();

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		ReloadListener() { super(GSON, "big_cannon_breech_strength"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
			BREECH_STRENGTHS.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					Block block = CBCRegistryUtils.getOptionalBlock(entry.getKey()).orElseThrow(() -> {
						return new JsonSyntaxException("Could not find big cannon breech block '" + entry.getKey() + "'");
					});
					int strength = Math.max(0, GsonHelper.getAsInt(el.getAsJsonObject(), "breech_strength"));
					BREECH_STRENGTHS.put(block, strength);
				} catch (Exception e) {

				}
			}
		}
	}

	public static int getStrength(Block block, int defaultStrength) { return BREECH_STRENGTHS.getOrDefault(block, defaultStrength); }

	public static void writeBuf(RegistryFriendlyByteBuf buf, ClientboundBigCannonBreechStrengthPacket pkt) {
		buf.writeVarInt(pkt.strengths.size());
		for (Map.Entry<Block, Integer> entry : pkt.strengths.entrySet()) {
			buf.writeResourceLocation(CBCRegistryUtils.getBlockLocation(entry.getKey()))
				.writeVarInt(entry.getValue());
		}
	}

	public static ClientboundBigCannonBreechStrengthPacket readBuf(RegistryFriendlyByteBuf buf) {
		int sz = buf.readVarInt();
        Map<Block, Integer> strengths = new Reference2IntOpenHashMap<>();
		for (int i = 0; i < sz; ++i)
			strengths.put(CBCRegistryUtils.getBlock(buf.readResourceLocation()), buf.readVarInt());
        return new ClientboundBigCannonBreechStrengthPacket(strengths);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundBigCannonBreechStrengthPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundBigCannonBreechStrengthPacket(), server);
	}

    // TODO c6 playtest
	public record ClientboundBigCannonBreechStrengthPacket(Map<Block, Integer> strengths) implements RootPacket {
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBigCannonBreechStrengthPacket> STREAM_CODEC =
            StreamCodec.of(BigCannonBreechStrengthHandler::writeBuf, BigCannonBreechStrengthHandler::readBuf);

		public ClientboundBigCannonBreechStrengthPacket() { this(new Reference2IntOpenHashMap<>(BREECH_STRENGTHS)); }

		@Override
		public void handle(Executor exec, PacketListener listener, Player player) {
            BREECH_STRENGTHS.clear();
            BREECH_STRENGTHS.putAll(this.strengths);
		}
	}

}
