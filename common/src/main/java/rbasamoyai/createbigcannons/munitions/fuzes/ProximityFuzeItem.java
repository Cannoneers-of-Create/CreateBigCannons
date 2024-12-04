package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
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
		CompoundTag tag = stack.getOrCreateTag();
		int airTime = tag.getInt("AirTime");
		if (airTime > CBCConfigs.SERVER.munitions.proximityFuzeArmingTime.get()) tag.putBoolean("Armed", true);
		tag.putInt("AirTime", ++airTime);
		return false;
	}

	@Override
	public boolean onProjectileClip(ItemStack stack, AbstractCannonProjectile projectile, Vec3 start, Vec3 end, ProjectileContext ctx, boolean baseFuze) {
		if (baseFuze) return false;
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("Armed")) return false;

		double l = Math.max(tag.getInt("DetonationDistance"), 1);
		double angle = Math.toRadians(Math.max(tag.getInt("DetonationAngle"),0));
		Vec3 dir = projectile.getDeltaMovement().normalize();
		Vector3f dirNorm3f = dir.toVector3f();
		Vec3 right = dir.cross(new Vec3(Direction.UP.step()));
		Vec3 up = dir.cross(right);
		dir = dir.scale(l);

		Vec3 trajectory = end.subtract(start);
		Vec3 normalizedTraj = trajectory.normalize();

		Vector3f dir3f = dir.toVector3f();
		Vector3f right3f = right.toVector3f();
		Quaternionf rotationAxial = new Quaternionf(new AxisAngle4f((float)Math.toRadians(30), dirNorm3f));
		Quaternionf rotationRadial = new Quaternionf(new AxisAngle4f((float)angle, right3f));
		Quaternionf rotationRadialThird = new Quaternionf(new AxisAngle4f((float)angle/3, right3f));
		Vec3 rayStart = new Vec3(start.x, start.y, start.z);
		for (int i = 0; i < trajectory.length() / 2;i++){
			Vector3f dirRotated = new Vector3f(dir3f);
			dirRotated.rotate(rotationRadial);
			for (int j = 0; j < 12; j++){
				Vec3 rayEnd = new Vec3(dirRotated).add(rayStart);
				if (projectile.level().clip(new ClipContext(rayStart, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile)).getType() != HitResult.Type.MISS) {
					return true;
				}
				dirRotated.rotate(rotationAxial);
			}
			rayStart = rayStart.add(dir.normalize().scale(i * 2));
		}
		rayStart = new Vec3(end.x, end.y, end.z);
		for (int i = 0; i < 3;i++){
			Vector3f dirRotated = new Vector3f(dir3f);
			dirRotated.rotate(rotationRadialThird);
			for(int i2 = 0; i2 < i; i2++){
				dirRotated.rotate(rotationRadialThird);
			}
			for (int j = 0; j < 12; j++){
				Vec3 rayEnd = new Vec3(dirRotated).add(rayStart);
				if (projectile.level().clip(new ClipContext(rayStart, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile)).getType() != HitResult.Type.MISS) {
					return true;
				}
				dirRotated.rotate(rotationAxial);
			}
		}
		if (projectile.level().clip(new ClipContext(end, end.add(dir), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile)).getType() != HitResult.Type.MISS) {
			return true;
		}

		AABB currentMovementRegion = projectile.getBoundingBox()
			.expandTowards(dir.scale(1.75))
			.inflate(l)
			.move(start.subtract(projectile.position()));
		List<Entity> entities = projectile.level().getEntities(projectile, currentMovementRegion, projectile::canHitEntity);

		for (Entity target : entities) {
			float targetRadius = Math.max(target.getBbHeight(), target.getBbWidth())/2;
			Vec3 relPos = target.position().subtract(start);
			double frontalDist = normalizedTraj.dot(relPos);
			double lateralDist = normalizedTraj.cross(relPos).length();
			if (lateralDist > (l + targetRadius) * Math.sin(angle)) continue;
			double minimalDist = -targetRadius;
			double maximalDist = trajectory.length() + l + targetRadius;
			if (angle > 0){
				minimalDist = lateralDist * 1 / Math.tan(angle) - targetRadius;
				maximalDist = trajectory.length() + Math.sqrt((l + targetRadius) * (l + targetRadius) - lateralDist * lateralDist);
			}
			if (frontalDist >= minimalDist && frontalDist <= maximalDist){
				return true;
			}
		}

		return super.onProjectileClip(stack, projectile, start, end, ctx, false);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer splayer && player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			CompoundTag tag = stack.getOrCreateTag();
			if (!tag.contains("DetonationDistance")) {
				tag.putInt("DetonationDistance", 1);
			}
			if (!tag.contains("DetonationAngle")){
				tag.putInt("DetonationAngle",0);
			}
			int dist = tag.getInt("DetonationDistance");
			int ang = tag.getInt("DetonationAngle");
			CBCMenuTypes.SET_PROXIMITY_FUZE.open(splayer, this.getDisplayName(), this, buf -> {
				buf.writeVarInt(dist);
				buf.writeVarInt(ang);
				buf.writeItem(new ItemStack(this));
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
		stack.getOrCreateTag().putInt("DetonationDistance", 1);
		stack.getOrCreateTag().putInt("DetonationAngle",0);
		return stack;
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info", stack.getOrCreateTag().getInt("DetonationDistance"), stack.getOrCreateTag().getInt("DetonationAngle"))
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".proximity_fuze.tooltip.shell_info.item", stack.getOrCreateTag().getInt("DetonationDistance"), stack.getOrCreateTag().getInt("DetonationAngle"))
			.component());
	}

}
