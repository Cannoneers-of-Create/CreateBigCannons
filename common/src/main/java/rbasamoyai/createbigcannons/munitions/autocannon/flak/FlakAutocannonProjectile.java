package rbasamoyai.createbigcannons.munitions.autocannon.flak;

import java.util.function.Predicate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FlakAutocannonProjectile extends AbstractAutocannonProjectile<FlakAutocannonProjectileProperties> {

	private ItemStack fuze = ItemStack.EMPTY;

	public FlakAutocannonProjectile(EntityType<? extends FlakAutocannonProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.canDetonate(fz -> fz.onProjectileTick(this.fuze, this))) this.detonate();
	}

	@Override
	protected void expireProjectile() {
		if (this.fuze.getItem() instanceof FuzeItem fuzeItem && fuzeItem.onProjectileExpiry(this.fuze, this)) this.detonate();
		super.expireProjectile();
	}

	@Override
	protected void onImpact(HitResult result, boolean stopped) {
		super.onImpact(result, stopped);
		if (this.canDetonate(fz -> fz.onProjectileImpact(this.fuze, this, result, stopped, false))) this.detonate();
	}

	@Override
	protected boolean onClip(ProjectileContext ctx, Vec3 pos) {
		if (super.onClip(ctx, pos)) return true;
		if (this.canDetonate(fz -> fz.onProjectileClip(this.fuze, this, pos, ctx, false))) {
			this.detonate();
			return true;
		}
		return false;
	}

	protected void detonate() {
		Vec3 oldDelta = this.getDeltaMovement();
		FlakAutocannonProjectileProperties properties = this.getProperties();
		this.level.explode(null, this.getX(), this.getY(), this.getZ(), properties == null ? 2 : properties.explosionPower(),
			CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		this.setDeltaMovement(oldDelta);
		if (properties != null) {
			Shrapnel.spawnShrapnelBurst(this.level, CBCEntityTypes.SHRAPNEL.get(), this.position(), this.getDeltaMovement(),
				properties.shrapnelCount(), properties.shrapnelSpread(), properties.shrapnelDamage());
		}
		this.discard();
	}

	public void setFuze(ItemStack fuze) { this.fuze = fuze; }

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.fuze != null && !this.fuze.isEmpty()) tag.put("Fuze", this.fuze.save(new CompoundTag()));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.fuze = tag.contains("Fuze", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Fuze")) : ItemStack.EMPTY;
	}

	protected final boolean canDetonate(Predicate<FuzeItem> cons) {
		return !this.level.isClientSide && this.level.hasChunkAt(this.blockPosition()) && this.fuze.getItem() instanceof FuzeItem fuzeItem && cons.test(fuzeItem);
	}

}
