package rbasamoyai.createbigcannons.cannon_control.config;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.CannonContraptionTypeRegistry;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

public class CannonMountPropertiesHandler {

	private static final Map<BlockEntityType<?>, Map<ICannonContraptionType, CannonMountBlockPropertiesProvider>> BLOCK_MOUNT_PROPERTIES = new Reference2ObjectOpenHashMap<>();
	private static final Map<EntityType<?>, Map<ICannonContraptionType, CannonMountEntityPropertiesProvider>> ENTITY_MOUNT_PROPERTIES = new Reference2ObjectOpenHashMap<>();

	private static final Map<BlockEntityType<?>, CannonMountBlockPropertiesSerializer<?>> BLOCK_MOUNT_SERIALIZERS = new Reference2ReferenceOpenHashMap<>();
	private static final Map<EntityType<?>, CannonMountEntityPropertiesSerializer> ENTITY_MOUNT_SERIALIZERS = new Reference2ReferenceOpenHashMap<>();

	private static final CannonMountBlockPropertiesProvider FALLBACK_BLOCK = new CannonMountBlockPropertiesProvider() {
		@Override public float maximumElevation(Level level, BlockState state, BlockPos pos) { return 0f; }
		@Override public float maximumDepression(Level level, BlockState state, BlockPos pos) { return 0f; }
	};

	private static final CannonMountEntityPropertiesProvider FALLBACK_ENTITY = new CannonMountEntityPropertiesProvider() {
		@Override public float maximumElevation(Entity entity) { return 0f; }
		@Override public float maximumDepression(Entity entity) { return 0f; }
	};

	public static class BlockEntityReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final BlockEntityReloadListener INSTANCE = new BlockEntityReloadListener();

		public BlockEntityReloadListener() { super(GSON, "cannon_mounts/block_entities"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			BLOCK_MOUNT_PROPERTIES.clear();
			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					String[] pathComponents = loc.getPath().split("/");
					ResourceLocation blockEntityLoc = new ResourceLocation(loc.getNamespace(), pathComponents[0]);
					ResourceLocation cannonTypeLoc = new ResourceLocation(pathComponents[1], pathComponents[2]);
					BlockEntityType<?> beType = Registry.BLOCK_ENTITY_TYPE.getOptional(blockEntityLoc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown block entity type '" + blockEntityLoc + "'");
					});
					ICannonContraptionType contraptionType = CannonContraptionTypeRegistry.getOptional(cannonTypeLoc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown cannon contraption type '" + cannonTypeLoc + "'");
					});
					if (!BLOCK_MOUNT_SERIALIZERS.containsKey(beType)) continue;
					CannonMountBlockPropertiesSerializer<?> ser = BLOCK_MOUNT_SERIALIZERS.get(beType);
					if (!BLOCK_MOUNT_PROPERTIES.containsKey(beType))
						BLOCK_MOUNT_PROPERTIES.put(beType, new Reference2ObjectOpenHashMap<>());
					Map<ICannonContraptionType, CannonMountBlockPropertiesProvider> mountMap = BLOCK_MOUNT_PROPERTIES.get(beType);
					mountMap.put(contraptionType, ser.fromJson(beType, contraptionType, el.getAsJsonObject()));
				} catch (Exception e) {

				}
			}
		}
	}

	public static class EntityReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final EntityReloadListener INSTANCE = new EntityReloadListener();

		public EntityReloadListener() { super(GSON, "cannon_mounts/entities"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			ENTITY_MOUNT_PROPERTIES.clear();
			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					String[] pathComponents = loc.getPath().split("/");
					ResourceLocation entityLoc = new ResourceLocation(loc.getNamespace(), pathComponents[0]);
					ResourceLocation cannonTypeLoc = new ResourceLocation(pathComponents[1], pathComponents[2]);
					EntityType<?> entityType = Registry.ENTITY_TYPE.getOptional(entityLoc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown entity type '" + entityLoc + "'");
					});
					ICannonContraptionType contraptionType = CannonContraptionTypeRegistry.getOptional(cannonTypeLoc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown cannon contraption type '" + cannonTypeLoc + "'");
					});
					if (!ENTITY_MOUNT_SERIALIZERS.containsKey(entityType)) continue;
					CannonMountEntityPropertiesSerializer ser = ENTITY_MOUNT_SERIALIZERS.get(entityType);
					if (!ENTITY_MOUNT_PROPERTIES.containsKey(entityType))
						ENTITY_MOUNT_PROPERTIES.put(entityType, new Reference2ObjectOpenHashMap<>());
					Map<ICannonContraptionType, CannonMountEntityPropertiesProvider> mountMap = ENTITY_MOUNT_PROPERTIES.get(entityType);
					mountMap.put(contraptionType, ser.fromJson(entityType, contraptionType, el.getAsJsonObject()));
				} catch (Exception e) {

				}
			}
		}
	}

	public static <T extends CannonMountBlockPropertiesSerializer<?>> T registerBlockMountSerializer(BlockEntityType<?> type, T ser) {
		if (BLOCK_MOUNT_SERIALIZERS.containsKey(type)) {
			throw new IllegalStateException("Serializer for block entity " + Registry.BLOCK_ENTITY_TYPE.getKey(type) + " already registered");
		}
		BLOCK_MOUNT_SERIALIZERS.put(type, ser);
		return ser;
	}

	public static <T extends CannonMountEntityPropertiesSerializer> T registerEntityMountSerializer(EntityType<?> type, T ser) {
		if (ENTITY_MOUNT_SERIALIZERS.containsKey(type)) {
			throw new IllegalStateException("Serializer for block entity " + Registry.ENTITY_TYPE.getKey(type) + " already registered");
		}
		ENTITY_MOUNT_SERIALIZERS.put(type, ser);
		return ser;
	}

	public static CannonMountBlockPropertiesProvider getProperties(BlockEntity be, ICannonContraptionType contraptionType) {
		return getProperties(be.getType(), contraptionType);
	}

	public static CannonMountBlockPropertiesProvider getProperties(BlockEntityType<?> beType, ICannonContraptionType contraptionType) {
		return BLOCK_MOUNT_PROPERTIES.containsKey(beType) ? BLOCK_MOUNT_PROPERTIES.get(beType).getOrDefault(contraptionType, FALLBACK_BLOCK) : FALLBACK_BLOCK;
	}

	public static CannonMountEntityPropertiesProvider getProperties(Entity entity, ICannonContraptionType contraptionType) {
		return getProperties(entity.getType(), contraptionType);
	}

	public static CannonMountEntityPropertiesProvider getProperties(EntityType<?> entityType, ICannonContraptionType contraptionType) {
		return ENTITY_MOUNT_PROPERTIES.containsKey(entityType) ? ENTITY_MOUNT_PROPERTIES.get(entityType).getOrDefault(contraptionType, FALLBACK_ENTITY) : FALLBACK_ENTITY;
	}

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(BLOCK_MOUNT_PROPERTIES.size());
		for (Map.Entry<BlockEntityType<?>, Map<ICannonContraptionType, CannonMountBlockPropertiesProvider>> entry : BLOCK_MOUNT_PROPERTIES.entrySet()) {
			buf.writeResourceLocation(Registry.BLOCK_ENTITY_TYPE.getKey(entry.getKey()));
			Map<ICannonContraptionType, CannonMountBlockPropertiesProvider> map = entry.getValue();
			buf.writeVarInt(map.size());
			for (Map.Entry<ICannonContraptionType, CannonMountBlockPropertiesProvider> entry1 : map.entrySet()) {
				buf.writeResourceLocation(CannonContraptionTypeRegistry.getKey(entry1.getKey()));
				toNetworkCasted(buf, entry.getKey(), entry1.getValue());
			}
		}
		buf.writeVarInt(ENTITY_MOUNT_PROPERTIES.size());
		for (Map.Entry<EntityType<?>, Map<ICannonContraptionType, CannonMountEntityPropertiesProvider>> entry : ENTITY_MOUNT_PROPERTIES.entrySet()) {
			buf.writeResourceLocation(Registry.ENTITY_TYPE.getKey(entry.getKey()));
			Map<ICannonContraptionType, CannonMountEntityPropertiesProvider> map = entry.getValue();
			buf.writeVarInt(map.size());
			for (Map.Entry<ICannonContraptionType, CannonMountEntityPropertiesProvider> entry1 : map.entrySet()) {
				buf.writeResourceLocation(CannonContraptionTypeRegistry.getKey(entry1.getKey()));
				toNetworkCasted(buf, entry.getKey(), entry1.getValue());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T extends CannonMountBlockPropertiesProvider> void toNetworkCasted(FriendlyByteBuf buf, BlockEntityType<?> beType, T properties) {
		CannonMountBlockPropertiesSerializer<T> ser = (CannonMountBlockPropertiesSerializer<T>) BLOCK_MOUNT_SERIALIZERS.get(beType);
		ser.toNetwork(properties, buf);
	}

	@SuppressWarnings("unchecked")
	private static <T extends CannonMountEntityPropertiesProvider> void toNetworkCasted(FriendlyByteBuf buf, EntityType<?> entityType, T properties) {
		CannonMountEntityPropertiesSerializer<T> ser = (CannonMountEntityPropertiesSerializer<T>) ENTITY_MOUNT_SERIALIZERS.get(entityType);
		ser.toNetwork(properties, buf);
	}

	public static void readBuf(FriendlyByteBuf buf) {
		BLOCK_MOUNT_PROPERTIES.clear();
		int blockSz = buf.readVarInt();
		for (int i = 0; i < blockSz; ++i) {
			ResourceLocation beTypeLoc = buf.readResourceLocation();
			BlockEntityType<?> beType = Registry.BLOCK_ENTITY_TYPE.get(beTypeLoc);
			CannonMountBlockPropertiesSerializer<?> ser = BLOCK_MOUNT_SERIALIZERS.get(beType);

			int mapSz = buf.readVarInt();
			Map<ICannonContraptionType, CannonMountBlockPropertiesProvider> map = new Reference2ObjectOpenHashMap<>();
			for (int j = 0; j < mapSz; ++j) {
				ResourceLocation contraptionTypeLoc = buf.readResourceLocation();
				ICannonContraptionType contraptionType = CannonContraptionTypeRegistry.get(contraptionTypeLoc);
				map.put(contraptionType, ser.fromNetwork(buf));
			}
			BLOCK_MOUNT_PROPERTIES.put(beType, map);
		}
		ENTITY_MOUNT_PROPERTIES.clear();
		int entitySz = buf.readVarInt();
		for (int i = 0; i < entitySz; ++i) {
			ResourceLocation entityTypeLoc = buf.readResourceLocation();
			EntityType<?> entityType = Registry.ENTITY_TYPE.get(entityTypeLoc);
			CannonMountEntityPropertiesSerializer<?> ser = ENTITY_MOUNT_SERIALIZERS.get(entityType);

			int mapSz = buf.readVarInt();
			Map<ICannonContraptionType, CannonMountEntityPropertiesProvider> map = new Reference2ObjectOpenHashMap<>();
			for (int j = 0; j < mapSz; ++j) {
				ResourceLocation contraptionTypeLoc = buf.readResourceLocation();
				ICannonContraptionType contraptionType = CannonContraptionTypeRegistry.get(contraptionTypeLoc);
				map.put(contraptionType, ser.fromNetwork(buf));
			}
			ENTITY_MOUNT_PROPERTIES.put(entityType, map);
		}
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundSyncCannonMountPropertiesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundSyncCannonMountPropertiesPacket(), server);
	}

	public record ClientboundSyncCannonMountPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundSyncCannonMountPropertiesPacket() { this(null); }

		public static ClientboundSyncCannonMountPropertiesPacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundSyncCannonMountPropertiesPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}

	}

}
