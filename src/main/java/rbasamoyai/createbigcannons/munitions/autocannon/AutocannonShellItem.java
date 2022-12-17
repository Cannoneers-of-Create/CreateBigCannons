package rbasamoyai.createbigcannons.munitions.autocannon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AutocannonShellItem extends Item {

    public AutocannonShellItem(Properties properties) { super(properties); }

    public ItemStack getProjectileFromStack(ItemStack stack) {
        if (stack.getItem() != this) return ItemStack.EMPTY;
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains("Projectile", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Projectile")) : ItemStack.EMPTY;
    }

}
