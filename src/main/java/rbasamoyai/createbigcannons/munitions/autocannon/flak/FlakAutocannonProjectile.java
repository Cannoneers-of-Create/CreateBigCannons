package rbasamoyai.createbigcannons.munitions.autocannon.flak;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;

public class FlakAutocannonProjectile extends AbstractAutocannonProjectile {

	private ItemStack fuze = ItemStack.EMPTY;

	public FlakAutocannonProjectile(EntityType<? extends FlakAutocannonProjectile> type, Level level) {
		super(type, level);
		this.setProjectileMass((byte) 1);
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide
			&& this.level.hasChunkAt(this.blockPosition())
			&& this.fuze.getItem() instanceof FuzeItem fuzeItem
			&& fuzeItem.onProjectileTick(this.fuze, this)) {
			this.detonate();
		}
	}

	@Override
	protected void expireProjectile() {
		if (this.fuze.getItem() instanceof FuzeItem fuzeItem && fuzeItem.onProjectileExpiry(this.fuze, this)) this.detonate();
		super.expireProjectile();
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (!this.level.isClientSide && this.fuze.getItem() instanceof FuzeItem fuzeItem && fuzeItem.onProjectileImpact(this.fuze, this, result)) {
			this.detonate();
		}
	}

	protected void detonate() {
		Vec3 oldDelta = this.getDeltaMovement();
		this.level.explode(null, this.getX(), this.getY(), this.getZ(), 2.0f, CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		this.setDeltaMovement(oldDelta);
		int count = CBCConfigs.SERVER.munitions.flakCount.get();
		float spread = CBCConfigs.SERVER.munitions.flakSpread.getF();
		float damage = CBCConfigs.SERVER.munitions.flakDamage.getF();
		Shrapnel.spawnShrapnelBurst(this.level, CBCEntityTypes.SHRAPNEL.get(), this.position(), this.getDeltaMovement(), count, spread, damage);
		this.discard();
	}

	public void setFuze(ItemStack fuze) { this.fuze = fuze; }

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.fuze != null && !this.fuze.isEmpty()) tag.put("Fuze", this.fuze.serializeNBT());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.fuze = tag.contains("Fuze", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Fuze")) : ItemStack.EMPTY;
	}

}
