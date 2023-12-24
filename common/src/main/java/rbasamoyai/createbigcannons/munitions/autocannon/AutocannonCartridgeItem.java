package rbasamoyai.createbigcannons.munitions.autocannon;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;

import java.util.ArrayList;
import java.util.List;

public class AutocannonCartridgeItem extends Item implements AutocannonAmmoItem {

    public AutocannonCartridgeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ItemStack round = getProjectileStack(stack);
        if (!round.isEmpty()) {
            tooltip.add(new TranslatableComponent("item.minecraft.crossbow.projectile").append(" ").append(round.getDisplayName()));
            if (round.getItem() instanceof AutocannonRoundItem) {
                List<Component> subTooltip = new ArrayList<>();
                round.getItem().appendHoverText(round, level, subTooltip, flag);
                for (int i = 0; i < subTooltip.size(); ++i) {
                    subTooltip.set(i, new TextComponent("  ").append(subTooltip.get(i)).withStyle(ChatFormatting.GRAY));
                }
                tooltip.addAll(subTooltip);
            }
        }
    }

	@Override public ItemStack getSpentItem(ItemStack stack) { return CBCItems.EMPTY_AUTOCANNON_CARTRIDGE.asStack(); }

	@Override public AutocannonAmmoType getType() { return AutocannonAmmoType.AUTOCANNON; }

	@Override
	@Nullable
    public AbstractAutocannonProjectile<?> getAutocannonProjectile(ItemStack stack, Level level) {
        ItemStack projectileStack = getProjectileStack(stack);
        return projectileStack.getItem() instanceof AutocannonRoundItem projectileItem ? projectileItem.getAutocannonProjectile(projectileStack, level) : null;
    }

	@Nullable
	@Override
	public EntityType<? extends PropertiesMunitionEntity<? extends AutocannonProjectileProperties>> getEntityType(ItemStack stack) {
		ItemStack projectileStack = getProjectileStack(stack);
		return projectileStack.getItem() instanceof AutocannonRoundItem projectileItem ? projectileItem.getEntityType(projectileStack) : null;
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

	@Override
	public boolean isTracer(ItemStack stack) {
		return hasProjectile(stack) && getProjectileStack(stack).getOrCreateTag().getBoolean("Tracer");
	}

	@Override
	public void setTracer(ItemStack stack, boolean value) {
		if (!hasProjectile(stack)) return;
		CompoundTag tag = stack.getOrCreateTag().getCompound("Projectile");
		if (!tag.contains("tag", Tag.TAG_COMPOUND)) tag.put("tag", new CompoundTag());
		tag.getCompound("tag").putBoolean("Tracer", true);
	}

}
