package rbasamoyai.createbigcannons.cannonmount.carriage;

import com.mojang.math.Vector4f;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
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
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.network.CBCNetwork;
import rbasamoyai.createbigcannons.network.ServerboundCarriageWheelPacket;

public class CannonCarriageEntity extends Entity implements ControlPitchContraption {

	private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> DATA_ID_RIDER = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> DATA_ID_WHEEL_LF = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ID_WHEEL_RF = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ID_WHEEL_LB = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_ID_WHEEL_RB = SynchedEntityData.defineId(CannonCarriageEntity.class, EntityDataSerializers.FLOAT);

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
	public Vector4f previousWheelState = new Vector4f(0, 0, 0, 0);

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
		this.entityData.define(DATA_ID_WHEEL_LF, 0f);
		this.entityData.define(DATA_ID_WHEEL_RF, 0f);
		this.entityData.define(DATA_ID_WHEEL_LB, 0f);
		this.entityData.define(DATA_ID_WHEEL_RB, 0f);
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

		this.previousWheelState = this.getWheelState();

		if (this.isControlledByLocalInstance()) {
			this.moveCarriage();
			if (this.level.isClientSide) {
				this.controlCarriage();
				CBCNetwork.INSTANCE.sendToServer(new ServerboundCarriageWheelPacket(this));
			}
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

		Vector4f newState = this.getWheelState();

		float wMod = this.getWeightModifier();
		float turnRate = CBCConfigs.SERVER.cannons.carriageTurnRate.getF() * wMod;
		float speed = CBCConfigs.SERVER.cannons.carriageSpeed.getF() * wMod;

		if (this.inputLeft) deltaYaw -= turnRate;
		if (this.inputRight) deltaYaw += turnRate;
		this.setYRot(this.getYRot() + deltaYaw);
		float f = deltaYaw * 7;
		newState.add(f, f, f, f);

		float f1 = 0;
		if (this.inputPitch) {
			if (this.inputForward) f1 -= turnRate;
			if (this.inputBackward) f1 += turnRate;
			this.setXRot(Mth.clamp(this.getXRot() - f1, -30, 30));
		} else {
			if (this.inputForward) f1 += speed;
			if (this.inputBackward) f1 -= speed * 0.5f;
			this.setDeltaMovement(this.getDeltaMovement()
					.add((double) Mth.sin(-this.getYRot() * Mth.DEG_TO_RAD) * f1, 0.0d, (double) Mth.cos(this.getYRot() * Mth.DEG_TO_RAD) * f1));
			float f2 = f1 * 200;
			newState.add(f2, f2, -f2, -f2);
		}
		this.setWheelState(newState);
	}

	protected float getWeightModifier() {
		if (!CBCConfigs.SERVER.cannons.cannonWeightAffectsCarriageSpeed.get()) return 1;
		if (this.cannonContraption == null || !(this.cannonContraption.getContraption() instanceof MountedCannonContraption cannon)) return 1;
		float weight = cannon.getWeightForStress();
		return weight <= 0.0f ? 1 : CannonMaterial.CAST_IRON.weight() * 5 / weight; // Base weight is a 5 long cast iron cannon.
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
			if (!this.level.isClientSide) this.disassemble();

			Direction dir = this.getDirection();
			BlockPos placePos = this.blockPosition();

			if (this.level.getBlockState(placePos).getDestroySpeed(this.level, placePos) != -1) {
				this.level.destroyBlock(placePos, true);
				this.level.setBlock(placePos, CBCBlocks.CANNON_CARRIAGE.getDefaultState()
						.setValue(CannonCarriageBlock.FACING, dir)
						.setValue(CannonCarriageBlock.SADDLED, this.isCannonRider()), 11);
			}

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

	public void disassemble() {
		if (this.cannonContraption != null) {
			this.resetContraptionToOffset();
			this.cannonContraption.stopRiding();
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
			this.disassemble();
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

	public void setWheelState(Vector4f vec) {
		this.entityData.set(DATA_ID_WHEEL_LF, vec.x());
		this.entityData.set(DATA_ID_WHEEL_RF, vec.y());
		this.entityData.set(DATA_ID_WHEEL_LB, vec.z());
		this.entityData.set(DATA_ID_WHEEL_RB, vec.w());
	}

	public Vector4f getWheelState() {
		return new Vector4f(this.entityData.get(DATA_ID_WHEEL_LF), this.entityData.get(DATA_ID_WHEEL_RF), this.entityData.get(DATA_ID_WHEEL_LB), this.entityData.get(DATA_ID_WHEEL_RB));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.setDamage(tag.getFloat("Damage"));
		this.setHurtTime(tag.getInt("HurtTime"));
		this.setHurtDir(tag.getInt("HurtDir"));

		ListTag wheelStateTag = tag.getList("WheelState", Tag.TAG_FLOAT);
		this.setWheelState(new Vector4f(wheelStateTag.getFloat(0), wheelStateTag.getFloat(1), wheelStateTag.getFloat(2), wheelStateTag.getFloat(3)));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putFloat("Damage", this.getDamage());
		tag.putInt("HurtTime", this.getHurtTime());
		tag.putInt("HurtDir", this.getHurtDir());

		ListTag wheelStateTag = new ListTag();
		Vector4f wheelState = this.getWheelState();
		wheelStateTag.add(FloatTag.valueOf(Mth.wrapDegrees(wheelState.x())));
		wheelStateTag.add(FloatTag.valueOf(Mth.wrapDegrees(wheelState.y())));
		wheelStateTag.add(FloatTag.valueOf(Mth.wrapDegrees(wheelState.z())));
		wheelStateTag.add(FloatTag.valueOf(Mth.wrapDegrees(wheelState.w())));
		tag.put("WheelState", wheelStateTag);
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
