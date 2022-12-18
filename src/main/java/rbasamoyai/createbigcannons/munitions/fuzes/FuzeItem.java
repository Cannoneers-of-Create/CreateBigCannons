package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class FuzeItem extends Item {

	public FuzeItem(Properties properties) {
		super(properties);
	}
	
	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile projectile) { return false; }
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult result) { return false; }
	public boolean onProjectileExpiry(ItemStack stack, AbstractCannonProjectile projectile) { return false; }
	
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {}

}
