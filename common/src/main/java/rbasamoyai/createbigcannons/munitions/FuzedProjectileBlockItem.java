package rbasamoyai.createbigcannons.munitions;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FuzedProjectileBlockItem extends ProjectileBlockItem {

	public FuzedProjectileBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CompoundTag tag = stack.getOrCreateTag();
		ItemStack fuze = ItemStack.of(tag.getCompound("BlockEntityTag").getCompound("Fuze"));
		if (!fuze.isEmpty()) {
			Lang.builder("block")
				.translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze")
				.add(Components.literal(" "))
				.add(fuze.getDisplayName().copy())
				.addTo(tooltip);
			if (fuze.getItem() instanceof FuzeItem) {
				List<Component> subTooltip = new ArrayList<>();
				fuze.getItem().appendHoverText(fuze, level, subTooltip, flag);
				subTooltip.replaceAll(sibling -> Components.literal("  ").append(sibling).withStyle(ChatFormatting.GRAY));
				tooltip.addAll(subTooltip);
			}
		}
	}

}
