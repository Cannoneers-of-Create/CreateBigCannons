package rbasamoyai.createbigcannons.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

public class BlockRecipesManager {

	private static final Map<ResourceLocation, BlockRecipe> BLOCK_RECIPES_BY_NAME = new Object2ObjectOpenHashMap<>();
	private static final Map<BlockRecipeType<?>, Map<ResourceLocation, BlockRecipe>> BLOCK_RECIPES_BY_TYPE = new Reference2ObjectOpenHashMap<>();

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

	public static void writeBuf(RegistryFriendlyByteBuf buf, ClientboundBlockRecipesPacket pkt) {
		buf.writeVarInt(pkt.blockRecipes.size());
		for (Map.Entry<ResourceLocation, BlockRecipe> entry : pkt.blockRecipes.entrySet()) {
			buf.writeResourceLocation(entry.getKey());
            BlockRecipe.STREAM_CODEC.encode(buf, entry.getValue()); // TODO c6 playtest
		}
	}

	public static ClientboundBlockRecipesPacket readBuf(RegistryFriendlyByteBuf buf) {
		int sz = buf.readVarInt();
        Map<ResourceLocation, BlockRecipe> blockRecipes = new Object2ObjectOpenHashMap<>();
		for (int i = 0; i < sz; ++i) {
			ResourceLocation id = buf.readResourceLocation();
            BlockRecipe recipe = BlockRecipe.STREAM_CODEC.decode(buf);
			blockRecipes.put(id, recipe);
		}
        return new ClientboundBlockRecipesPacket(blockRecipes);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundBlockRecipesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundBlockRecipesPacket(), server);
	}

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();

        private final RegistryAccess registryAccess;

		public ReloadListener(RegistryAccess registryAccess) {
            super(GSON, "createbigcannons/block_recipes");
            this.registryAccess = registryAccess;
        }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resources, ProfilerFiller profiler) {
			clear();

            RegistryOps<JsonElement> registryOps = IndexPlatform.makeRegistryOps(this.registryAccess, this);

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
                ResourceLocation id = entry.getKey();
                try {
                    BlockRecipe recipe = BlockRecipe.CODEC.parse(registryOps, el).getOrThrow(JsonParseException::new);
                    BLOCK_RECIPES_BY_NAME.put(id, recipe);
                    BlockRecipeType<?> recipeType = recipe.getType();
                    if (!BLOCK_RECIPES_BY_TYPE.containsKey(recipeType)) {
                        BLOCK_RECIPES_BY_TYPE.put(recipeType, new HashMap<>());
                    }
                    BLOCK_RECIPES_BY_TYPE.get(recipeType).put(id, recipe);
                } catch (Exception e) {
                    CreateBigCannons.LOGGER.warn("Exception loading block recipe {}: {}", id, e.getMessage());
                }
			}
		}
	}

    // TODO c6 playtest
	public record ClientboundBlockRecipesPacket(Map<ResourceLocation, BlockRecipe> blockRecipes) implements RootPacket {
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBlockRecipesPacket> STREAM_CODEC =
            StreamCodec.of(BlockRecipesManager::writeBuf, BlockRecipesManager::readBuf);

		public ClientboundBlockRecipesPacket() { this(new Object2ObjectOpenHashMap<>(BLOCK_RECIPES_BY_NAME)); }

		@Override
        public void handle(Executor exec, PacketListener listener, Player player) {
            clear();
            for (Map.Entry<ResourceLocation, BlockRecipe> e : this.blockRecipes.entrySet()) {
                ResourceLocation id = e.getKey();
                BlockRecipe recipe = e.getValue();
                BLOCK_RECIPES_BY_NAME.put(id, recipe);
                BlockRecipeType<?> recipeType = recipe.getType();
                if (!BLOCK_RECIPES_BY_TYPE.containsKey(recipeType))
                    BLOCK_RECIPES_BY_TYPE.put(recipeType, new Object2ObjectOpenHashMap<>());
                BLOCK_RECIPES_BY_TYPE.get(recipeType).put(id, recipe);
            }
        }
	}

}
