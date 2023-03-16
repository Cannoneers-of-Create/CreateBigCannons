package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.*;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;

public class FluidBlob extends Shrapnel {

	public static final EntityDataSerializer<EndFluidStack> FLUID_STACK_SERIALIZER = new EntityDataSerializer<>() {
		@Override public void write(FriendlyByteBuf buf, EndFluidStack fluid) { fluid.writeBuf(buf); }
		@Override public EndFluidStack read(FriendlyByteBuf buf) { return EndFluidStack.readBuf(buf); }
		@Override public EndFluidStack copy(EndFluidStack fluid) { return fluid.copy(); }
	};

	private static final EntityDataAccessor<Byte> BLOB_SIZE = SynchedEntityData.defineId(FluidBlob.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<EndFluidStack> FLUID_STACK = SynchedEntityData.defineId(FluidBlob.class, FLUID_STACK_SERIALIZER);
	
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

	public void setFluidStack(EndFluidStack fstack) { this.entityData.set(FLUID_STACK, fstack); }
	public EndFluidStack getFluidStack() { return this.entityData.get(FLUID_STACK); }

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
		FluidBlobEffectRegistry.effectOnHitBlock(this, result);
		super.onHitBlock(result);
	}

	@Override
	protected void onHit(HitResult result) {
		FluidBlobEffectRegistry.effectOnAllHit(this, result);
		super.onHit(result);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (result.getEntity().getType() == this.getType()) return;
		FluidBlobEffectRegistry.effectOnHitEntity(this, result);
	}
	
	public AABB getAreaOfEffect(BlockPos pos) {
		return new AABB(pos).inflate(this.getBlobSize());
	}
	
	public static float getBlockAffectChance() { return CBCConfigs.SERVER.munitions.fluidBlobBlockAffectChance.getF(); }
	
	@Override
	protected ParticleOptions getTrailParticle() {
		return new AirParticleData();
	}
	
	@Override protected boolean canDestroyBlock(BlockState state) { return false; }

	@Override protected float getProjectileMass() { return 0; }

	@Override public float getDamage() { return 0; }
	@Override protected double getGravity() { return -0.3d; }

	public static void registerDefaultBlobEffects() {
		FluidBlobEffectRegistry.registerHitEntity(Fluids.WATER, FluidBlob::waterHitEntity);
		FluidBlobEffectRegistry.registerHitEntity(Fluids.LAVA, FluidBlob::lavaHitEntity);
		FluidBlobEffectRegistry.registerHitEntity(AllFluids.POTION.get(), FluidBlob::potionHitEntity);

		FluidBlobEffectRegistry.registerHitBlock(Fluids.WATER, FluidBlob::waterHitBlock);
		FluidBlobEffectRegistry.registerHitBlock(Fluids.LAVA, FluidBlob::lavaHitBlock);
		FluidBlobEffectRegistry.registerHitBlock(AllFluids.POTION.get(), FluidBlob::potionHitBlock);
	}

	public static void waterHitEntity(EndFluidStack fstack, FluidBlob blob, Level level, EntityHitResult result) {
		Entity entity = result.getEntity();
		entity.clearFire();
		if (!level.isClientSide) douseFire(entity.blockPosition(), blob, level);
	}

	public static void lavaHitEntity(EndFluidStack fstack, FluidBlob blob, Level level, EntityHitResult result) {
		Entity entity = result.getEntity();
		entity.setSecondsOnFire(100);
		if (!level.isClientSide) spawnFire(entity.blockPosition(), blob, level);
	}

	public static void potionHitEntity(EndFluidStack fstack, FluidBlob blob, Level level, EntityHitResult result) {
		if (!level.isClientSide) spawnAreaEffectCloud(result.getEntity().blockPosition(), blob, level);
	}

	public static void waterHitBlock(EndFluidStack fstack, FluidBlob blob, Level level, BlockHitResult result) {
		if (!level.isClientSide) douseFire(result.getBlockPos().relative(result.getDirection()), blob, level);
	}

	public static void lavaHitBlock(EndFluidStack fstack, FluidBlob blob, Level level, BlockHitResult result) {
		if (!level.isClientSide) spawnFire(result.getBlockPos().relative(result.getDirection()), blob, level);
	}

	public static void potionHitBlock(EndFluidStack fstack, FluidBlob blob, Level level, BlockHitResult result) {
		if (!level.isClientSide) spawnAreaEffectCloud(result.getBlockPos().relative(result.getDirection()), blob, level);
	}

	public static void douseFire(BlockPos root, FluidBlob blob, Level level) {
		float chance = getBlockAffectChance();
		AABB bounds = blob.getAreaOfEffect(root);
		BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
		BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
		for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
			if (chance == 0 || blob.random.nextFloat() > chance) continue;
			BlockState state = level.getBlockState(pos);
			if (state.is(BlockTags.FIRE)) {
				level.removeBlock(pos, false);
			} else if (AbstractCandleBlock.isLit(state)) {
				AbstractCandleBlock.extinguish(null, state, level, pos);
			} else if (CampfireBlock.isLitCampfire(state)) {
				level.levelEvent(null, 1009, pos, 0);
				CampfireBlock.dowse(blob.getOwner(), level, pos, state);
				level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
			}
		}
	}

	public static void spawnFire(BlockPos root, FluidBlob blob, Level level) {
		float chance = getBlockAffectChance();
		AABB bounds = blob.getAreaOfEffect(root);
		BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
		BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
		for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
			if (chance > 0 && level.getRandom().nextFloat() <= chance && level.isEmptyBlock(pos)) {
				level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
			}
		}
	}

	public static void spawnAreaEffectCloud(BlockPos pos, FluidBlob blob, Level level) {
		CompoundTag tag = blob.getFluidStack().data();

		AreaEffectCloud aec = EntityType.AREA_EFFECT_CLOUD.create(level);
		aec.setPos(Vec3.atCenterOf(pos));
		aec.setRadius(blob.getBlobSize() * 2);
		aec.setRadiusOnUse(-0.5f);
		aec.setWaitTime(10);
		aec.setRadiusPerTick(-aec.getRadius() / (float) aec.getDuration());
		aec.setPotion(PotionUtils.getPotion(tag));

		for (MobEffectInstance effect : PotionUtils.getAllEffects(tag)) {
			aec.addEffect(new MobEffectInstance(effect));
		}

		aec.setFixedColor(PotionUtils.getColor(PotionUtils.getAllEffects(tag)) | 0xff000000);
		level.addFreshEntity(aec);
	}
	
}
