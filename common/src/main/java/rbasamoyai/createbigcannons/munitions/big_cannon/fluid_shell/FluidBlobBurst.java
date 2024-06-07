package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;
import rbasamoyai.createbigcannons.munitions.fragment_burst.ProjectileBurstProperties;
import rbasamoyai.createbigcannons.network.ClientboundFluidBlobStackSyncPacket;

public class FluidBlobBurst extends CBCProjectileBurst<ProjectileBurstProperties> {

	private static final EntityDataAccessor<Byte> BLOB_SIZE = SynchedEntityData.defineId(FluidBlobBurst.class, EntityDataSerializers.BYTE);
	private EndFluidStack fluidStack = EndFluidStack.EMPTY;

	public FluidBlobBurst(EntityType<? extends CBCProjectileBurst<?>> entityType, Level level) { super(entityType, level); }

	public static float getBlockAffectChance() {
		return CBCConfigs.SERVER.munitions.fluidBlobBlockAffectChance.getF();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(BLOB_SIZE, (byte) 0);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putByte("Size", this.getBlobSize());
		tag.put("Fluid", this.getFluidStack().writeTag(new CompoundTag()));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setBlobSize(tag.getByte("Size"));
		this.setFluidStack(EndFluidStack.readTag(tag.getCompound("Fluid")));
	}

	protected void setBlobSize(byte size) {
		this.entityData.set(BLOB_SIZE, size < 0 ? (byte) 0 : size);
	}

	public byte getBlobSize() {
		return this.entityData.get(BLOB_SIZE);
	}

	public void setFluidStack(EndFluidStack fstack) { this.fluidStack = fstack; }
	public EndFluidStack getFluidStack() { return this.fluidStack; }

	@Override public double getSubProjectileWidth() { return 0.8; }
	@Override public double getSubProjectileHeight() { return 0.8; }

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide) {
			if (this.level.getGameTime() % 3 == 0) {
				NetworkPlatform.sendToClientTracking(new ClientboundFluidBlobStackSyncPacket(this), this);
			}
		}
		if (this.level.isClientSide) {
			if (!this.getFluidStack().isEmpty()) {
				for (SubProjectile subProjectile : this.subProjectiles) {
					double[] disp = subProjectile.displacement();
					double[] vel = subProjectile.velocity();
					this.level.addParticle(new FluidBlobParticleData(this.getBlobSize() * 0.25f + 1, this.getFluidStack().copy()),
						this.getX() + disp[0], this.getY() + disp[1], this.getZ() + disp[2], vel[0], vel[1], vel[2]);
				}
			}
		}
	}

	@Override
	protected void onSubProjectileHit(HitResult result, SubProjectile subProjectile) {
		if (!this.level.isClientSide)
			FluidBlobEffectRegistry.effectOnAllHit(this, subProjectile, result);
		super.onSubProjectileHit(result, subProjectile);
	}

	@Override
	protected void onSubProjectileHitBlock(BlockHitResult result, SubProjectile subProjectile) {
		if (!this.level.isClientSide)
			FluidBlobEffectRegistry.effectOnHitBlock(this, subProjectile, result);
		super.onSubProjectileHitBlock(result, subProjectile);
	}

	@Override
	protected void onSubProjectileHitEntity(EntityHitResult result, SubProjectile subProjectile) {
		if (!this.level.isClientSide)
			FluidBlobEffectRegistry.effectOnHitEntity(this, subProjectile, result);
		super.onSubProjectileHitEntity(result, subProjectile);
	}

	public AABB getAreaOfEffect(BlockPos pos) {
		return new AABB(pos).inflate(this.getBlobSize());
	}

}
