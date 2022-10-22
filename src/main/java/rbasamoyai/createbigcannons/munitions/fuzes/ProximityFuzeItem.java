package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.Iterator;
import java.util.List;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.FuzedCannonProjectile;

public class ProximityFuzeItem extends FuzeItem implements MenuProvider {

	public ProximityFuzeItem(Properties properties) {
		super(properties);
	}
	
	@Override public boolean onProjectileImpact(ItemStack stack, FuzedCannonProjectile projectile, HitResult result) { return true; }
	
	@Override
	public boolean onProjectileTick(ItemStack stack, FuzedCannonProjectile projectile) {
		CompoundTag tag = stack.getOrCreateTag();
		int airTime = tag.getInt("AirTime");
		if (airTime > CBCConfigs.SERVER.munitions.proximityFuzeArmingTime.get()) tag.putBoolean("Armed", true);
		boolean armed = tag.contains("Armed");
		tag.putInt("AirTime", ++airTime);
		if (!armed) return false;
		
		double h = Math.max(tag.getInt("DetonationDistance"), 1);
		double r = h * 0.5d;
		double h1 = h * 0.5d;
		Vec3 dir = projectile.getDeltaMovement().length() < 1e-4d ? new Vec3(0, -1, 0) : projectile.getDeltaMovement().normalize();
		Vec3 p = projectile.position();
		Vec3 p1 = p.add(dir.scale(h1));
		double x0 = p1.x;
		double y0 = p1.y;
		double z0 = p1.z;
		AABB roughBounds = new AABB(x0 - h1, y0 - h1, z0 - h1, x0 + h1, y0 + h1, z0 + h1).inflate(1);
		for (Iterator<BlockPos> iter = BlockPos.betweenClosedStream(roughBounds).iterator(); iter.hasNext(); ) {
			BlockPos bp = iter.next();
			Vec3 p2 = Vec3.atCenterOf(bp).subtract(p);
			double d = p2.dot(dir);
			double r1 = d * r / h;
			if (0 > d || d > h || p2.subtract(dir.scale(d)).lengthSqr() >= r1 * r1) continue;
			if (!projectile.level.getBlockState(bp).isAir()) return true;
		}
		return false;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer && player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			CompoundTag tag = stack.getOrCreateTag();
			if (!tag.contains("DetonationDistance")) {
				tag.putInt("DetonationDistance", 1);
			}
			int dist = tag.getInt("DetonationDistance");
			NetworkHooks.openGui((ServerPlayer) player, this, buf -> {
				buf.writeVarInt(dist);
				buf.writeItem(new ItemStack(this));
			});
		}
		return super.use(level, player, hand);
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
		return ProximityFuzeContainer.getServerMenu(windowId, playerInv, player.getMainHandItem());
	}

	@Override public Component getDisplayName() { return this.getDescription(); }
	
	public static ItemStack getCreativeTabItem(int defaultFuze) {
		ItemStack stack = CBCItems.PROXIMITY_FUZE.asStack();
		stack.getOrCreateTag().putInt("DetonationDistance", 1);
		return stack;
	}
	
	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = Lang.builder("item")
				.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info", stack.getOrCreateTag().getInt("DetonationDistance"))
				.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, ChatFormatting.GRAY, ChatFormatting.GREEN, 6));
	}

}
