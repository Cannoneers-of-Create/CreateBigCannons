package rbasamoyai.createbigcannons.fabric;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;

import io.github.fabricators_of_create.porting_lib.event.common.ModsLoadedCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.CBCCommonEvents;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorInspectionToolItem;
import rbasamoyai.createbigcannons.cannon_control.config.DefaultCannonMountPropertiesSerializers;
import rbasamoyai.createbigcannons.compat.copycats.CopycatsCompat;
import rbasamoyai.createbigcannons.compat.create.DefaultCreateCompat;
import rbasamoyai.createbigcannons.compat.trinkets.CBCTrinketsIntegration;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskItem;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class CBCCommonFabricEvents {

	public static void register() {
		ServerTickEvents.END_WORLD_TICK.register(CBCCommonFabricEvents::onServerLevelTick);
		ServerPlayConnectionEvents.JOIN.register(CBCCommonFabricEvents::onPlayerLogin);
		ServerPlayConnectionEvents.DISCONNECT.register(CBCCommonFabricEvents::onPlayerLogout);
		ServerWorldEvents.LOAD.register(CBCCommonFabricEvents::onLoadLevel);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(CBCCommonFabricEvents::onDatapackReload);
		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(CBCCommonFabricEvents::onDatapackSync);
		PlayerBlockBreakEvents.AFTER.register(CBCCommonFabricEvents::onPlayerBreakBlock);
		DeployerRecipeSearchEvent.EVENT.register(CBCCommonFabricEvents::onDeployerRecipeSearch);
		UseBlockCallback.EVENT.register(CBCCommonFabricEvents::onUseItemOnBlock);
		ModsLoadedCallback.EVENT.register(CBCCommonFabricEvents::onModsLoaded);

		CBCCommonEvents.onAddReloadListeners(CBCCommonFabricEvents::wrapAndRegisterReloadListener);

		FlammableBlockRegistry FLAMMABLE_REGISTRY = FlammableBlockRegistry.getDefaultInstance();
		FLAMMABLE_REGISTRY.add(CBCBlocks.POWDER_CHARGE.get(), 100, 30);
		//FLAMMABLE_REGISTRY.add(CBCBlocks.BIG_CARTRIDGE.get(), 100, 8); TODO: if flammable block registry or some other thing for dynamic spread...
	}

	public static void onModsLoaded(EnvType type) {
		BlockArmorInspectionToolItem.registerDefaultHandlers();
		GasMaskItem.registerDefaultHandlers();
		DefaultCreateCompat.init();
		DefaultCannonMountPropertiesSerializers.init();
		CBCModsFabric.COPYCATS.executeIfInstalled(() -> () -> CopycatsCompat.init(CBCModsFabric.COPYCATS::getBlock));
		CBCModsFabric.TRINKETS.executeIfInstalled(() -> () -> CBCTrinketsIntegration.init());
	}

	public static void onServerLevelTick(ServerLevel level) {
		CBCCommonEvents.serverLevelTickEnd(level);
	}

	public static void onPlayerBreakBlock(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
		CBCCommonEvents.onPlayerBreakBlock(state, level, pos, player);
	}

	public static void onPlayerLogin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
		CBCCommonEvents.onPlayerLogin(handler.getPlayer());
	}

	public static void onPlayerLogout(ServerGamePacketListenerImpl handler, MinecraftServer server) {
		CBCCommonEvents.onPlayerLogout(handler.getPlayer());
	}

	public static void onLoadLevel(MinecraftServer server, ServerLevel level) {
		CBCCommonEvents.onLoadLevel(level);
	}

	public static void onDatapackReload(MinecraftServer server, CloseableResourceManager resourceManager, boolean success) {
		CBCCommonEvents.onDatapackReload(server);
	}

	public static void onDatapackSync(ServerPlayer player, boolean joined) {
		CBCCommonEvents.onDatapackSync(player);
	}

	public static void wrapAndRegisterReloadListener(PreparableReloadListener base, ResourceLocation location) {
		IdentifiableResourceReloadListener listener = new IdentifiableResourceReloadListener() {
			@Override public ResourceLocation getFabricId() { return location; }

			@Override
			public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
				return base.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
			}
		};

		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(listener);
	}

	public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent evt) {
		CBCCommonEvents.onAddDeployerRecipes(evt.getBlockEntity(), evt.getInventory(), evt::addRecipe);
	}

	public static InteractionResult onUseItemOnBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
		return CBCCommonEvents.onUseItemOnBlock(player, level, hand, hitResult);
	}

}
