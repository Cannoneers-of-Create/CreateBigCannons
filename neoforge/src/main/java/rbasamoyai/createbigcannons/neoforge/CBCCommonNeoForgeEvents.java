package rbasamoyai.createbigcannons.neoforge;

import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import rbasamoyai.createbigcannons.CBCCommonEvents;
import rbasamoyai.createbigcannons.crafting.welding.CannonWelderItem;

public class CBCCommonNeoForgeEvents {

	public static void register(IEventBus forgeEventBus) {
		forgeEventBus.addListener(CBCCommonNeoForgeEvents::onPlayerBreakBlock);
		forgeEventBus.addListener(CBCCommonNeoForgeEvents::onPlayerLogin);
		forgeEventBus.addListener(CBCCommonNeoForgeEvents::onPlayerLogout);
		forgeEventBus.addListener(CBCCommonNeoForgeEvents::onLoadWorld);
		forgeEventBus.addListener(CBCCommonNeoForgeEvents::onServerWorldTick);
		forgeEventBus.addListener(CBCCommonNeoForgeEvents::onDatapackSync);
		forgeEventBus.addListener(CBCCommonNeoForgeEvents::onAddReloadListeners);
		forgeEventBus.addListener(CBCCommonNeoForgeEvents::onDeployerRecipeSearch);
		forgeEventBus.addListener(CBCCommonNeoForgeEvents::onUseItemOnBlock);
	}

	public static void onServerWorldTick(LevelTickEvent.Post evt) {
		if (evt.getLevel().isClientSide)
			return;
		CBCCommonEvents.serverLevelTickEnd(evt.getLevel());
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

	public static void onUseItemOnBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getItemStack().getItem() instanceof CannonWelderItem
			&& CannonWelderItem.welderItemAlwaysPlacesWhenUsed(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec()) == InteractionResult.FAIL)
			event.setUseBlock(TriState.FALSE); // TODO c6 playtest
	}

}
