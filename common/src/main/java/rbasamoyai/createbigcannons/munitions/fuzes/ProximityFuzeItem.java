package rbasamoyai.createbigcannons.munitions.fuzes;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;

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
		tag.putInt("AirTime", ++airTime);
		return false;
	}

	@Override
	public boolean onProjectileClip(ItemStack stack, AbstractCannonProjectile projectile, Vec3 location, ProjectileContext ctx) {
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("Armed")) return false;

		double l = Math.max(tag.getInt("DetonationDistance"), 1);
		Vec3 dir = projectile.getDeltaMovement().normalize();
		Vec3 right = dir.cross(new Vec3(Direction.UP.step()));
		Vec3 up = dir.cross(right);
		dir = dir.scale(l);
		double reach = Math.max(projectile.getBbWidth(), projectile.getBbHeight()) * 0.5;

		AABB currentMovementRegion = projectile.getBoundingBox()
				.expandTowards(dir.scale(1.75))
				.inflate(1)
				.move(location.subtract(projectile.position()));
		List<Entity> entities = projectile.level.getEntities(projectile, currentMovementRegion, projectile::canHitEntity);

		int radius = 2;
		double scale = 1.5;
		for (int i = -radius; i <= radius; ++i) {
			for (int j = -radius; j <= radius; ++j) {
				Vec3 ray = dir.add(right.scale(i * scale)).add(up.scale(j * scale));
				Vec3 rayEnd = location.add(ray);

				if (projectile.level.clip(new ClipContext(location, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile)).getType() != HitResult.Type.MISS) {
					return true;
				}

				for (Entity target : entities) {
					AABB targetBox = target.getBoundingBox().inflate(reach);
					if (targetBox.clip(location, rayEnd).isPresent()) return true;
				}
			}
		}

		return super.onProjectileClip(stack, projectile, location, ctx);
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
			CBCMenuTypes.SET_PROXIMITY_FUZE.open((ServerPlayer) player, this.getDisplayName(), this, buf -> {
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
