package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

public class TrafficConeBlockItem extends BlockItem implements Equipable {

	public TrafficConeBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@NotNull
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}

}
