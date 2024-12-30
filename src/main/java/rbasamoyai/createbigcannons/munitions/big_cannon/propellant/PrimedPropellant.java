package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

/**
 * Copy of PrimedTnt but with custom behavior. Use only for single-block propellants, and extend for multiple blocks.
 */
public class PrimedPropellant extends Entity {

	private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedPropellant.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<BlockState> DATA_APPEARANCE = SynchedEntityData.defineId(PrimedPropellant.class, EntityDataSerializers.BLOCK_STATE);

	protected float explosionPower = 4;
	protected float fuze;

	public PrimedPropellant(EntityType<? extends PrimedPropellant> entityType, Level level) {
		super(entityType, level);
		this.blocksBuilding = true;
	}

	public static PrimedPropellant create(Level level, double x, double y, double z, BlockState blockState) {
		PrimedPropellant propellant = new PrimedPropellant(CBCEntityTypes.PRIMED_PROPELLANT.get(), level);
		propellant.setPos(x, y, z);
		double d = level.random.nextDouble() * (float) (Math.PI * 2);
		propellant.setDeltaMovement(-Math.sin(d) * 0.02, 0.2F, -Math.cos(d) * 0.02);
		propellant.setFuse(level.random.nextInt(10) + 5);
		propellant.setAppearance(blockState);
		propellant.xo = x;
		propellant.yo = y;
		propellant.zo = z;
		return propellant;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_FUSE_ID, 20);
		this.entityData.define(DATA_APPEARANCE, CBCBlocks.POWDER_CHARGE.getDefaultState());
	}

	@Override protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.NONE; }

	@Override public boolean isPickable() { return !this.isRemoved(); }

	@Override
	public void tick() {
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
		}

		this.move(MoverType.SELF, this.getDeltaMovement());
		this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
		if (this.onGround()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
		}

		int i = this.getFuse() - 1;
		this.setFuse(i);
		if (i <= 0) {
			this.discard();
			if (!this.level().isClientSide) {
				this.explode();
			}
		} else {
			this.updateInWaterStateAndDoFluidPushing();
			if (this.level().isClientSide) {
				this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
			}
		}
	}

	private void explode() {
		this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), this.explosionPower,
			CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putShort("Fuse", (short) this.getFuse());
		tag.put("Appearance", NbtUtils.writeBlockState(this.getAppearance()));
		tag.putFloat("ExplosionPower", this.explosionPower);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.setFuse(tag.getShort("Fuse"));
		this.setAppearance(NbtUtils.readBlockState(this.level().holderLookup(CBCRegistryUtils.getBlockRegistryKey()), tag.getCompound("Appearance")));
		this.explosionPower = tag.getFloat("ExplosionPower");
	}

	@Override protected float getEyeHeight(Pose pose, EntityDimensions dimensions) { return 0.15F; }

	public void setFuse(int life) { this.entityData.set(DATA_FUSE_ID, life); }
	public int getFuse() { return this.entityData.get(DATA_FUSE_ID); }

	public void setAppearance(BlockState state) { this.entityData.set(DATA_APPEARANCE, state); }
	public BlockState getAppearance() { return this.entityData.get(DATA_APPEARANCE); }

	public void setExplosionPower(float explosionPower) { this.explosionPower = explosionPower; }

}
