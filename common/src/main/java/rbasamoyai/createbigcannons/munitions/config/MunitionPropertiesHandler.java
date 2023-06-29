package rbasamoyai.createbigcannons.munitions.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class MunitionPropertiesHandler {

    public static Map<EntityType<?>, MunitionProperties> PROJECTILES = new HashMap<>();
    private static final MunitionProperties DEFAULT = new MunitionProperties(0, 0, 0,
		true, false, false, null);

    public static class ReloadListener extends SimpleJsonResourceReloadListener {

        private static final Gson GSON = new Gson();

        public static final ReloadListener INSTANCE = new ReloadListener();

        protected ReloadListener() {
            super(GSON, "munition_properties");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
            PROJECTILES.clear();

            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                JsonElement element = entry.getValue();
                if (element.isJsonObject()) {
                    try {
                        for (Map.Entry<String, JsonElement> jsonEntry : element.getAsJsonObject().entrySet()) {
                            if (jsonEntry.getValue().isJsonObject()) {
                                String s = jsonEntry.getKey();
								EntityType<?> type = Registry.ENTITY_TYPE.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
									return new JsonSyntaxException("Unknown entity type '" + jsonEntry.getKey() + "'");
								});
                                MunitionProperties properties = MunitionProperties.fromJson(jsonEntry.getValue().getAsJsonObject(), s);
                                PROJECTILES.put(type, properties);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
			int x = 0;
        }
    }

	public static MunitionProperties getProperties(EntityType<?> type) { return PROJECTILES.getOrDefault(type, DEFAULT); }
    public static MunitionProperties getProperties(Entity entity) { return getProperties(entity.getType()); }

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(PROJECTILES.size());
		for (Map.Entry<EntityType<?>, MunitionProperties> entry : PROJECTILES.entrySet()) {
			buf.writeUtf(Registry.ENTITY_TYPE.getKey(entry.getKey()).toString());
			entry.getValue().writeBuf(buf);
		}
	}

	public static void readBuf(FriendlyByteBuf buf) {
		PROJECTILES.clear();
		int sz = buf.readVarInt();

		for (int i = 0; i < sz; ++i) {
			PROJECTILES.put(Registry.ENTITY_TYPE.get(new ResourceLocation(buf.readUtf())), MunitionProperties.readBuf(buf));
		}
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundMunitionPropertiesPacket(), player);
	}

	public record ClientboundMunitionPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundMunitionPropertiesPacket() { this(null); }

		public static ClientboundMunitionPropertiesPacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundMunitionPropertiesPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @org.jetbrains.annotations.Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}
	}

}
