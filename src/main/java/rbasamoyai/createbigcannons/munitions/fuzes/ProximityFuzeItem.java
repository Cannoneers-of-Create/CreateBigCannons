package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;
import java.util.Optional;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
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
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;

public class ProximityFuzeItem extends FuzeItem implements MenuProvider {

	public ProximityFuzeItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult hitResult, AbstractCannonProjectile.ImpactResult impactResult, boolean baseFuze) {
		return !baseFuze;
	}

	@Override
	public boolean onProjectileExpiry(ItemStack stack, AbstractCannonProjectile projectile) {
		return true;
	}

	@Override
	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile projectile) {
		int airTime = stack.getOrDefault(CBCDataComponents.AIR_TIME, 0);
		if (airTime > CBCConfigs.server().munitions.proximityFuzeArmingTime.get()) stack.set(CBCDataComponents.ARMED, true);
		stack.set(CBCDataComponents.AIR_TIME, ++airTime);
		return false;
	}

	@Override
	public boolean onProjectileClip(ItemStack stack, AbstractCannonProjectile projectile, Vec3 start, Vec3 end, ProjectileContext ctx, boolean baseFuze) {
		if (baseFuze) return false;
		if (!stack.has(CBCDataComponents.ARMED)) return false;

		double l = Math.max(stack.getOrDefault(CBCDataComponents.DETONATION_DISTANCE, 1), 1);
		Vec3 dir = projectile.getOrientation().normalize();
		Vec3 right = dir.cross(new Vec3(Direction.UP.step())).normalize();
        if (right.lengthSqr() < 1e-6d)
            right = new Vec3(1, 0, 0); // vertical burst
		Vec3 up = dir.cross(right).normalize();
		dir = dir.scale(l);
        Vec3 disp = end.subtract(start);

        int radius = CBCConfigs.server().munitions.proximityFuzeScale.get();
        double scale = CBCConfigs.server().munitions.proximityFuzeSpacing.get();

		AABB currentMovementRegion = projectile.getBoundingBox()
			.expandTowards(dir.scale(1.75))
			.inflate(radius * scale + 2)
			.move(start.subtract(projectile.position()));
        Level level = projectile.level();
		List<Entity> entities = level.getEntities(projectile, currentMovementRegion, projectile::canHitEntity);

        boolean hit = false;
        double detonationDistance = disp.length() + l; // Upper bound, if disp and orientation are co-linear

		for (int i = -radius; i <= radius; ++i) {
			for (int j = -radius; j <= radius; ++j) {
				Vec3 ray = dir.add(right.scale(i * scale)).add(up.scale(j * scale));
				Vec3 rayEnd = start.add(ray);
                Vec3 rayExt = rayEnd.add(disp);

                HitResult stemResult = level.clip(new ClipContext(start, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile));
                if (stemResult.getType() == HitResult.Type.MISS)
                    stemResult = level.clip(new ClipContext(rayEnd, rayExt, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile));
                if (stemResult.getType() != HitResult.Type.MISS) {
                    hit = true;
                    Vec3 hitLoc = stemResult.getLocation();
                    double candidateDist = start.distanceTo(hitLoc);
                    if (candidateDist < detonationDistance)
                        detonationDistance = candidateDist;
                }

				for (Entity target : entities) {
					AABB targetBox = target.getBoundingBox().inflate(scale * 0.5);
                    Optional<Vec3> hitOp = targetBox.clip(start, rayEnd);
                    if (hitOp.isEmpty())
                        hitOp = targetBox.clip(rayEnd, rayExt);
                    if (hitOp.isPresent()) {
                        hit = true;
                        Vec3 hitLoc = hitOp.get();
                        double candidateDist = start.distanceTo(hitLoc);
                        if (candidateDist < detonationDistance)
                            detonationDistance = candidateDist;
                    }
				}
			}
		}
        if (hit) {
            ctx.setDetonationPositionForClip(start.add(disp.normalize().scale(Math.max(detonationDistance - l, 0)))); // Only looking for offset
            return true;
        }

		return super.onProjectileClip(stack, projectile, start, end, ctx, false);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer splayer && player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			if (!stack.has(CBCDataComponents.DETONATION_DISTANCE)) {
				stack.set(CBCDataComponents.DETONATION_DISTANCE, 1);
			}
			int dist = stack.getOrDefault(CBCDataComponents.DETONATION_DISTANCE, 1);
			CBCMenuTypes.SET_PROXIMITY_FUZE.open(splayer, this.getDisplayName(), this, buf -> {
				buf.writeVarInt(dist);
                ItemStack.STREAM_CODEC.encode(buf, new ItemStack(this));
			});
		}
		return super.use(level, player, hand);
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
		return ProximityFuzeContainer.getServerMenu(windowId, playerInv, player.getMainHandItem());
	}

	@Override
	public Component getDisplayName() {
		return this.getDescription();
	}

	public static ItemStack getCreativeTabItem(int defaultFuze) {
		ItemStack stack = CBCItems.PROXIMITY_FUZE.asStack();
        stack.set(CBCDataComponents.DETONATION_DISTANCE, 1);
		return stack;
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = CreateLang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info", stack.getOrDefault(CBCDataComponents.DETONATION_DISTANCE, 1))
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
		tooltip.add(CreateLang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info.item", stack.getOrDefault(CBCDataComponents.DETONATION_DISTANCE, 1))
			.component());
	}

}
