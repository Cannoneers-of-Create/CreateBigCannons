package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import com.simibubi.create.foundation.particle.AirParticleData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCDataSerializers;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;

public class FluidBlob extends Shrapnel {
	private static final EntityDataAccessor<Byte> BLOB_SIZE = SynchedEntityData.defineId(FluidBlob.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<EndFluidStack> FLUID_STACK = SynchedEntityData.defineId(FluidBlob.class, CBCDataSerializers.FLUID_STACK_SERIALIZER);

	public FluidBlob(EntityType<? extends FluidBlob> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(BLOB_SIZE, (byte) 0);
		this.entityData.define(FLUID_STACK, EndFluidStack.EMPTY);
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

	public void setFluidStack(EndFluidStack fstack) {
		this.entityData.set(FLUID_STACK, fstack);
	}

	public EndFluidStack getFluidStack() {
		return this.entityData.get(FLUID_STACK);
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.getFluidStack().isEmpty()) {
			Vec3 vel = this.getDeltaMovement();
			this.level.addParticle(new FluidBlobParticleData(this.getBlobSize() * 0.25f + 1, this.getFluidStack().copy()), this.getX(), this.getY(), this.getZ(), vel.x, vel.y, vel.z);
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		if (!this.level.isClientSide) FluidBlobEffectRegistry.effectOnHitBlock(this, result);
		super.onHitBlock(result);
	}

	@Override
	protected void onHit(HitResult result) {
		if (!this.level.isClientSide) FluidBlobEffectRegistry.effectOnAllHit(this, result);
		super.onHit(result);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (result.getEntity().getType() == this.getType()) return;
		if (!this.level.isClientSide) FluidBlobEffectRegistry.effectOnHitEntity(this, result);
	}

	public AABB getAreaOfEffect(BlockPos pos) {
		return new AABB(pos).inflate(this.getBlobSize());
	}

	public static float getBlockAffectChance() {
		return CBCConfigs.SERVER.munitions.fluidBlobBlockAffectChance.getF();
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return new AirParticleData();
	}

	@Override
	protected boolean canDestroyBlock(BlockState state) {
		return false;
	}

}
