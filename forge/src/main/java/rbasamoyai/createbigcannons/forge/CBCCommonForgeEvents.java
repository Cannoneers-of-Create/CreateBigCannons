package rbasamoyai.createbigcannons.forge;

import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import rbasamoyai.createbigcannons.base.CBCCommonEvents;

public class CBCCommonForgeEvents {

	public static void register(IEventBus forgeEventBus) {
		forgeEventBus.addListener(CBCCommonForgeEvents::onPlayerBreakBlock);
		forgeEventBus.addListener(CBCCommonForgeEvents::onPlayerLogin);
		forgeEventBus.addListener(CBCCommonForgeEvents::onPlayerLogout);
		forgeEventBus.addListener(CBCCommonForgeEvents::onLoadWorld);
		forgeEventBus.addListener(CBCCommonForgeEvents::onServerWorldTick);
		forgeEventBus.addListener(CBCCommonForgeEvents::onDatapackSync);
		forgeEventBus.addListener(CBCCommonForgeEvents::onAddReloadListeners);
		forgeEventBus.addListener(CBCCommonForgeEvents::onDeployerRecipeSearch);
	}

	public static void onServerWorldTick(TickEvent.LevelTickEvent evt) {
		if (evt.phase == TickEvent.Phase.START) {
			return;
		}
		if (evt.side == LogicalSide.CLIENT) {
			return;
		}
		CBCCommonEvents.serverLevelTickEnd(evt.level);
	}

	public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
		CBCCommonEvents.onPlayerBreakBlock(event.getState(), event.getLevel(), event.getPos(), event.getPlayer());
	}

	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent evt) {
		if (evt.getEntity() instanceof ServerPlayer player) {
			CBCCommonEvents.onPlayerLogin(player);
		}
	}

	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent evt) {
		CBCCommonEvents.onPlayerLogout(evt.getEntity());
	}

	public static void onLoadWorld(LevelEvent.Load evt) {
		CBCCommonEvents.onLoadLevel(evt.getLevel());
	}

	public static void onDatapackSync(OnDatapackSyncEvent evt) {
		if (evt.getPlayer() == null) {
			CBCCommonEvents.onDatapackReload(evt.getPlayerList().getServer());
		} else {
			CBCCommonEvents.onDatapackSync(evt.getPlayer());
		}
	}

	public static void onAddReloadListeners(AddReloadListenerEvent event) {
		CBCCommonEvents.onAddReloadListeners((m, l) -> event.addListener(m));
	}

	public static void onDeployerRecipeSearch(DeployerRecipeSearchEvent evt) {
		CBCCommonEvents.onAddDeployerRecipes(evt.getBlockEntity(), evt.getInventory(), evt::addRecipe);
	}

}
