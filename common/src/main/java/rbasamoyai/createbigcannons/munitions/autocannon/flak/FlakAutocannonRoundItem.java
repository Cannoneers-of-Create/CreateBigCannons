package rbasamoyai.createbigcannons.munitions.autocannon.flak;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FlakAutocannonRoundItem extends AutocannonRoundItem implements FuzedItemMunition {

	public FlakAutocannonRoundItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractAutocannonProjectile<?> getAutocannonProjectile(ItemStack stack, Level level) {
		FlakAutocannonProjectile projectile = CBCEntityTypes.FLAK_AUTOCANNON.create(level);
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("Fuze", Tag.TAG_COMPOUND)) {
			projectile.setFuze(ItemStack.of(tag.getCompound("Fuze")));
		}
		return projectile;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CompoundTag tag = stack.getOrCreateTag();
		ItemStack fuze = tag.contains("Fuze", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Fuze")) : ItemStack.EMPTY;
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

	@Override
	public EntityType<? extends PropertiesMunitionEntity<? extends AutocannonProjectileProperties>> getEntityType(ItemStack stack) {
		return CBCEntityTypes.FLAK_AUTOCANNON.get();
	}

}
