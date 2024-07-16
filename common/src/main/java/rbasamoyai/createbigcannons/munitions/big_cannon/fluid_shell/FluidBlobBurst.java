package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;
import rbasamoyai.createbigcannons.network.ClientboundFluidBlobStackSyncPacket;
import rbasamoyai.ritchiesprojectilelib.projectile_burst.ProjectileBurstClipContext;

public class FluidBlobBurst extends CBCProjectileBurst {

	private static final EntityDataAccessor<Byte> BLOB_SIZE = SynchedEntityData.defineId(FluidBlobBurst.class, EntityDataSerializers.BYTE);
	private EndFluidStack fluidStack = EndFluidStack.EMPTY;

	public FluidBlobBurst(EntityType<? extends FluidBlobBurst> entityType, Level level) { super(entityType, level); }

	public static float getBlockAffectChance() {
		return CBCConfigs.SERVER.munitions.fluidBlobBlockAffectChance.getF();
	}

	private final Set<Entity> clippedThisTick = new HashSet<>();

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
		this.clippedThisTick.clear();
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

	protected HitResult clipAndDamage(SubProjectile info) {
		Vec3 vel = new Vec3(info.velocity()[0], info.velocity()[1], info.velocity()[2]);
		Vec3 start = new Vec3(info.displacement()[0] + this.getX(), info.displacement()[1] + this.getY(), info.displacement()[2] + this.getZ());
		Vec3 end = start.add(vel);
		double halfHeight = this.getSubProjectileHeight() / 2d;
		double halfWidth = this.getSubProjectileWidth() / 2d;
		HitResult hitResult = this.level.clip(new ProjectileBurstClipContext(start, end, ClipContext.Block.COLLIDER,
			ClipContext.Fluid.NONE, this, start.y - halfHeight));
		if (hitResult.getType() != HitResult.Type.MISS)
			end = hitResult.getLocation();
		AABB aabb = new AABB(start.x - halfWidth, start.y - halfHeight, start.z - halfWidth, start.x + halfWidth,
			start.y + halfHeight, start.z + halfWidth).inflate(this.getBlobSize());
		this.clipEntities(start, end, aabb.expandTowards(vel).inflate(1.0), info);
		return hitResult;
	}

	private void clipEntities(Vec3 startVec, Vec3 endVec, AABB boundingBox, SubProjectile subProjectile) {
		float inflate = this.getBlobSize() + 0.3f;
		for (Entity entity : this.level.getEntities(this, boundingBox, this::canHitEntity)) {
			AABB entityBB = entity.getBoundingBox().inflate(inflate);
			Optional<Vec3> optional = entityBB.clip(startVec, endVec);
			if (optional.isPresent())
				this.onSubProjectileHitEntity(new EntityHitResult(entity), subProjectile);
		}
	}

	@Override
	public boolean canHitEntity(Entity target) {
		return !this.clippedThisTick.contains(target) && super.canHitEntity(target);
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
		this.clippedThisTick.add(result.getEntity());
		if (!this.level.isClientSide)
			FluidBlobEffectRegistry.effectOnHitEntity(this, subProjectile, result);
		super.onSubProjectileHitEntity(result, subProjectile);
	}

	public AABB getAreaOfEffect(BlockPos pos) {
		return new AABB(pos).inflate(this.getBlobSize());
	}

}
