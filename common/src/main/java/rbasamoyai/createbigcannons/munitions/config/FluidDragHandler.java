package rbasamoyai.createbigcannons.munitions.config;

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
import net.minecraft.world.level.material.FluidState;
import rbasamoyai.createbigcannons.base.tag_utils.FluidTypeDataHolder;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class FluidDragHandler {

	private static final FluidTypeDataHolder<Double> FLUID_DRAG = new FluidTypeDataHolder<>();

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		ReloadListener() { super(GSON, "fluid_drag"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			FLUID_DRAG.cleanUp();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				JsonObject obj = el.getAsJsonObject();
				double fluidDrag = Math.max(GsonHelper.getAsDouble(obj, "fluid_drag", 0), 0);

				ResourceLocation loc = entry.getKey();
				if (loc.getPath().startsWith("tags/")) {
					TagKey<Fluid> tag = TagKey.create(CBCRegistryUtils.getFluidRegistryKey(), CBCUtils.location(loc.getNamespace(), loc.getPath().substring(5)));
					FLUID_DRAG.addTagData(tag, fluidDrag);
				} else {
					Fluid fluid = CBCRegistryUtils.getOptionalFluid(loc).orElseThrow(() -> {
						return new JsonSyntaxException("Unknown fluid type '" + loc + "'");
					});
					FLUID_DRAG.addData(fluid, fluidDrag);
				}
			}
		}
	}

	public static void loadTags() { FLUID_DRAG.loadTags(); }

	public static double getFluidDrag(FluidState fluidState) { return getFluidDrag(fluidState.getType()); }

	public static double getFluidDrag(Fluid fluid) {
		Double value = FLUID_DRAG.getData(fluid);
		return value == null ? 0 : value;
	}

	public static void writeBuf(FriendlyByteBuf buf) {
		FLUID_DRAG.writeToNetwork(buf, FriendlyByteBuf::writeDouble);
	}

	public static void readBuf(FriendlyByteBuf buf) {
		FLUID_DRAG.readFromNetwork(buf, FriendlyByteBuf::readDouble);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundFluidDragPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundFluidDragPacket(), server);
	}

	public record ClientboundFluidDragPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundFluidDragPacket() { this(null); }

		public static ClientboundFluidDragPacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundFluidDragPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			if (this.buf != null)
				readBuf(this.buf);
		}
	}

}
