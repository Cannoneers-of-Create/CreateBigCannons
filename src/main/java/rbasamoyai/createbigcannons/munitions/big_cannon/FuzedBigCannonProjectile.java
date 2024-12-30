package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public abstract class FuzedBigCannonProjectile extends AbstractBigCannonProjectile {

	private ItemStack fuze = ItemStack.EMPTY;
	private int explosionCountdown = -1;

	protected FuzedBigCannonProjectile(EntityType<? extends FuzedBigCannonProjectile> type, Level level) {
		super(type, level);
	}

	public void setFuze(ItemStack stack) { this.fuze = stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack; }

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide && this.explosionCountdown > 0)
			--this.explosionCountdown;
		if (this.canDetonate(fz -> fz.onProjectileTick(this.fuze, this)) || !this.level().isClientSide && this.explosionCountdown == 0) {
			this.detonate(this.position());
			this.removeNextTick = true;
		}
	}

	@Override
	protected boolean onClip(ProjectileContext ctx, Vec3 start, Vec3 end) {
		if (super.onClip(ctx, start, end)) return true;
		boolean baseFuze = this.getFuzeProperties().baseFuze();
		if (this.canDetonate(fz -> fz.onProjectileClip(this.fuze, this, start, end, ctx, baseFuze))) {
			this.detonate(start);
			return true;
		}
		return false;
	}

	@Override
	protected boolean onImpact(HitResult hitResult, ImpactResult impactResult, ProjectileContext projectileContext) {
		super.onImpact(hitResult, impactResult, projectileContext);
		boolean baseFuze = this.getFuzeProperties().baseFuze();
		if (this.canDetonate(fz -> fz.onProjectileImpact(this.fuze, this, hitResult, impactResult, baseFuze))) {
			this.detonate(hitResult.getLocation());
			return true;
		} else {
			return false;
		}
	}

	@Nonnull protected abstract BigCannonFuzePropertiesComponent getFuzeProperties();

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Fuze", this.fuze.save(new CompoundTag()));
		if (this.explosionCountdown >= 0)
			tag.putInt("ExplosionCountdown", this.explosionCountdown);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.fuze = ItemStack.of(tag.getCompound("Fuze"));
		this.explosionCountdown = tag.contains("ExplosionCountdown", Tag.TAG_INT) ? tag.getInt("ExplosionCountdown") : -1;
	}

	protected final boolean canDetonate(Predicate<FuzeItem> cons) {
		return !this.level().isClientSide && this.level().hasChunkAt(this.blockPosition()) && !this.isRemoved()
			&& this.fuze.getItem() instanceof FuzeItem fuzeItem && cons.test(fuzeItem);
	}

	/**
	 * Use {@link #detonate(Position)}
	 */
	@Deprecated
	protected void detonate() { this.detonate(this.position()); }

	protected abstract void detonate(Position position);

	@Override
	public boolean canLingerInGround() {
		return !this.level().isClientSide && this.level().hasChunkAt(this.blockPosition()) && this.fuze.getItem() instanceof FuzeItem fuzeItem && fuzeItem.canLingerInGround(this.fuze, this);
	}

	public void setExplosionCountdown(int value) { this.explosionCountdown = Math.max(value, -1); }
	public int getExplosionCountdown() { return this.explosionCountdown; }

}
