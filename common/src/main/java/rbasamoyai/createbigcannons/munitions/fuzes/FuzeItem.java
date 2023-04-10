package rbasamoyai.createbigcannons.munitions.fuzes;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;

import java.util.List;

public class FuzeItem extends Item {

	public FuzeItem(Properties properties) {
		super(properties);
	}
	
	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile projectile) { return false; }
	public boolean onProjectileClip(ItemStack stack, AbstractCannonProjectile projectile, Vec3 location, ProjectileContext ctx) { return false; }
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult result, boolean stopped) { return false; }
	public boolean onProjectileExpiry(ItemStack stack, AbstractCannonProjectile projectile) { return false; }
	
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {}

}
