package rbasamoyai.createbigcannons.munitions.fuzes;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

import java.util.List;

public class ProximityFuzeItem extends FuzeItem implements MenuProvider {

	public ProximityFuzeItem(Properties properties) {
		super(properties);
	}
	
	@Override public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult result) { return true; }
	@Override public boolean onProjectileExpiry(ItemStack stack, AbstractCannonProjectile projectile) { return true; }

	@Override
	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile projectile) {
		CompoundTag tag = stack.getOrCreateTag();
		int airTime = tag.getInt("AirTime");
		if (airTime > CBCConfigs.SERVER.munitions.proximityFuzeArmingTime.get()) tag.putBoolean("Armed", true);
		boolean armed = tag.contains("Armed");
		tag.putInt("AirTime", ++airTime);
		if (!armed) return false;
		
		double l = Math.max(tag.getInt("DetonationDistance"), 1) * 1.5d;
		Vec3 oldVel = projectile.getDeltaMovement();
		projectile.setDeltaMovement(oldVel.normalize().scale(l));
		HitResult scan = ProjectileUtil.getHitResult(projectile, projectile::canHitEntity);
		projectile.setDeltaMovement(oldVel);

		return scan.getType() != HitResult.Type.MISS;
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

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Lang.builder("item")
				.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info.item", stack.getOrCreateTag().getInt("DetonationDistance"))
				.component());
	}

}
