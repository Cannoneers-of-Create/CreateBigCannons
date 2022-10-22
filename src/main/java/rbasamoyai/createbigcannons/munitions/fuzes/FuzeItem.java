package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.munitions.FuzedCannonProjectile;

public class FuzeItem extends Item {

	public FuzeItem(Properties properties) {
		super(properties);
	}
	
	public boolean onProjectileTick(ItemStack stack, FuzedCannonProjectile projectile) { return false; }
	public boolean onProjectileImpact(ItemStack stack, FuzedCannonProjectile projectile, HitResult result) { return false; }
	
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {}

}
