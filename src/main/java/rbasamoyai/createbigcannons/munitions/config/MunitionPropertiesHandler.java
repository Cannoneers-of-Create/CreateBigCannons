package rbasamoyai.createbigcannons.munitions.config;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class MunitionPropertiesHandler {

	private static final Map<EntityType<?>, PropertiesTypeHandler<EntityType<?>, ?>> PROJECTILES = new Reference2ReferenceOpenHashMap<>();
	private static final Map<Block, PropertiesTypeHandler<Block, ?>> BLOCK_PROPELLANT = new Reference2ReferenceOpenHashMap<>();
	private static final Map<Item, PropertiesTypeHandler<Item, ?>> ITEM_PROPELLANT = new Reference2ReferenceOpenHashMap<>();

    public static class ReloadListenerProjectiles extends SimpleJsonResourceReloadListener {
        private static final Gson GSON = new Gson();

        public static final ReloadListenerProjectiles INSTANCE = new ReloadListenerProjectiles();

        protected ReloadListenerProjectiles() {
            super(GSON, "munition_properties/projectiles");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
            PROJECTILES.values().forEach(PropertiesTypeHandler::clearForReload);

            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                JsonElement element = entry.getValue();
                if (!element.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					EntityType<?> type = CBCRegistryUtils.getOptionalEntityType(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown entity type '" + loc + "'");
					});
					PropertiesTypeHandler<EntityType<?>, ?> handler = PROJECTILES.get(type);
					if (handler == null)
						throw new JsonSyntaxException("No configuration for entity type '" + loc + "' present");
					handler.loadFromJson(type, loc, element.getAsJsonObject());
				} catch (Exception e) {

				}
            }
        }
    }

	public static class ReloadListenerBlockPropellant extends SimpleJsonResourceReloadListener {

		private static final Gson GSON = new Gson();

		public static final ReloadListenerBlockPropellant INSTANCE = new ReloadListenerBlockPropellant();

		protected ReloadListenerBlockPropellant() {
			super(GSON, "munition_properties/block_propellant");
		}

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
			BLOCK_PROPELLANT.values().forEach(PropertiesTypeHandler::clearForReload);

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement element = entry.getValue();
				if (!element.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					Block block = CBCRegistryUtils.getOptionalBlock(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown block '" + loc + "'");
					});
					PropertiesTypeHandler<Block, ?> handler = BLOCK_PROPELLANT.get(block);
					if (handler == null)
						throw new JsonSyntaxException("No configuration for block '" + loc + "' present");
					handler.loadFromJson(block, loc, element.getAsJsonObject());
				} catch (Exception e) {

				}
			}
		}
	}

	public static class ReloadListenerItemPropellant extends SimpleJsonResourceReloadListener {

		private static final Gson GSON = new Gson();

		public static final ReloadListenerItemPropellant INSTANCE = new ReloadListenerItemPropellant();

		protected ReloadListenerItemPropellant() {
			super(GSON, "munition_properties/item_propellant");
		}

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
			ITEM_PROPELLANT.values().forEach(PropertiesTypeHandler::clearForReload);

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement element = entry.getValue();
				if (!element.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					Item item = CBCRegistryUtils.getOptionalItem(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown item '" + loc + "'");
					});
					PropertiesTypeHandler<Item, ?> handler = ITEM_PROPELLANT.get(item);
					if (handler == null)
						throw new JsonSyntaxException("No configuration for item '" + loc + "' present");
					handler.loadFromJson(item, loc, element.getAsJsonObject());
				} catch (Exception e) {

				}
			}
		}
	}

	public static void registerProjectileHandler(EntityType<?> type, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		if (PROJECTILES.containsKey(type))
			throw new IllegalStateException("Handler for entity type " + CBCRegistryUtils.getEntityTypeLocation(type) + " already registered");
		PROJECTILES.put(type, handler);
	}

	public static void registerBlockPropellantHandler(Block block, PropertiesTypeHandler<Block, ?> handler) {
		if (BLOCK_PROPELLANT.containsKey(block))
			throw new IllegalStateException("Handler for block " + CBCRegistryUtils.getBlockLocation(block) + " already registered");
		BLOCK_PROPELLANT.put(block, handler);
	}

	public static void registerItemPropellantHandler(Item item, PropertiesTypeHandler<Item, ?> handler) {
		if (ITEM_PROPELLANT.containsKey(item))
			throw new IllegalStateException("Handler for item " + CBCRegistryUtils.getItemLocation(item) + " already registered");
		ITEM_PROPELLANT.put(item, handler);
	}

	public static void writeBuf(RegistryFriendlyByteBuf buf) {
		writeToNetwork(buf, PROJECTILES, CBCRegistryUtils.getEntityTypeRegistry());
		writeToNetwork(buf, BLOCK_PROPELLANT, CBCRegistryUtils.getBlockRegistry());
		writeToNetwork(buf, ITEM_PROPELLANT, CBCRegistryUtils.getItemRegistry());
	}

	private static <TYPE> void writeToNetwork(RegistryFriendlyByteBuf buf, Map<TYPE, PropertiesTypeHandler<TYPE, ?>> handlers, Registry<TYPE> registry) {
		buf.writeVarInt(handlers.size());
		for (Map.Entry<TYPE, PropertiesTypeHandler<TYPE, ?>> entry : handlers.entrySet()) {
			TYPE type = entry.getKey();
			buf.writeResourceLocation(registry.getKey(type));
			entry.getValue().writeToNetwork(type, buf);
		}
	}

	public static void readBuf(RegistryFriendlyByteBuf buf) {
		readFromNetwork(buf, PROJECTILES, CBCRegistryUtils.getEntityTypeRegistry());
		readFromNetwork(buf, BLOCK_PROPELLANT, CBCRegistryUtils.getBlockRegistry());
		readFromNetwork(buf, ITEM_PROPELLANT, CBCRegistryUtils.getItemRegistry());
	}

	private static <TYPE> void readFromNetwork(RegistryFriendlyByteBuf buf, Map<TYPE, PropertiesTypeHandler<TYPE, ?>> map, Registry<TYPE> registry) {
		map.values().forEach(PropertiesTypeHandler::clearForReload);
		int size = buf.readVarInt();
		for (int i = 0; i < size; ++i) {
			TYPE type = registry.get(buf.readResourceLocation());
			map.get(type).loadFromNetwork(type, buf);
		}
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundMunitionPropertiesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundMunitionPropertiesPacket(), server);
	}

    // TODO c6 playtest
	public record ClientboundMunitionPropertiesPacket(@Nullable RegistryFriendlyByteBuf buf) implements RootPacket {
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundMunitionPropertiesPacket> STREAM_CODEC =
            StreamCodec.of((b, t) -> writeBuf(b), b -> {
                RegistryFriendlyByteBuf copy = new RegistryFriendlyByteBuf(b.copy(), b.registryAccess());
                b.skipBytes(b.readableBytes());
                return new ClientboundMunitionPropertiesPacket(copy);
            });

        public ClientboundMunitionPropertiesPacket() { this(null); }

		@Override
        public void handle(Executor exec, PacketListener listener, Player player) {
            if (this.buf != null)
                readBuf(this.buf);
        }
	}

}
