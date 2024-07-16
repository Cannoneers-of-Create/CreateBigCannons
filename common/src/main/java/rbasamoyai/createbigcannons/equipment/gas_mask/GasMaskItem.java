package rbasamoyai.createbigcannons.equipment.gas_mask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import rbasamoyai.createbigcannons.CBCTags.CBCItemTags;
import rbasamoyai.createbigcannons.index.CBCItems;

public class GasMaskItem extends Item {

	private static final List<Predicate<LivingEntity>> IS_WEARING_PREDICATES = new ArrayList<>();
	private static final List<Predicate<Player>> OVERLAY_DISPLAY_PREDICATES = new ArrayList<>();

	public GasMaskItem(Properties properties) {
        super(properties);
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack itemStack = player.getItemInHand(usedHand);
		ItemStack itemStack2 = player.getItemBySlot(EquipmentSlot.HEAD);
		if (itemStack2.isEmpty()) {
			player.setItemSlot(EquipmentSlot.HEAD, itemStack.copy());
			if (!level.isClientSide())
				player.awardStat(Stats.ITEM_USED.get(this));
			itemStack.setCount(0);
			return InteractionResultHolder.success(itemStack);
		} else {
			return InteractionResultHolder.fail(itemStack);
		}
	}

	public static boolean isWearingWorkingMask(LivingEntity entity) {
		for (Predicate<LivingEntity> predicate : IS_WEARING_PREDICATES) {
			if (predicate.test(entity))
				return true;
		}
		return false;
	}

	public static boolean canShowGasMaskOverlay(Player player) {
		for (Predicate<Player> predicate : OVERLAY_DISPLAY_PREDICATES) {
			if (predicate.test(player))
				return true;
		}
		return false;
	}

	/**
	 * Use this method to add custom entry points to the gas mask test, e.g. custom
	 * armor, handheld alternatives, etc.
	 *
	 * <br>Adapted from {@link com.simibubi.create.content.equipment.goggles.GogglesItem#addIsWearingPredicate(Predicate)}.
	 */
	public static void addIsWearingPredicate(Predicate<LivingEntity> predicate) { IS_WEARING_PREDICATES.add(predicate); }

	/**
	 * For internal use by Create Big Cannons only. The Gas Mask screen overlay
	 * cannot be changed by a mod-to-mod basis.
	 */
	public static void addOverlayDisplayPredicate(Predicate<Player> predicate) { OVERLAY_DISPLAY_PREDICATES.add(predicate); }

	public static void registerDefaultHandlers() {
		addIsWearingPredicate(GasMaskItem::defaultHandler);
		addOverlayDisplayPredicate(GasMaskItem::defaultOverlayHandler);
	}

	private static boolean defaultHandler(LivingEntity entity) {
		return entity.getItemBySlot(EquipmentSlot.HEAD).is(CBCItemTags.GAS_MASKS);
	}

	private static boolean defaultOverlayHandler(Player player) {
		return player.getItemBySlot(EquipmentSlot.HEAD).is(CBCItems.GAS_MASK.get());
	}

}
