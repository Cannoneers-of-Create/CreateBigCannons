package rbasamoyai.createbigcannons.block_armor_properties;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class BlockArmorPropertiesHandler {

	private static final Map<Block, SimpleBlockArmorProperties> TAG_MAP = new Reference2ObjectOpenHashMap<>();
	private static final Map<Block, BlockArmorPropertiesProvider> BLOCK_MAP = new Reference2ObjectOpenHashMap<>();
	private static final Map<TagKey<Block>, SimpleBlockArmorProperties> TAGS_TO_EVALUATE = new Object2ObjectLinkedOpenHashMap<>();

	private static final Map<Block, BlockArmorPropertiesSerializer<?>> CUSTOM_SERIALIZERS = new Reference2ReferenceOpenHashMap<>();
	private static final VariantBlockArmorProperties FALLBACK_PROPERTIES = new VariantBlockArmorProperties(new SimpleBlockArmorProperties(0), new Reference2ObjectOpenHashMap<>());

	private static final BlockArmorPropertiesProvider FALLBACK_PROVIDER = new BlockArmorPropertiesProvider() {
		@Override public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) { return state.getBlock().getExplosionResistance(); }
	};

	public static class BlockReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final BlockReloadListener INSTANCE = new BlockReloadListener();

		public BlockReloadListener() { super(GSON, "block_armor"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			cleanUp();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					if (loc.getPath().startsWith("tags/")) {
						ResourceLocation pruned = CBCUtils.location(loc.getNamespace(), loc.getPath().substring(5));
						TagKey<Block> tag = TagKey.create(CBCRegistryUtils.getBlockRegistryKey(), pruned);
						TAGS_TO_EVALUATE.put(tag, SimpleBlockArmorProperties.fromJson(el.getAsJsonObject()));
					} else {
						Block block = CBCRegistryUtils.getOptionalBlock(loc).orElseThrow(() -> {
							return new JsonSyntaxException("Unknown block '" + loc + "'");
						});
						if (CUSTOM_SERIALIZERS.containsKey(block)) {
							BLOCK_MAP.put(block, CUSTOM_SERIALIZERS.get(block).loadBlockArmorPropertiesFromJson(block, el.getAsJsonObject()));
						} else {
							BLOCK_MAP.put(block, VariantBlockArmorProperties.fromJson(block, el.getAsJsonObject()));
						}
					}
				} catch (Exception e) {
					//CreateBigCannons.LOGGER.warn("Exception loading block armor properties: {}", e.getMessage());
					//Commented out for silent "optional" loading if a bit silly
				}
			}
		}
	}

	public static void loadTags() {
		TAG_MAP.clear();
		for (Map.Entry<TagKey<Block>, SimpleBlockArmorProperties> entry : TAGS_TO_EVALUATE.entrySet()) {
			SimpleBlockArmorProperties properties = entry.getValue();
			for (Holder<Block> holder : CBCRegistryUtils.getBlockTagEntries(entry.getKey())) {
				TAG_MAP.put(holder.value(), properties);
			}
		}
		TAGS_TO_EVALUATE.clear();
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundSyncBlockArmorPropertiesPacket(), server);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundSyncBlockArmorPropertiesPacket(), player);
	}

	public static void cleanUp() {
		TAG_MAP.clear();
		BLOCK_MAP.clear();
		TAGS_TO_EVALUATE.clear();
	}

	public static BlockArmorPropertiesProvider getProperties(BlockState state) { return getProperties(state.getBlock()); }

	public static BlockArmorPropertiesProvider getProperties(Block block) {
		if (BLOCK_MAP.containsKey(block)) return BLOCK_MAP.get(block);
		if (TAG_MAP.containsKey(block)) return TAG_MAP.get(block);
		return FALLBACK_PROVIDER;
	}

	public static <T extends BlockArmorPropertiesSerializer<?>> T registerCustomSerializer(Block block, T ser) {
		if (CUSTOM_SERIALIZERS.containsKey(block)) {
			throw new IllegalStateException("Serializer for block " + CBCRegistryUtils.getBlockLocation(block) + " already registered");
		}
		CUSTOM_SERIALIZERS.put(block, ser);
		return ser;
	}

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(TAG_MAP.size());
		for (Map.Entry<Block, SimpleBlockArmorProperties> entry : TAG_MAP.entrySet()) {
			buf.writeResourceLocation(CBCRegistryUtils.getBlockLocation(entry.getKey()));
			entry.getValue().toNetwork(buf);
		}
		buf.writeVarInt(BLOCK_MAP.size());
		for (Map.Entry<Block, BlockArmorPropertiesProvider> entry : BLOCK_MAP.entrySet()) {
			buf.writeResourceLocation(CBCRegistryUtils.getBlockLocation(entry.getKey()));
			toNetworkCasted(entry.getKey(), entry.getValue(), buf);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T extends BlockArmorPropertiesProvider> void toNetworkCasted(Block block, T properties, FriendlyByteBuf buf) {
		BlockArmorPropertiesSerializer<T> ser = (BlockArmorPropertiesSerializer<T>) CUSTOM_SERIALIZERS.get(block);
		if (ser != null) {
			ser.toNetwork(properties, buf);
		} else if (properties instanceof VariantBlockArmorProperties vbap) {
			vbap.toNetwork(buf);
		} else { // Should not happen!
			CreateBigCannons.LOGGER.warn("Invalid regular block properties encountered for block {}", block);
			FALLBACK_PROPERTIES.toNetwork(buf);
		}
	}

	public static void readBuf(FriendlyByteBuf buf) {
		TAG_MAP.clear();
		int tagSz = buf.readVarInt();
		for (int i = 0; i < tagSz; ++i) {
			Block block = CBCRegistryUtils.getBlock(buf.readResourceLocation());
			SimpleBlockArmorProperties properties = SimpleBlockArmorProperties.fromNetwork(buf);
			TAG_MAP.put(block, properties);
		}
		BLOCK_MAP.clear();
		int blockSz = buf.readVarInt();
		for (int i = 0; i < blockSz; ++i) {
			Block block = CBCRegistryUtils.getBlock(buf.readResourceLocation());
			BlockArmorPropertiesSerializer<?> ser = CUSTOM_SERIALIZERS.get(block);
			BLOCK_MAP.put(block, ser == null ? VariantBlockArmorProperties.fromNetwork(buf) : ser.fromNetwork(buf));
		}
	}

	public record ClientboundSyncBlockArmorPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundSyncBlockArmorPropertiesPacket() { this(null); }

		public static ClientboundSyncBlockArmorPropertiesPacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundSyncBlockArmorPropertiesPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}
	}

}
