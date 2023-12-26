package rbasamoyai.createbigcannons.munitions.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

public class MunitionPropertiesHandler {

	private static final Map<EntityType<?>, MunitionPropertiesSerializer<?>> ENTITY_TYPE_SERIALIZERS = new HashMap<>();
    private static final Map<EntityType<?>, MunitionProperties> PROJECTILES = new HashMap<>();

	private static final Map<Block, MunitionPropertiesSerializer<?>> BLOCK_SERIALIZERS = new HashMap<>();
	private static final Map<Block, MunitionProperties> BLOCK_PROPELLANT = new HashMap<>();

	private static final Map<Item, MunitionPropertiesSerializer<?>> ITEM_SERIALIZERS = new HashMap<>();
	private static final Map<Item, MunitionProperties> ITEM_PROPELLANT = new HashMap<>();

    public static class ReloadListenerProjectiles extends SimpleJsonResourceReloadListener {

        private static final Gson GSON = new Gson();

        public static final ReloadListenerProjectiles INSTANCE = new ReloadListenerProjectiles();

        protected ReloadListenerProjectiles() {
            super(GSON, "munition_properties/projectiles");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
            PROJECTILES.clear();

            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                JsonElement element = entry.getValue();
                if (!element.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();

					EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getOptional(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown entity type '" + loc + "'");
					});
					MunitionPropertiesSerializer<?> ser = ENTITY_TYPE_SERIALIZERS.get(type);
					if (ser == null)
						throw new JsonSyntaxException("No configuration for entity type '" + loc + "' present");
					MunitionProperties properties = ser.fromJson(loc, element.getAsJsonObject());
					PROJECTILES.put(type, properties);
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
			BLOCK_PROPELLANT.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement element = entry.getValue();
				if (!element.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();

					Block block = BuiltInRegistries.BLOCK.getOptional(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown block '" + loc + "'");
					});
					MunitionPropertiesSerializer<?> ser = BLOCK_SERIALIZERS.get(block);
					if (ser == null)
						throw new JsonSyntaxException("No configuration for block '" + loc + "' present");
					MunitionProperties properties = ser.fromJson(loc, element.getAsJsonObject());
					BLOCK_PROPELLANT.put(block, properties);
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
			ITEM_PROPELLANT.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement element = entry.getValue();
				if (!element.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();

					Item item = BuiltInRegistries.ITEM.getOptional(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown item '" + loc + "'");
					});
					MunitionPropertiesSerializer<?> ser = ITEM_SERIALIZERS.get(item);
					if (ser == null)
						throw new JsonSyntaxException("No configuration for item '" + loc + "' present");
					MunitionProperties properties = ser.fromJson(loc, element.getAsJsonObject());
					ITEM_PROPELLANT.put(item, properties);
				} catch (Exception e) {

				}
			}
		}
	}

	public static <T extends MunitionProperties> void registerPropertiesSerializer(EntityType<? extends PropertiesMunitionEntity<T>> type, MunitionPropertiesSerializer<T> ser) {
		if (ENTITY_TYPE_SERIALIZERS.containsKey(type))
			throw new IllegalStateException("Serializer for entity type " + BuiltInRegistries.ENTITY_TYPE.getKey(type) + " already registered");
		ENTITY_TYPE_SERIALIZERS.put(type, ser);
	}

	public static <T extends MunitionProperties, B extends Block & PropertiesMunitionBlock<T>> void registerPropertiesSerializer(B block, MunitionPropertiesSerializer<T> ser) {
		if (BLOCK_SERIALIZERS.containsKey(block))
			throw new IllegalStateException("Serializer for block " + BuiltInRegistries.BLOCK.getKey(block) + " already registered");
		BLOCK_SERIALIZERS.put(block, ser);
	}

	public static <T extends MunitionProperties, I extends Item & PropertiesMunitionItem<T>> void registerPropertiesSerializer(I item, MunitionPropertiesSerializer<T> ser) {
		if (ITEM_SERIALIZERS.containsKey(item))
			throw new IllegalStateException("Serializer for item " + BuiltInRegistries.ITEM.getKey(item) + " already registered");
		ITEM_SERIALIZERS.put(item, ser);
	}

	@Nullable public static MunitionProperties getProperties(EntityType<?> type) { return PROJECTILES.get(type); }
    @Nullable public static MunitionProperties getProperties(Entity entity) { return getProperties(entity.getType()); }

	@Nullable public static MunitionProperties getProperties(Block block) { return BLOCK_PROPELLANT.get(block); }
	@Nullable public static MunitionProperties getProperties(BlockState state) { return getProperties(state.getBlock()); }

	@Nullable public static MunitionProperties getProperties(Item item) { return ITEM_PROPELLANT.get(item); }
	@Nullable public static MunitionProperties getProperties(ItemStack stack) { return getProperties(stack.getItem()); }

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(PROJECTILES.size());
		for (Map.Entry<EntityType<?>, MunitionProperties> entry : PROJECTILES.entrySet()) {
			toNetworkCasted(buf, entry.getKey(), entry.getValue());
		}
		buf.writeVarInt(BLOCK_PROPELLANT.size());
		for (Map.Entry<Block, MunitionProperties> entry : BLOCK_PROPELLANT.entrySet()) {
			toNetworkCasted(buf, entry.getKey(), entry.getValue());
		}
		buf.writeVarInt(ITEM_PROPELLANT.size());
		for (Map.Entry<Item, MunitionProperties> entry : ITEM_PROPELLANT.entrySet()) {
			toNetworkCasted(buf, entry.getKey(), entry.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private static <T extends MunitionProperties> void toNetworkCasted(FriendlyByteBuf buf, EntityType<?> type, T properties) {
		buf.writeUtf(BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
		MunitionPropertiesSerializer<T> ser = (MunitionPropertiesSerializer<T>) ENTITY_TYPE_SERIALIZERS.get(type);
		ser.toNetwork(buf, properties);
	}

	@SuppressWarnings("unchecked")
	private static <T extends MunitionProperties> void toNetworkCasted(FriendlyByteBuf buf, Block block, T properties) {
		buf.writeUtf(BuiltInRegistries.BLOCK.getKey(block).toString());
		MunitionPropertiesSerializer<T> ser = (MunitionPropertiesSerializer<T>) BLOCK_SERIALIZERS.get(block);
		ser.toNetwork(buf, properties);
	}

	@SuppressWarnings("unchecked")
	private static <T extends MunitionProperties> void toNetworkCasted(FriendlyByteBuf buf, Item item, T properties) {
		buf.writeUtf(BuiltInRegistries.ITEM.getKey(item).toString());
		MunitionPropertiesSerializer<T> ser = (MunitionPropertiesSerializer<T>) ITEM_SERIALIZERS.get(item);
		ser.toNetwork(buf, properties);
	}

	public static void readBuf(FriendlyByteBuf buf) {
		PROJECTILES.clear();
		int szProj = buf.readVarInt();
		for (int i = 0; i < szProj; ++i) {
			ResourceLocation loc = new ResourceLocation(buf.readUtf());
			EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(loc);
			MunitionPropertiesSerializer<?> ser = ENTITY_TYPE_SERIALIZERS.get(type);
			PROJECTILES.put(type, ser.fromNetwork(loc, buf));
		}
		BLOCK_PROPELLANT.clear();
		int szBlock = buf.readVarInt();
		for (int i = 0; i < szBlock; ++i) {
			ResourceLocation loc = new ResourceLocation(buf.readUtf());
			Block block = BuiltInRegistries.BLOCK.get(loc);
			MunitionPropertiesSerializer<?> ser = BLOCK_SERIALIZERS.get(block);
			BLOCK_PROPELLANT.put(block, ser.fromNetwork(loc, buf));
		}
		ITEM_PROPELLANT.clear();
		int szItem = buf.readVarInt();
		for (int i = 0; i < szItem; ++i) {
			ResourceLocation loc = new ResourceLocation(buf.readUtf());
			Item item = BuiltInRegistries.ITEM.get(loc);
			MunitionPropertiesSerializer<?> ser = ITEM_SERIALIZERS.get(item);
			ITEM_PROPELLANT.put(item, ser.fromNetwork(loc, buf));
		}
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundMunitionPropertiesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundMunitionPropertiesPacket(), server);
	}

	public record ClientboundMunitionPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundMunitionPropertiesPacket() { this(null); }

		public static ClientboundMunitionPropertiesPacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundMunitionPropertiesPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}
	}

}
