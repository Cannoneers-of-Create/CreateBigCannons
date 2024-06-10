package rbasamoyai.createbigcannons.crafting.casting;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class FluidCastingTimeHandler {

	public static final Map<TagKey<Fluid>, Integer> TAGS_TO_LOAD = new Object2ObjectLinkedOpenHashMap<>();
	public static final Map<Fluid, Integer> FLUID_MAP = new Reference2ObjectOpenHashMap<>();
	public static final Map<Fluid, Integer> TAG_MAP = new Reference2ObjectOpenHashMap<>();

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		public ReloadListener() {
			super(GSON, "fluid_casting_time");
		}

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resources, ProfilerFiller profiler) {
			clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				JsonObject obj = el.getAsJsonObject();
				int castingTime = GsonHelper.getAsInt(obj, "casting_time", 1000);
				castingTime = Math.max(castingTime, 0);

				ResourceLocation loc = entry.getKey();
				if (loc.getPath().startsWith("tags/")) {
					TagKey<Fluid> tag = TagKey.create(CBCRegistryUtils.getFluidRegistryKey(), CBCUtils.location(loc.getNamespace(), loc.getPath().substring(5)));
					TAGS_TO_LOAD.put(tag, castingTime);
				} else {
					Fluid fluid = CBCRegistryUtils.getOptionalFluid(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown fluid type '" + loc + "'");
					});
					FLUID_MAP.put(fluid, castingTime);
				}
			}
		}
	}

	public static void clear() {
		FLUID_MAP.clear();
		TAG_MAP.clear();
		TAGS_TO_LOAD.clear();
	}

	public static void loadTags() {
		TAG_MAP.clear();
		for (Map.Entry<TagKey<Fluid>, Integer> entry : TAGS_TO_LOAD.entrySet()) {
			Integer hardness = entry.getValue();
			for (Holder<Fluid> holder : CBCRegistryUtils.getFluidTagEntries(entry.getKey())) {
				TAG_MAP.put(holder.value(), hardness);
			}
		}
		TAGS_TO_LOAD.clear();
	}

	public static int getCastingTime(Fluid fluid) {
		if (FLUID_MAP.containsKey(fluid)) return FLUID_MAP.get(fluid);
		if (TAG_MAP.containsKey(fluid)) return TAG_MAP.get(fluid);
		return 1000;
	}

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(FLUID_MAP.size());
		for (Map.Entry<Fluid, Integer> entry : FLUID_MAP.entrySet()) {
			buf.writeResourceLocation(CBCRegistryUtils.getFluidLocation(entry.getKey())).writeVarInt(entry.getValue());
		}
		buf.writeVarInt(TAG_MAP.size());
		for (Map.Entry<Fluid, Integer> entry : TAG_MAP.entrySet()) {
			buf.writeResourceLocation(CBCRegistryUtils.getFluidLocation(entry.getKey())).writeVarInt(entry.getValue());
		}
	}

	public static void readBuf(FriendlyByteBuf buf) {
		clear();
		int sz = buf.readVarInt();
		for (int i = 0; i < sz; ++i) {
			ResourceLocation id = buf.readResourceLocation();
			int castingTime = buf.readVarInt();
			Optional<Fluid> op = CBCRegistryUtils.getOptionalFluid(id);
			if (op.isEmpty()) continue;
			FLUID_MAP.put(op.get(), castingTime);
		}
		int sz1 = buf.readVarInt();
		for (int i = 0; i < sz1; ++i) {
			ResourceLocation id = buf.readResourceLocation();
			int castingTime = buf.readVarInt();
			Optional<Fluid> op = CBCRegistryUtils.getOptionalFluid(id);
			if (op.isEmpty()) continue;
			TAG_MAP.put(op.get(), castingTime);
		}
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundFluidCastingTimePacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundFluidCastingTimePacket(), server);
	}

	public record ClientboundFluidCastingTimePacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundFluidCastingTimePacket() { this(null); }

		public static ClientboundFluidCastingTimePacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundFluidCastingTimePacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}
	}

}
