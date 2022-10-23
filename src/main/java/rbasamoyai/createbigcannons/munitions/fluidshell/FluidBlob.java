package rbasamoyai.createbigcannons.munitions.fluidshell;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.contraptions.particle.AirParticleData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.shrapnel.Shrapnel;

public class FluidBlob extends Shrapnel {
	
	public static final EntityDataSerializer<FluidStack> FLUID_STACK_SERIALIZER = new EntityDataSerializer<>() {
		@Override public void write(FriendlyByteBuf buf, FluidStack fluid) { buf.writeFluidStack(fluid); }
		@Override public FluidStack read(FriendlyByteBuf buf) { return buf.readFluidStack(); }
		@Override public FluidStack copy(FluidStack fluid) { return fluid.copy(); }
	};
	
	private static final EntityDataAccessor<Byte> BLOB_SIZE = SynchedEntityData.defineId(FluidBlob.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<FluidStack> FLUID = SynchedEntityData.defineId(FluidBlob.class, FLUID_STACK_SERIALIZER);
	
	public FluidBlob(EntityType<? extends FluidBlob> type, Level level) {
		super(type, level);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(BLOB_SIZE, (byte) 0);
		this.entityData.define(FLUID, FluidStack.EMPTY);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (!this.getFluidStack().isEmpty()) tag.put("Fluid", this.getFluidStack().writeToNBT(new CompoundTag()));
		tag.putByte("Size", this.getBlobSize());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setFluidBase(tag.contains("Fluid") ? FluidStack.loadFluidStackFromNBT(tag.getCompound("Fluid")) : FluidStack.EMPTY);
		this.setBlobSize(tag.getByte("Size"));
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (!this.getFluidStack().isEmpty()) {
			Vec3 vel = this.getDeltaMovement();
			this.level.addParticle(new FluidBlobParticleData(this.getBlobSize() / 4 + 1, this.getFluidStack().copy()), this.getX(), this.getY(), this.getZ(), vel.x, vel.y, vel.z);
		}
	}
	
	public FluidStack getFluidStack() { return this.entityData.get(FLUID); }
	public void setFluidBase(FluidStack fluid) { this.entityData.set(FLUID, fluid); }
	
	public void setFluidStack(FluidStack fluid) {
		this.setFluidBase(fluid);
		this.setBlobSize((byte)(this.getFluidStack().getAmount() / mbPerAoeRadius()));
	}
	
	protected void setBlobSize(byte size) {
		this.entityData.set(BLOB_SIZE, size < 0 ? (byte) 0 : size);
	}
	
	public byte getBlobSize() {
		return this.entityData.get(BLOB_SIZE);
	}
	
	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level.isClientSide) {
			this.onHitPos(result.getBlockPos().relative(result.getDirection()));
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (result.getEntity().getType() == this.getType()) {
			return;
		}
		Fluid fluidType = this.getFluidStack().getFluid();
		Entity entity = result.getEntity();
		if (fluidType == Fluids.WATER) {
			entity.clearFire();
		} else if (fluidType == Fluids.LAVA) {
			entity.setSecondsOnFire(100);
		}
		if (!this.level.isClientSide) {
			this.onHitPos(entity.blockPosition());
		}
	}
	
	protected void onHitPos(BlockPos pos) {
		if (this.level.isClientSide) return;
		Fluid fluidType = this.getFluidStack().getFluid();
		if (fluidType == Fluids.WATER) {
			this.douseFire(pos);
		} else if (fluidType == Fluids.LAVA) {
			this.spawnFire(pos);
		} else if (AllFluids.POTION.is(fluidType)) {
			this.spawnAreaEffectCloud(pos);
		}
	}
	
	protected AABB getAreaOfEffect(BlockPos pos) {
		return new AABB(pos).inflate(this.getBlobSize());
	}
	
	private void douseFire(BlockPos root) {
		float chance = getBlockAffectChance();
		AABB bounds = this.getAreaOfEffect(root);
		BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
		BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
		for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
			if (chance == 0 || this.random.nextFloat() > chance) continue;
			BlockState state = this.level.getBlockState(pos);
			if (state.is(BlockTags.FIRE)) {
				this.level.removeBlock(pos, false);
			} else if (AbstractCandleBlock.isLit(state)) {
				AbstractCandleBlock.extinguish(null, state, this.level, pos);
			} else if (CampfireBlock.isLitCampfire(state)) {
				this.level.levelEvent(null, 1009, pos, 0);
				CampfireBlock.dowse(this.getOwner(), this.level, pos, state);
				this.level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
			}
		}
	}
	
	private void spawnFire(BlockPos root) {
		float chance = getBlockAffectChance();
		AABB bounds = this.getAreaOfEffect(root);
		BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
		BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
		for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
			if (chance > 0 && this.random.nextFloat() <= chance && this.level.isEmptyBlock(pos)) {
				this.level.setBlockAndUpdate(pos, BaseFireBlock.getState(this.level, pos));
			}
		}
	}
	
	public static float getBlockAffectChance() { return CBCConfigs.SERVER.munitions.fluidBlobBlockAffectChance.getF(); }
	
	private void spawnAreaEffectCloud(BlockPos pos) {
		CompoundTag tag = this.getFluidStack().getTag();
		
		AreaEffectCloud aec = EntityType.AREA_EFFECT_CLOUD.create(this.level);
		aec.setPos(Vec3.atCenterOf(pos));
		aec.setRadius(this.getBlobSize() * 2);
		aec.setRadiusOnUse(-0.5f);
		aec.setWaitTime(10);
		aec.setRadiusPerTick(-aec.getRadius() / (float) aec.getDuration());
		aec.setPotion(PotionUtils.getPotion(tag));
		
		for (MobEffectInstance effect : PotionUtils.getAllEffects(tag)) {
			aec.addEffect(new MobEffectInstance(effect));
		}
		
		aec.setFixedColor(PotionUtils.getColor(PotionUtils.getAllEffects(tag)) | 0xff000000);
		this.level.addFreshEntity(aec);
	}
	
	@Override
	protected ParticleOptions getTrailParticle() {
		return new AirParticleData();
	}
	
	@Override protected boolean canDestroyBlock(BlockState state) { return false; }
	@Override public float getDamage() { return 0; }
	@Override protected double getGravity() { return -0.3d;}

	public static int mbPerAoeRadius() {
		return CBCConfigs.SERVER.munitions.mbPerAoeRadius.get();
	}
	
}
