package rbasamoyai.createbigcannons.munitions.autocannon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

public class AutocannonCartridgeItem extends Item implements AutocannonAmmoItem {

	public AutocannonCartridgeItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
		ItemStack round = getProjectileStack(stack);
		if (!round.isEmpty()) {
			tooltip.add(Component.translatable("item.minecraft.crossbow.projectile").append(" ").append(round.getDisplayName()));
			if (round.getItem() instanceof AutocannonRoundItem) {
				List<Component> subTooltip = new ArrayList<>();
				round.getItem().appendHoverText(round, ctx, subTooltip, flag);
				for (int i = 0; i < subTooltip.size(); ++i) {
					subTooltip.set(i, Component.literal("  ").append(subTooltip.get(i)).withStyle(ChatFormatting.GRAY));
				}
				tooltip.addAll(subTooltip);
			}
		}
	}

	@Override
	public ItemStack getSpentItem(ItemStack stack) {
		return CBCItems.EMPTY_AUTOCANNON_CARTRIDGE.asStack();
	}

	@Override
	public AutocannonAmmoType getType() {
		return AutocannonAmmoType.AUTOCANNON;
	}

	@Override
	@Nullable
	public AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
		ItemStack projectileStack = getProjectileStack(stack);
		return projectileStack.getItem() instanceof AutocannonRoundItem projectileItem ? projectileItem.getAutocannonProjectile(projectileStack, level) : null;
	}

	@Nullable
	@Override
	public EntityType<?> getEntityType(ItemStack stack) {
		ItemStack projectileStack = getProjectileStack(stack);
		return projectileStack.getItem() instanceof AutocannonRoundItem projectileItem ? projectileItem.getEntityType(projectileStack) : null;
	}

	@Override
	public AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack) {
		ItemStack projectileStack = getProjectileStack(itemStack);
		return projectileStack.getItem() instanceof AutocannonRoundItem roundItem ? roundItem.getAutocannonProperties(itemStack) :
			AutocannonProjectilePropertiesComponent.DEFAULT;
	}

	public static ItemStack getProjectileStack(ItemStack stack) {
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.PROJECTILE, ItemContainerContents.EMPTY);
        return items.copyOne();
	}

	public static boolean hasProjectile(ItemStack stack) {
		return stack.has(CBCDataComponents.PROJECTILE);
	}

	public static void writeProjectile(ItemStack round, ItemStack cartridge) {
		if (round.getItem() instanceof AutocannonRoundItem && cartridge.getItem() instanceof AutocannonCartridgeItem) {
			cartridge.set(CBCDataComponents.PROJECTILE, ItemContainerContents.fromItems(Lists.newArrayList(round)));
		}
	}

	@Override
	public boolean isTracer(ItemStack stack) {
		return hasProjectile(stack) && getProjectileStack(stack).getOrDefault(CBCDataComponents.AUTOCANNON_TRACER, false);
	}

	@Override
	public void setTracer(ItemStack stack, boolean value) {
		if (!hasProjectile(stack))
            return;
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.PROJECTILE, ItemContainerContents.EMPTY);
        ItemStack projectile = items.copyOne();
        projectile.set(CBCDataComponents.AUTOCANNON_TRACER, value);
        stack.set(CBCDataComponents.PROJECTILE, ItemContainerContents.fromItems(Lists.newArrayList(projectile)));
	}

}
