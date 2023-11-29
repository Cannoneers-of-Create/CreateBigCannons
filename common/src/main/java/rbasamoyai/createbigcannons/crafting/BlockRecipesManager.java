package rbasamoyai.createbigcannons.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

public class BlockRecipesManager {

	private static final Map<ResourceLocation, BlockRecipe> BLOCK_RECIPES_BY_NAME = new HashMap<>();
	private static final Map<BlockRecipeType<?>, Map<ResourceLocation, BlockRecipe>> BLOCK_RECIPES_BY_TYPE = new HashMap<>();

	public static Collection<BlockRecipe> getRecipes() {
		return BLOCK_RECIPES_BY_NAME.values();
	}

	public static Collection<BlockRecipe> getRecipesOfType(BlockRecipeType<?> type) {
		return BLOCK_RECIPES_BY_TYPE.getOrDefault(type, new HashMap<>()).values();
	}

	public static void clear() {
		BLOCK_RECIPES_BY_NAME.clear();
		BLOCK_RECIPES_BY_TYPE.clear();
	}

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(BLOCK_RECIPES_BY_NAME.size());
		for (Map.Entry<ResourceLocation, BlockRecipe> entry : BLOCK_RECIPES_BY_NAME.entrySet()) {
			buf.writeResourceLocation(entry.getKey());
			toNetworkCasted(buf, entry.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends BlockRecipe> void toNetworkCasted(FriendlyByteBuf buf, T recipe) {
		BlockRecipeSerializer<T> ser = (BlockRecipeSerializer<T>) recipe.getSerializer();
		buf.writeResourceLocation(CBCRegistries.blockRecipeSerializers().getKey(ser));
		ser.toNetwork(buf, recipe);
	}

	public static void readBuf(FriendlyByteBuf buf) {
		clear();
		int sz = buf.readVarInt();
		Registry<BlockRecipeSerializer<?>> serializersRegistry = CBCRegistries.blockRecipeSerializers();
		Registry<BlockRecipeType<?>> typeRegistry = CBCRegistries.blockRecipeTypes();
		for (int i = 0; i < sz; ++i) {
			ResourceLocation id = buf.readResourceLocation();
			ResourceLocation type = buf.readResourceLocation();
			BlockRecipe recipe = serializersRegistry.get(type).fromNetwork(id, buf);
			BLOCK_RECIPES_BY_NAME.put(id, recipe);
			BlockRecipeType<?> recipeType = typeRegistry.get(type);
			if (!BLOCK_RECIPES_BY_TYPE.containsKey(recipeType)) {
				BLOCK_RECIPES_BY_TYPE.put(recipeType, new HashMap<>());
			}
			BLOCK_RECIPES_BY_TYPE.get(recipeType).put(id, recipe);
		}
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundRecipesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundRecipesPacket(), server);
	}

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		public ReloadListener() {
			super(GSON, "block_recipes");
		}

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resources,
			ProfilerFiller profiler) {
			clear();

			Registry<BlockRecipeSerializer<?>> serializersRegistry = CBCRegistries.blockRecipeSerializers();
			Registry<BlockRecipeType<?>> typeRegistry = CBCRegistries.blockRecipeTypes();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (el.isJsonObject()) {
					ResourceLocation id = entry.getKey();
					JsonObject obj = el.getAsJsonObject();
					ResourceLocation type = new ResourceLocation(obj.get("type").getAsString());
					BlockRecipe recipe = serializersRegistry.get(type).fromJson(id, obj);
					BLOCK_RECIPES_BY_NAME.put(id, recipe);
					BlockRecipeType<?> recipeType = typeRegistry.get(type);
					if (!BLOCK_RECIPES_BY_TYPE.containsKey(recipeType)) {
						BLOCK_RECIPES_BY_TYPE.put(recipeType, new HashMap<>());
					}
					BLOCK_RECIPES_BY_TYPE.get(recipeType).put(id, recipe);
				}
			}
		}
	}

	public static class ClientboundRecipesPacket implements RootPacket {
		private FriendlyByteBuf buf;

		public ClientboundRecipesPacket() {
		}

		public ClientboundRecipesPacket(FriendlyByteBuf buf) {
			this.buf = new FriendlyByteBuf(buf.copy());
		}

		@Override
		public void rootEncode(FriendlyByteBuf buf) {
			writeBuf(buf);
		}

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			readBuf(this.buf);
		}
	}

}
