package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
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
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
		CompoundTag tag = (CompoundTag) stack.saveOptional(Minecraft.getInstance().level.registryAccess());
		ItemStack tracer = ItemStack.of(tag.getCompound("BlockEntityTag").getCompound("Tracer"));
		if (!tracer.isEmpty())
			tooltip.add(Component.translatable("tooltip.createbigcannons.tracer"));
	}

}
