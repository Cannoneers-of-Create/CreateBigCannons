package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class TrafficConeBlockItem extends BlockItem implements Wearable {

	public TrafficConeBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack itemStack = player.getItemInHand(usedHand);
		ItemStack itemStack2 = player.getItemBySlot(EquipmentSlot.HEAD);
		if (itemStack2.isEmpty()) {
			player.setItemSlot(EquipmentSlot.HEAD, itemStack.copy());
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(this));
			}

			itemStack.setCount(0);
			return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
		} else {
			return InteractionResultHolder.fail(itemStack);
		}
	}

}
