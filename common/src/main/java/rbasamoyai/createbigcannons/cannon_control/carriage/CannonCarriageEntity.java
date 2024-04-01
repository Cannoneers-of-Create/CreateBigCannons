package rbasamoyai.createbigcannons.cannon_control.carriage;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.network.protocol.game.ClientGamePacketListener;

import org.joml.Vector4f;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.goggles.IHaveEntityGoggleInformation;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.ExtendsCannonMount;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.ItemCannon;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBigCannonMaterials;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.network.ServerboundCarriageWheelPacket;

public class CannonCarriageEntity extends Entity implements ControlPitchContraption, IHaveEntityGoggleInformation {

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

	public CannonCarriageEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	public BlockState getControllerState() {
		return CBCBlocks.CANNON_CARRIAGE.getDefaultState();
	}

	@Nullable
	@Override
	public ResourceLocation getTypeId() { return CreateBigCannons.resource("cannon_carriage"); }

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

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Nullable
	@Override
	public LivingEntity getControllingPassenger() {
		return this.canTurnCannon()
			? (Player) this.getPassengers().stream().filter(Player.class::isInstance).findFirst().orElse(null)
			: this.cannonContraption.getControllingPassenger();
	}

	@Override
	public void tick() {
		super.tick();

		this.tickLerp();

		this.previousWheelState = this.getWheelState();

		if (this.isControlledByLocalInstance()) {
			this.moveCarriage();
			if (this.level().isClientSide) {
				this.controlCarriage();
				NetworkPlatform.sendToServer(new ServerboundCarriageWheelPacket(this));
			}
			this.move(MoverType.SELF, this.getDeltaMovement());
		} else {
			this.setDeltaMovement(Vec3.ZERO);
		}
		this.applyRotation();
	}

	public void tryFiringShot() {
		if (this.level() instanceof ServerLevel slevel
			&& this.cannonContraption != null
			&& this.cannonContraption.getContraption() instanceof AbstractMountedCannonContraption cannon) {
			if (this.getControllingPassenger() instanceof Player player && cannon instanceof ItemCannon itemCannon) {
				ItemStack stack = getValidStack(player, s -> s.getItem() instanceof AutocannonAmmoItem);
				ItemStack result = itemCannon.insertItemIntoCannon(stack, false);
				if (!player.isCreative() && stack.getCount() != result.getCount()) {
					stack.setCount(result.getCount());
					if (stack.isEmpty()) player.getInventory().removeItem(stack);
				}
			}
			cannon.fireShot(slevel, this.cannonContraption);
		}
	}

	public static ItemStack getValidStack(Player player, Predicate<ItemStack> pred) {
		ItemStack held = ProjectileWeaponItem.getHeldProjectile(player, pred);
		if (!held.isEmpty()) return held;
		Inventory playerInv = player.getInventory();
		for (int i = 0; i < playerInv.getContainerSize(); ++i) {
			ItemStack stack = playerInv.getItem(i);
			if (pred.test(stack)) return stack;
		}
		return ItemStack.EMPTY;
	}

	private void moveCarriage() {
		double grav = this.isNoGravity() ? 0 : this.isUnderWater() ? -7e-4d : -0.04d;
		double friction = this.isInWater() ? 0.01f : 0.05f;
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
		if (!this.hasPlayerController() || !this.onGround()) return;

		float deltaYaw = 0;

		Vector4f newState = this.getWheelState();

		float wMod = this.getWeightModifier();
		float turnRate = CBCConfigs.SERVER.cannons.carriageTurnRate.getF() * wMod;
		float speed = CBCConfigs.SERVER.cannons.carriageSpeed.getF() * wMod;

		boolean flag = this.canTurnCannon();

		if (flag) {
			if (this.inputLeft) deltaYaw -= turnRate;
			if (this.inputRight) deltaYaw += turnRate;
			this.setYRot(this.getYRot() + deltaYaw);
			float f = deltaYaw * 7;
			newState.add(f, f, f, f);
		}

		float f1 = 0;
		if (this.inputPitch && this.cannonContraption != null) {
			if (flag) {
				if (this.inputForward) f1 -= turnRate;
				if (this.inputBackward) f1 += turnRate;
				this.setXRot(Mth.clamp(this.getXRot() - f1, -this.cannonContraption.maximumDepression(), this.cannonContraption.maximumElevation()));
			}
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
		if (this.cannonContraption == null || !(this.cannonContraption.getContraption() instanceof AbstractMountedCannonContraption cannon))
			return 1;
		float weight = cannon.getWeightForStress();
		return weight <= 0.0f ? 1 : CBCBigCannonMaterials.CAST_IRON.properties().weight() * 5 / weight; // Base weight is a 5 long cast iron cannon.
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
			this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
		}

		if (this.lerpSteps > 0) {
			double d0 = this.getX() + (this.lerpX - this.getX()) / (double) this.lerpSteps;
			double d1 = this.getY() + (this.lerpY - this.getY()) / (double) this.lerpSteps;
			double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double) this.lerpSteps;
			double d3 = Mth.wrapDegrees(this.lerpYRot - (double) this.getYRot());
			this.setYRot(this.getYRot() + (float) d3 / (float) this.lerpSteps);
			this.setXRot(this.getXRot() + (float) (this.lerpXRot - (double) this.getXRot()) / (float) this.lerpSteps);
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
		Level level = this.level();

		ItemStack stack = player.getItemInHand(hand);
		if (player.isSecondaryUseActive()) {
			if (stack.isEmpty() && this.isCannonRider()) {
				if (!level.isClientSide) {
					this.setCannonRider(false);
					ItemStack resultStack = Items.SADDLE.getDefaultInstance();
					if (!player.addItem(resultStack) && !player.isCreative()) {
						ItemEntity item = player.drop(resultStack, false);
						if (item != null) {
							item.setNoPickUpDelay();
							item.setTarget(player.getUUID());
						}
					}
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
			return InteractionResult.PASS;
		}

		if (stack.isEmpty()) {
			if (this.hasPlayerController() || this.isUnderWater() || player.isUnderWater())
				return InteractionResult.PASS;
			if (level.isClientSide) return InteractionResult.SUCCESS;
			if (!this.canTurnCannon())
				return player.startRiding(this.cannonContraption) ? InteractionResult.CONSUME : InteractionResult.PASS;
			return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
		}
		if (AllItems.WRENCH.isIn(stack)) {
			if (!level.isClientSide) this.disassemble();

			Direction dir = this.getDirection();
			BlockPos placePos = this.blockPosition();

			if (level.getBlockState(placePos).getDestroySpeed(level, placePos) != -1) {
				level.destroyBlock(placePos, true);
				level.setBlock(placePos, CBCBlocks.CANNON_CARRIAGE.getDefaultState()
					.setValue(CannonCarriageBlock.FACING, dir)
					.setValue(CannonCarriageBlock.SADDLED, this.isCannonRider()), 11);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		if (stack.is(Items.SADDLE) && !this.isCannonRider()) {
			if (!level.isClientSide) {
				this.setCannonRider(true);
				if (!player.isCreative()) stack.shrink(1);
			}
			level.playSound(player, this, SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 0.5F, 1.0F);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.interact(player, hand);
	}

	protected boolean canTurnCannon() {
		return this.cannonContraption == null || this.cannonContraption.canBeTurnedByController(this);
	}

	@Override
	protected boolean canAddPassenger(Entity entity) {
		if (entity.getType() == EntityType.PLAYER) return !this.hasPlayerController();
		return entity.getType() == CBCEntityTypes.PITCH_ORIENTED_CONTRAPTION.get() && this.cannonContraption == null;
	}

	private boolean hasPlayerController() {
		return this.getControllingPassenger() != null;
	}

	public void trySettingFireRateCarriage(int fireRateAdjustment) {
		if (!this.level().isClientSide && this.cannonContraption != null && this.cannonContraption.getContraption() instanceof MountedAutocannonContraption autocannon)
			autocannon.trySettingFireRateCarriage(fireRateAdjustment);
	}

	@Override
	protected void positionRider(Entity entity, Entity.MoveFunction function) {
		if (!(entity instanceof Player)) {
			super.positionRider(entity, function);
			return;
		}
		if (!this.hasPassenger(entity)) return;

		if (this.isCannonRider()) {
			function.accept(entity, this.getX(), this.getY() + 1.375, this.getZ());
		} else {
			double yawRad = Math.toRadians(this.getYRot());
			double x = this.getX() + Math.cos(yawRad) * 1.5;
			double z = this.getZ() + Math.sin(yawRad) * 1.5;
			function.accept(entity, x, this.getY(), z);
		}
	}

	@Override
	public double getPassengersRidingOffset() {
		return 27 / 32f;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	public void disassemble() {
		if (this.cannonContraption != null) {
			this.resetContraptionToOffset();
			this.cannonContraption.stopRiding();
		}
		AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.level(), this.blockPosition());
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
		float sgn = flag ? 1 : -1;
		float d = -this.cannonContraption.maximumDepression();
		float e = this.cannonContraption.maximumElevation();
		this.cannonContraption.prevPitch = Mth.clamp(this.xRotO, d, e) * sgn;
		this.cannonContraption.pitch = Mth.clamp(this.getXRot(), d, e) * sgn;
		this.cannonContraption.prevYaw = this.yRotO;
		this.cannonContraption.yaw = this.getYRot();
	}

	@Override
	public void onPassengerTurned(Entity entity) {
		super.onPassengerTurned(entity);

		if (this.cannonContraption == entity) {
			Direction dir = this.cannonContraption.getInitialOrientation();
			boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);

			this.xRotO = flag ? this.cannonContraption.prevPitch : -this.cannonContraption.prevPitch;
			this.setXRot(flag ? this.cannonContraption.pitch : -this.cannonContraption.pitch);
			this.yRotO = this.cannonContraption.prevYaw;
			this.setYRot(this.cannonContraption.yaw);
		}
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		if (this.isInvulnerableTo(source)) return false;
		if (this.level().isClientSide || this.isRemoved()) return true;
		this.setHurtDir(-this.getHurtDir());
		this.setHurtTime(10);
		this.setDamage(this.getDamage() + damage * 10.0f);
		this.markHurt();
		this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
		boolean flag = source.getEntity() instanceof Player player && player.getAbilities().instabuild;
		if (flag || this.getDamage() > 40.0F) {
			if (!flag && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
				this.spawnAtLocation(CBCBlocks.CANNON_CARRIAGE.asStack());
				if (this.isCannonRider()) this.spawnAtLocation(Items.SADDLE);
			}
			this.disassemble();
		}
		return true;
	}

	public void setDamage(float damage) {
		this.entityData.set(DATA_ID_DAMAGE, damage);
	}

	public float getDamage() {
		return this.entityData.get(DATA_ID_DAMAGE);
	}

	public void setHurtTime(int time) {
		this.entityData.set(DATA_ID_HURT, time);
	}

	public int getHurtTime() {
		return this.entityData.get(DATA_ID_HURT);
	}

	public void setHurtDir(int dir) {
		this.entityData.set(DATA_ID_HURTDIR, dir);
	}

	public int getHurtDir() {
		return this.entityData.get(DATA_ID_HURTDIR);
	}

	public void setCannonRider(boolean cannonRider) {
		this.entityData.set(DATA_ID_RIDER, cannonRider);
	}

	public boolean isCannonRider() {
		return this.entityData.get(DATA_ID_RIDER);
	}

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
		this.setCannonRider(tag.getBoolean("CannonRider"));

		ListTag wheelStateTag = tag.getList("WheelState", Tag.TAG_FLOAT);
		this.setWheelState(new Vector4f(wheelStateTag.getFloat(0), wheelStateTag.getFloat(1), wheelStateTag.getFloat(2), wheelStateTag.getFloat(3)));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putFloat("Damage", this.getDamage());
		tag.putInt("HurtTime", this.getHurtTime());
		tag.putInt("HurtDir", this.getHurtDir());
		tag.putBoolean("CannonRider", this.isCannonRider());

		ListTag wheelStateTag = new ListTag();
		Vector4f wheelState = this.getWheelState();
		wheelStateTag.add(FloatTag.valueOf(Mth.wrapDegrees(wheelState.x())));
		wheelStateTag.add(FloatTag.valueOf(Mth.wrapDegrees(wheelState.y())));
		wheelStateTag.add(FloatTag.valueOf(Mth.wrapDegrees(wheelState.z())));
		wheelStateTag.add(FloatTag.valueOf(Mth.wrapDegrees(wheelState.w())));
		tag.put("WheelState", wheelStateTag);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

	@Override
	public boolean isAttachedTo(AbstractContraptionEntity entity) {
		return this.cannonContraption == entity;
	}

	@Override
	public void attach(PitchOrientedContraptionEntity poce) {
		if (!(poce.getContraption() instanceof AbstractMountedCannonContraption)) return;
		poce.startRiding(this);
		this.cannonContraption = poce;
		this.positionRider(poce);
	}

	@Override
	public BlockPos getDismountPositionForContraption(PitchOrientedContraptionEntity poce) {
		return this.blockPosition().relative(this.getDirection().getOpposite()).above();
	}

	@Override
	public void onStall() {
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		ExtendsCannonMount.addCannonInfoToTooltip(tooltip, this.cannonContraption);
		return true;
	}

}
