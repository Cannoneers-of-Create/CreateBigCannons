package rbasamoyai.createbigcannons.cannonmount.carriage;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.cannonmount.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannonmount.MountedCannonContraption;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;

public class CannonCarriageEntity extends Entity implements ControlPitchContraption {

	private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> DATA_ID_RIDER = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.BOOLEAN);

	private boolean inputForward;
	private boolean inputBackward;
	private boolean inputLeft;
	private boolean inputRight;
	private boolean inputPitch;

	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYRot;
	private double lerpXRot;

	private PitchOrientedContraptionEntity cannonContraption;
	
	public CannonCarriageEntity(EntityType<? extends CannonCarriageEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_ID_HURT, 0);
		this.entityData.define(DATA_ID_HURTDIR, 0);
		this.entityData.define(DATA_ID_DAMAGE, 0f);
		this.entityData.define(DATA_ID_RIDER, false);
	}

	@Override public boolean isPickable() { return !this.isRemoved(); }
	@Override public boolean isPushable() { return true; }

	@Nullable
	@Override
	public Entity getControllingPassenger() {
		return this.getPassengers().stream().filter(Player.class::isInstance).findFirst().orElse(null);
	}

	@Override
	public void tick() {
		super.tick();

		this.tickLerp();

		if (this.isControlledByLocalInstance()) {
			this.moveCarriage();
			if (this.level.isClientSide) this.controlCarriage();
			this.move(MoverType.SELF, this.getDeltaMovement());
		} else {
			this.setDeltaMovement(Vec3.ZERO);
		}

		this.applyRotation();
	}

	public void tryFiringShot() {
		if (this.level instanceof ServerLevel slevel && this.cannonContraption != null) {
			Contraption contraption = this.cannonContraption.getContraption();
			if (contraption instanceof MountedCannonContraption cannon) {
				cannon.fireShot(slevel, this.cannonContraption);
			}
		}
	}

	private void moveCarriage() {
		double grav = this.isNoGravity() ? 0 : this.isUnderWater() ? -7e-4d : -0.04d;
		double friction = this.isInWater() ? 0.9f : 0.05f;
		Vec3 oldVel = this.getDeltaMovement();
		this.setDeltaMovement(oldVel.x * friction, oldVel.y + grav, oldVel.z * friction);
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
		double yawRad = Math.toRadians(this.getYRot());
		double x = this.getX() + Math.cos(yawRad) * 1.5;
		double z = this.getZ() + Math.sin(yawRad) * 1.5;
		return new Vec3(x, this.getY() + 0.5f, z);
	}

	private void controlCarriage() {
		if (!this.hasPlayerController() || !this.isOnGround()) return;
		float deltaYaw = 0;
		if (this.inputLeft) deltaYaw -= 1;
		if (this.inputRight) deltaYaw += 1;
		this.setYRot(this.getYRot() + deltaYaw);

		float f = 0;
		if (this.inputPitch) {
			if (this.inputForward) f -= 1;
			if (this.inputBackward) f += 1;
			this.setXRot(Mth.clamp(this.getXRot() - f, -30, 30));
		} else {
			if (this.inputForward) f += 0.04f;
			if (this.inputBackward) f -= 0.005f;
			this.setDeltaMovement(this.getDeltaMovement()
					.add((double) Mth.sin(-this.getYRot() * Mth.DEG_TO_RAD) * f, 0.0d, (double) Mth.cos(this.getYRot() * Mth.DEG_TO_RAD) * f));
		}
	}

	@Override
	public void lerpTo(double lerpX, double lerpY, double lerpZ, float lerpYRot, float lerpXRot, int lerpSteps, boolean p_38305_) {
		this.lerpX = lerpX;
		this.lerpY = lerpY;
		this.lerpZ = lerpZ;
		this.lerpYRot = lerpYRot;
		this.lerpXRot = lerpXRot;
		this.lerpSteps = 10;
	}

	private void tickLerp() {
		if (this.isControlledByLocalInstance()) {
			this.lerpSteps = 0;
			this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
		}

		if (this.lerpSteps > 0) {
			double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
			double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
			double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
			double d3 = Mth.wrapDegrees(this.lerpYRot - (double)this.getYRot());
			this.setYRot(this.getYRot() + (float)d3 / (float)this.lerpSteps);
			this.setXRot(this.getXRot() + (float)(this.lerpXRot - (double)this.getXRot()) / (float)this.lerpSteps);
			--this.lerpSteps;
			this.setPos(d0, d1, d2);
			this.setRot(this.getYRot(), this.getXRot());
		}
	}

	public void setInput(boolean inputLeft, boolean inputRight, boolean inputForward, boolean inputBackward, boolean inputPitch) {
		this.inputLeft = inputLeft;
		this.inputRight = inputRight;
		this.inputForward = inputForward;
		this.inputBackward = inputBackward;
		this.inputPitch = inputPitch;
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		InteractionResult ret = super.interact(player, hand);
		if (ret.consumesAction()) return ret;

		ItemStack stack = player.getItemInHand(hand);
		if (player.isSecondaryUseActive()) {
			if (stack.isEmpty() && this.isCannonRider()) {
				if (!this.level.isClientSide) {
					this.setCannonRider(false);
					player.addItem(Items.SADDLE.getDefaultInstance());
				}
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}
			return InteractionResult.PASS;
		}

		if (stack.isEmpty()) {
			if (this.hasPlayerController() || this.isUnderWater() || player.isUnderWater()) return InteractionResult.PASS;
			if (this.level.isClientSide) return InteractionResult.SUCCESS;
			return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
		}
		if (AllItems.WRENCH.isIn(stack)) {
			if (!this.level.isClientSide) this.disassemble(false);
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		if (stack.is(Items.SADDLE) && !this.isCannonRider()) {
			if (!this.level.isClientSide) {
				this.setCannonRider(true);
				if (!player.isCreative()) stack.shrink(1);
			}
			this.level.playSound(player, this, SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 0.5F, 1.0F);
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		return super.interact(player, hand);
	}

	@Override
	protected boolean canAddPassenger(Entity entity) {
		if (entity.getType() == EntityType.PLAYER) return !this.hasPlayerController();
		return entity.getType() == CBCEntityTypes.PITCH_ORIENTED_CONTRAPTION.get() && this.cannonContraption == null;
	}

	private boolean hasPlayerController() {
		return this.getPassengers().stream().anyMatch(Player.class::isInstance);
	}

	@Override
	public void positionRider(Entity entity) {
		if (!(entity instanceof Player)) {
			super.positionRider(entity);
			return;
		}
		if (!this.hasPassenger(entity)) return;

		if (this.isCannonRider()) {
			entity.setPos(this.getX(), this.getY() + 1.375, this.getZ());
		} else {
			double yawRad = Math.toRadians(this.getYRot());
			double x = this.getX() + Math.cos(yawRad) * 1.5;
			double z = this.getZ() + Math.sin(yawRad) * 1.5;
			entity.setPos(x, this.getY(), z);
		}
	}

	@Override public double getPassengersRidingOffset() { return 27 / 32f; }

	@Override public boolean canBeCollidedWith() { return true; }

	public void disassemble(boolean destroy) {
		Direction dir = this.getDirection();
		BlockPos placePos = this.blockPosition();
		if (this.cannonContraption != null) {
			this.resetContraptionToOffset();
			this.cannonContraption.stopRiding();
		}
		if (!destroy && this.level.getBlockState(placePos).getDestroySpeed(this.level, placePos) != -1) {
			this.level.destroyBlock(placePos, true);
			this.level.setBlock(placePos, CBCBlocks.CANNON_CARRIAGE.getDefaultState()
					.setValue(CannonCarriageBlock.FACING, dir)
					.setValue(CannonCarriageBlock.SADDLED, this.isCannonRider()), 11);
		}
		AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.level, this.blockPosition());
		this.discard();
	}

	private void resetContraptionToOffset() {
		if (this.cannonContraption == null) return;
		this.setXRot(0);
		this.setYRot(this.getDirection().toYRot());
		this.applyRotation();
		Vec3 vec = Vec3.atBottomCenterOf(this.blockPosition().above());
		this.cannonContraption.setPos(vec);
	}

	public void applyRotation() {
		if (this.cannonContraption == null) return;

		Direction dir = this.cannonContraption.getInitialOrientation();
		boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
		this.cannonContraption.prevPitch = flag ? this.xRotO : -this.xRotO;
		this.cannonContraption.pitch = flag ? this.getXRot() : -this.getXRot();
		this.cannonContraption.prevYaw = this.yRotO;
		this.cannonContraption.yaw = this.getYRot();
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		if (this.isInvulnerableTo(source)) return false;
		if (this.level.isClientSide || this.isRemoved()) return true;
		this.setHurtDir(-this.getHurtDir());
		this.setHurtTime(10);
		this.setDamage(this.getDamage() + damage * 10.0f);
		this.markHurt();
		this.gameEvent(GameEvent.ENTITY_DAMAGED, source.getEntity());
		boolean flag = source.getEntity() instanceof Player player && player.getAbilities().instabuild;
		if (flag || this.getDamage() > 40.0F) {
			if (!flag && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
				this.spawnAtLocation(CBCBlocks.CANNON_CARRIAGE.asStack());
				if (this.isCannonRider()) this.spawnAtLocation(Items.SADDLE);
			}
			this.disassemble(true);
		}
		return true;
	}

	public void setDamage(float damage) { this.entityData.set(DATA_ID_DAMAGE, damage); }
	public float getDamage() { return this.entityData.get(DATA_ID_DAMAGE); }

	public void setHurtTime(int time) { this.entityData.set(DATA_ID_HURT, time); }
	public int getHurtTime() { return this.entityData.get(DATA_ID_HURT); }

	public void setHurtDir(int dir) { this.entityData.set(DATA_ID_HURTDIR, dir); }
	public int getHurtDir() { return this.entityData.get(DATA_ID_HURTDIR); }

	public void setCannonRider(boolean cannonRider) { this.entityData.set(DATA_ID_RIDER, cannonRider); }
	public boolean isCannonRider() { return this.entityData.get(DATA_ID_RIDER); }

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.setDamage(tag.getFloat("Damage"));
		this.setHurtTime(tag.getInt("HurtTime"));
		this.setHurtDir(tag.getInt("HurtDir"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putFloat("Damage", this.getDamage());
		tag.putInt("HurtTime", this.getHurtTime());
		tag.putInt("HurtDir", this.getHurtDir());
	}

	@Override public Packet<?> getAddEntityPacket() { return new ClientboundAddEntityPacket(this); }

	public static void build(EntityType.Builder<? extends CannonCarriageEntity> builder) {
		builder.setTrackingRange(8)
				.fireImmune()
				.setShouldReceiveVelocityUpdates(true)
				.sized(1.5f, 1.5f);
	}

	@Override public boolean isAttachedTo(AbstractContraptionEntity entity) { return this.cannonContraption == entity; }

	@Override
	public void attach(PitchOrientedContraptionEntity poce) {
		if (!(poce.getContraption() instanceof MountedCannonContraption)) return;
		poce.startRiding(this);
		this.cannonContraption = poce;
		this.positionRider(poce);
	}

	@Override public void onStall() {}

}
