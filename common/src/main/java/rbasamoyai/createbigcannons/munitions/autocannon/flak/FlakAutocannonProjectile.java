package rbasamoyai.createbigcannons.munitions.autocannon.flak;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FlakAutocannonProjectile extends AbstractAutocannonProjectile {

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
	protected boolean onImpact(HitResult result, boolean stopped) {
		super.onImpact(result, stopped);
		if (this.canDetonate(fz -> fz.onProjectileImpact(this.fuze, this, result, stopped, false))) {
			this.detonate();
			return true;
		} else {
			return false;
		}
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
		FlakAutocannonProjectileProperties properties = this.getAllProperties();
		FlakExplosion explosion = new FlakExplosion(this.level, null, this.indirectArtilleryFire(), this.getX(), this.getY(), this.getZ(),
			properties.explosion().explosivePower(), CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level, explosion);
		CBCProjectileBurst.spawnConeBurst(this.level, CBCEntityTypes.FLAK_BURST.get(), this.position(), oldDelta,
			properties.flakBurst().burstProjectileCount(), properties.flakBurst().burstSpread());
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

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	protected FlakAutocannonProjectileProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.FLAK_AUTOCANNON.getPropertiesOf(this);
	}

}
