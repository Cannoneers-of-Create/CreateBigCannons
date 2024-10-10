package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.utility.Components;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ProjectileBlockItem extends BlockItem {

	public ProjectileBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CompoundTag tag = stack.getOrCreateTag();
		ItemStack tracer = ItemStack.of(tag.getCompound("BlockEntityTag").getCompound("Tracer"));
		if (!tracer.isEmpty())
			tooltip.add(Components.translatable("tooltip.createbigcannons.tracer"));
	}

}
