package rbasamoyai.createbigcannons.munitions.autocannon.flak;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FlakAutocannonRoundItem extends AutocannonRoundItem implements FuzedItemMunition {

	public FlakAutocannonRoundItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
		FlakAutocannonProjectile projectile = CBCEntityTypes.FLAK_AUTOCANNON.create(level);
		if (stack.has(CBCDataComponents.FUZE)) {
            ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
            ItemStack fuze = items.copyOne();
			projectile.setFuze(fuze);
		}
		return projectile;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
        ItemStack fuze = items.copyOne();
		if (!fuze.isEmpty()) {
			CreateLang.builder("block")
				.translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze")
				.add(Component.literal(" "))
				.add(fuze.getDisplayName().copy())
				.addTo(tooltip);
			if (fuze.getItem() instanceof FuzeItem) {
				List<Component> subTooltip = new ArrayList<>();
				fuze.getItem().appendHoverText(fuze, ctx, subTooltip, flag);
				subTooltip.replaceAll(sibling -> Component.literal("  ").append(sibling).withStyle(ChatFormatting.GRAY));
				tooltip.addAll(subTooltip);
			}
		}
	}

	@Nonnull
	@Override
	public AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack) {
		return CBCMunitionPropertiesHandlers.FLAK_AUTOCANNON.getPropertiesOf(this.getEntityType(itemStack)).autocannonProperties();
	}

	@Override
	public EntityType<?> getEntityType(ItemStack stack) {
		return CBCEntityTypes.FLAK_AUTOCANNON.get();
	}

}
