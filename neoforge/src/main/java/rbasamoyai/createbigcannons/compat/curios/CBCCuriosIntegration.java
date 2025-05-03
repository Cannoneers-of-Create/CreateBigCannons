package rbasamoyai.createbigcannons.compat.curios;

import java.util.Map;
import java.util.Optional;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import rbasamoyai.createbigcannons.CBCTags.CBCItemTags;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskItem;
import rbasamoyai.createbigcannons.index.CBCItems;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

// Adapted from Create's Curios compat --ritchie
public class CBCCuriosIntegration {

	/**
	 * Resolves the Stacks Handler Map given an Entity.
	 * It is recommended to then use a `.map(curiosMap -> curiosMap.get({key})`,
	 * which can be null and would therefore be caught by the Optional::map function.
	 *
	 * @param entity The entity which possibly has a Curio Inventory capability
	 * @return An optional of the Stacks Handler Map
	 */
	private static Optional<Map<String, ICurioStacksHandler>> resolveCuriosMap(LivingEntity entity) {
		return entity.getCapability(CuriosCapability.INVENTORY).map(ICuriosItemHandler::getCurios);
	}

	public static void init(IEventBus modBus, IEventBus forgeBus) {
		modBus.addListener(CBCCuriosIntegration::onCommonSetup);
		modBus.addListener(CBCCuriosIntegration::onInterModEnqueue);
	}

	private static void onCommonSetup(FMLCommonSetupEvent event) {
		GasMaskItem.addIsWearingPredicate(player -> resolveCuriosMap(player)
			.map(curiosMap -> curiosMap.get("head"))
			.map(stacksHandler -> {
				IDynamicStackHandler stacks = stacksHandler.getStacks();
				int slots = stacksHandler.getSlots();
				for (int slot = 0; slot < slots; slot++) {
					if (stacks.getStackInSlot(slot).is(CBCItemTags.GAS_MASKS))
						return true;
				}
				return false;
			})
			.orElse(false));

		GasMaskItem.addOverlayDisplayPredicate(player -> resolveCuriosMap(player)
			.map(curiosMap -> curiosMap.get("head"))
			.map(stacksHandler -> {
				IDynamicStackHandler stacks = stacksHandler.getStacks();
				int slots = stacksHandler.getSlots();
				for (int slot = 0; slot < slots; slot++) {
					if (stacks.getStackInSlot(slot).is(CBCItems.GAS_MASK.get()))
						return true;
				}
				return false;
			})
			.orElse(false));
	}

	private static void onInterModEnqueue(InterModEnqueueEvent event) {
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder()
			.size(2).build());
	}

}
