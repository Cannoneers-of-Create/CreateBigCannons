package rbasamoyai.createbigcannons.block_armor_properties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCItems;

public class BlockArmorInspectionToolItem extends Item {

	private static final List<Predicate<Player>> IS_HOLDING_PREDICATES = new ArrayList<>();

	public BlockArmorInspectionToolItem(Properties properties) { super(properties); }

	@Override public boolean isFoil(ItemStack stack) { return true; }

	public static boolean isHoldingTool(Player player) {
		for (Predicate<Player> predicate : IS_HOLDING_PREDICATES) {
			if (predicate.test(player))
				return true;
		}
		return false;
	}

	public static void addBlockArmorInfo(List<Component> tooltip, Level level, BlockPos pos, BlockState blockState) {
		if (blockState.isAir())
			return;
		String precision = CBCConfigs.CLIENT.blockArmorTooltipPrecision.get().toString();
		String format = "%." + precision + "f";
		BlockArmorPropertiesProvider provider = BlockArmorPropertiesHandler.getProperties(blockState);
		double toughness = provider.toughness(level, blockState, pos, true);
		double hardness = provider.hardness(level, blockState, pos, true);
		Lang.builder()
			.add(Components.translatable("debug.createbigcannons.block_armor_info"))
			.forGoggles(tooltip);
		Lang.builder()
			.add(Components.translatable("debug.createbigcannons.block_toughness").withStyle(ChatFormatting.GRAY))
			.add(Components.literal(String.format(format, toughness)).withStyle(ChatFormatting.GOLD))
			.forGoggles(tooltip, 1);
		Lang.builder()
			.add(Components.translatable("debug.createbigcannons.block_hardness").withStyle(ChatFormatting.GRAY))
			.add(Components.literal(String.format(format, hardness)).withStyle(ChatFormatting.GOLD))
			.forGoggles(tooltip, 1);
	}

	/**
	 * Use this method to add custom entry points to the block armor info overlay, e.g. custom
	 * armor, handheld alternatives, etc.
	 *
	 * <br>Adapted from {@link com.simibubi.create.content.equipment.goggles.GogglesItem#addIsWearingPredicate(Predicate)}.
	 */
	public static void addIsHoldingPredicate(Predicate<Player> predicate) { IS_HOLDING_PREDICATES.add(predicate); }

	public static void registerDefaultHandlers() {
		addIsHoldingPredicate(BlockArmorInspectionToolItem::defaultHandler);
	}

	private static boolean defaultHandler(Player player) {
		return CBCItems.BLOCK_ARMOR_INSPECTION_TOOL.isIn(player.getMainHandItem()) || CBCItems.BLOCK_ARMOR_INSPECTION_TOOL.isIn(player.getOffhandItem());
	}

}
