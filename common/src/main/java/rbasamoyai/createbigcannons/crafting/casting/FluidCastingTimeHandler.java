package rbasamoyai.createbigcannons.crafting.casting;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

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
import rbasamoyai.createbigcannons.base.tag_utils.FluidTypeDataHolder;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class FluidCastingTimeHandler {

	private static final FluidTypeDataHolder<Integer> CASTING_TIME = new FluidTypeDataHolder<>();

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
					CASTING_TIME.addTagData(tag, castingTime);
				} else {
					Fluid fluid = CBCRegistryUtils.getOptionalFluid(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown fluid type '" + loc + "'");
					});
					CASTING_TIME.addData(fluid, castingTime);
				}
			}
		}
	}

	public static void clear() { CASTING_TIME.cleanUp(); }

	public static void loadTags() { CASTING_TIME.loadTags(); }

	public static int getCastingTime(Fluid fluid) {
		Integer castingTime = CASTING_TIME.getData(fluid);
		return castingTime == null ? 1000 : castingTime;
	}

	public static void writeBuf(FriendlyByteBuf buf) {
		CASTING_TIME.writeToNetwork(buf, FriendlyByteBuf::writeVarInt);
	}

	public static void readBuf(FriendlyByteBuf buf) {
		CASTING_TIME.readFromNetwork(buf, FriendlyByteBuf::readVarInt);
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
