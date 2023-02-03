package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public abstract class FuzedBigCannonProjectile extends AbstractBigCannonProjectile {

	private ItemStack fuze = ItemStack.EMPTY;
	
	protected FuzedBigCannonProjectile(EntityType<? extends FuzedBigCannonProjectile> type, Level level) {
		super(type, level);
	}
	
	public void setFuze(ItemStack stack) { this.fuze = stack == null ? ItemStack.EMPTY : stack; }
	
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
	protected void onFinalImpact(HitResult result) {
		super.onHit(result);
		if (!this.level.isClientSide && this.fuze.getItem() instanceof FuzeItem fuzeItem && fuzeItem.onProjectileImpact(this.fuze, this, result)) {
			this.detonate();
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Fuze", this.fuze.serializeNBT());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.fuze = ItemStack.of(tag.getCompound("Fuze"));
	}
	
	protected abstract void detonate();

}
