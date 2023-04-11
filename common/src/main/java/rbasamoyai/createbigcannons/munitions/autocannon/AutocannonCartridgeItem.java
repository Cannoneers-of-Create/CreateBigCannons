package rbasamoyai.createbigcannons.munitions.autocannon;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.index.CBCItems;

import java.util.ArrayList;
import java.util.List;

public class AutocannonCartridgeItem extends Item {

    public AutocannonCartridgeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ItemStack round = getProjectileStack(stack);
        if (!round.isEmpty()) {
            tooltip.add(Component.translatable("item.minecraft.crossbow.projectile").append(" ").append(round.getDisplayName()));
            if (flag.isAdvanced() && round.getItem() instanceof AutocannonRoundItem) {
                List<Component> subTooltip = new ArrayList<>();
                round.getItem().appendHoverText(round, level, subTooltip, flag);
                for (int i = 0; i < subTooltip.size(); ++i) {
                    subTooltip.set(i, Component.literal("  ").append(subTooltip.get(i)).withStyle(ChatFormatting.GRAY));
                }
                tooltip.addAll(subTooltip);
            }
        }
    }

    public ItemStack getEmptyCartridge(ItemStack stack) {
        return CBCItems.EMPTY_AUTOCANNON_CARTRIDGE.asStack();
    }

    @Nullable
    public static AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
        ItemStack projectileStack = getProjectileStack(stack);
        return projectileStack.getItem() instanceof AutocannonRoundItem projectileItem ? projectileItem.getAutocannonProjectile(projectileStack, level) : null;
    }

    public static ItemStack getProjectileStack(ItemStack stack) {
        return hasProjectile(stack) ? ItemStack.of(stack.getOrCreateTag().getCompound("Projectile")) : ItemStack.EMPTY;
    }

    public static boolean hasProjectile(ItemStack stack) {
        return stack.getOrCreateTag().contains("Projectile", Tag.TAG_COMPOUND);
    }

    public static void writeProjectile(ItemStack round, ItemStack cartridge) {
        if (round.getItem() instanceof AutocannonRoundItem && cartridge.getItem() instanceof AutocannonCartridgeItem) {
            cartridge.getOrCreateTag().put("Projectile", round.save(new CompoundTag()));
        }
    }

}
