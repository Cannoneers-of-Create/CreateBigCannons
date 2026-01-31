package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;

public class FuzeItem extends Item {

	public FuzeItem(Properties properties) {
		super(properties);
	}

	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile projectile) { return false; }
	public boolean onProjectileClip(ItemStack stack, AbstractCannonProjectile projectile, Vec3 start, Vec3 end, ProjectileContext ctx, boolean baseFuze) { return false; }
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult hitResult, AbstractCannonProjectile.ImpactResult impactResult, boolean baseFuze) { return false; }
	public boolean onProjectileExpiry(ItemStack stack, AbstractCannonProjectile projectile) { return false; }
	public boolean onRedstoneSignal(ItemStack stack, Level level, BlockPos pos, BlockState state, int signalStrength, Direction from) { return false; }
	public boolean canLingerInGround(ItemStack stack, AbstractCannonProjectile projectile) { return false; }

	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {}

}
