package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import javax.annotation.Nullable;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class TrafficConeBlockItem extends BlockItem implements Equipable {

	public TrafficConeBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack itemStack = player.getItemInHand(usedHand);
		ItemStack itemStack2 = player.getItemBySlot(EquipmentSlot.HEAD);
		if (itemStack2.isEmpty()) {
			ItemStack copy = itemStack.copy();
			copy.setCount(1);
			player.setItemSlot(EquipmentSlot.HEAD, copy);
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(this));
			}
			itemStack.shrink(1);
			return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
		} else {
			return InteractionResultHolder.fail(itemStack);
		}
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}

	@Nullable
	@Override
	public SoundEvent getEquipSound() {
		return SoundEvents.ARMOR_EQUIP_GENERIC;
	}

}
