package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface DropMortarMunition<PROJECTILE extends AbstractBigCannonProjectile & DropMortarProjectile> {

	PROJECTILE getDropMortarProjectile(Level level, ItemStack stack);

	static ItemStack getFuze(ItemStack stack) {
		return ItemStack.of(stack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("Fuze"));
	}

	static ItemStack getTracer(ItemStack stack) {
		return ItemStack.of(stack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("Tracer"));
	}

}
