package rbasamoyai.createbigcannons.cannon_control.contraption;

import javax.annotation.Nullable;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.base.PreciseProjectile;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.IAutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;

public class PitchOrientedContraptionEntity extends OrientedContraptionEntity implements PreciseProjectile { // Weird naming, will change when refactor to RPL

	private BlockPos controllerPos;
	private boolean updatesOwnRotation;

	public PitchOrientedContraptionEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	public static PitchOrientedContraptionEntity create(Level level, Contraption contraption, Direction initialOrientation, boolean updatesOwnRotation) {
		PitchOrientedContraptionEntity entity = CBCEntityTypes.PITCH_ORIENTED_CONTRAPTION.create(level);

		entity.setContraption(contraption);
		entity.setInitialOrientation(initialOrientation);
		entity.startAtInitialYaw();
		entity.updatesOwnRotation = updatesOwnRotation;
		return entity;
	}

	public static PitchOrientedContraptionEntity create(Level level, Contraption contraption, Direction initialOrientation, ControlPitchContraption.Block block) {
		PitchOrientedContraptionEntity poce = create(level, contraption, initialOrientation, true);
		poce.controllerPos = block.getControllerBlockPos();
		return poce;
	}

	@Override
	protected void readAdditional(CompoundTag compound, boolean spawnPacket) {
		super.readAdditional(compound, spawnPacket);
		if (compound.contains("ControllerRelative"))
			this.controllerPos = NbtUtils.readBlockPos(compound.getCompound("ControllerRelative")).offset(this.blockPosition());
		else if (this.level().getBlockEntity(this.blockPosition().below(2)) instanceof ControlPitchContraption.Block controller && !this.isPassenger()) {
			// Legacy, cannon mount
			this.controllerPos = controller.getControllerBlockPos();
		}
		this.updatesOwnRotation = compound.getBoolean("UpdatesOwnRotation");
	}

	@Override
	protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
		super.writeAdditional(compound, spawnPacket);
		if (this.controllerPos != null)
			compound.put("ControllerRelative", NbtUtils.writeBlockPos(controllerPos.subtract(blockPosition())));
		compound.putBoolean("UpdatesOwnRotation", this.updatesOwnRotation);
	}

	protected ControlPitchContraption getController() {
		if (this.controllerPos != null) {
			if (!this.level().isLoaded(this.controllerPos)) return null;
			return this.level().getBlockEntity(this.controllerPos) instanceof ControlPitchContraption controller ? controller : null;
		}
		return this.getVehicle() instanceof ControlPitchContraption controller ? controller : null;
	}

	@Override
	protected void tickContraption() {
		super.tickContraption();

		if (this.updatesOwnRotation) {
			this.prevYaw = this.yaw;
			this.prevPitch = this.pitch;
		}

		this.contraption.anchor = this.blockPosition();
		Level level = this.level();
		if (this.contraption instanceof AbstractMountedCannonContraption mounted) mounted.tick(level, this);

		ControlPitchContraption controller = this.getController();
		if (controller == null) {
			if (!level.isClientSide) this.disassemble();
			return;
		}
		if (!controller.isAttachedTo(this)) {
			controller.attach(this);
			if (level.isClientSide) this.setPos(this.getX(), this.getY(), this.getZ());
		}
	}

	public void handleAnimation() {
		if (this.contraption instanceof AbstractMountedCannonContraption mounted) mounted.animate();
	}

	public float maximumDepression() {
		return this.contraption instanceof AbstractMountedCannonContraption cannon && this.getController() != null
			? cannon.maximumDepression(this.getController()) : 90f;
	}

	public float maximumElevation() {
		return this.contraption instanceof AbstractMountedCannonContraption cannon && this.getController() != null
			? cannon.maximumElevation(this.getController()) : 90f;
	}

	@Override
	protected void onContraptionStalled() {
		ControlPitchContraption controller = this.getController();
		if (controller != null) controller.onStall();
		super.onContraptionStalled();
	}

	@Override
	public ContraptionRotationState getRotationState() {
		return new CBCContraptionRotationState(this);
	}

	@Override
	protected boolean updateOrientation(boolean rotationLock, boolean wasStalled, Entity riding, boolean isOnCoupling) {
		return false;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		float prevYaw = this.yaw;
		super.onSyncedDataUpdated(key);
		this.yaw = prevYaw;
	}

	@Override
	public Vec3 applyRotation(Vec3 localPos, float partialTicks) {
		localPos = VecHelper.rotate(localPos, this.getViewXRot(partialTicks), this.getInitialOrientation().getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
		localPos = VecHelper.rotate(localPos, this.getViewYRot(partialTicks), Direction.Axis.Y);
		localPos = VecHelper.rotate(localPos, this.getInitialYaw(), Direction.Axis.Y);
		return localPos;
	}

	@Override
	public Vec3 reverseRotation(Vec3 localPos, float partialTicks) {
		localPos = VecHelper.rotate(localPos, -this.getInitialYaw(), Direction.Axis.Y);
		localPos = VecHelper.rotate(localPos, -this.getViewYRot(partialTicks), Direction.Axis.Y);
		localPos = VecHelper.rotate(localPos, -this.getViewXRot(partialTicks), this.getInitialOrientation().getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
		return localPos;
	}

	public float getRotationCoefficient() {
		return this.contraption instanceof AbstractMountedCannonContraption cannon ? Math.max(1 / cannon.getWeightForStress(), 0.1f) : 0.1f;
	}

	@Override
	public void onPassengerTurned(Entity entity) {
		if (this.contraption instanceof AbstractMountedCannonContraption cannon && cannon.canBeTurnedByPassenger(entity)) {
			Direction dir = this.getInitialOrientation();
			boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
			this.prevPitch = flag ? -entity.xRotO : entity.xRotO;
			this.pitch = flag ? -entity.getXRot() : entity.getXRot();
			this.prevYaw = entity.yRotO;
			this.yaw = entity.getYRot();

			entity.setYBodyRot(entity.getYRot());
			if (CBCEntityTypes.CANNON_CARRIAGE.is(this.getVehicle())) {
				this.getVehicle().onPassengerTurned(this);
			} else if (this.getController() instanceof CannonMountBlockEntity mount) {
				mount.applyHandRotation();
				this.xRotO = this.prevPitch;
				this.setXRot(this.pitch);
				this.yRotO = this.prevYaw;
				this.setYRot(this.yaw);
			}
		}
	}

	@Override
	public void addSittingPassenger(Entity passenger, int seatIndex) {
		if (passenger instanceof Mob mob && mob.getLeashHolder() instanceof Player player) {
			this.addSittingPassenger(player, seatIndex);
		}
		super.addSittingPassenger(passenger, seatIndex);
	}

	@Override
	protected void addPassenger(Entity entity) {
		super.addPassenger(entity);
		Direction dir = this.getInitialOrientation();
		boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);

		entity.setXRot(flag ? -this.pitch : this.prevPitch);
		entity.setYRot(this.yaw);
		entity.xRotO = flag ? -this.prevPitch : this.prevPitch;
		entity.yRotO = this.prevYaw;
	}

	@Override
	public void positionRider(Entity passenger, Entity.MoveFunction function) {
		if (!this.hasPassenger(passenger)) return;
		Vec3 transformedVector = this.getPassengerPosition(passenger, 1);
		transformedVector = this.processRiderPositionHook(passenger, transformedVector);
		if (transformedVector == null) return;
		passenger.setPos(transformedVector.x, transformedVector.y, transformedVector.z);
	}

	/**
	 * Mixin to process rider (e.g. autocannon rider).
	 *
	 * @param passenger
	 * @param original
	 * @return null to override vanilla CBC positioning
	 */
	@Nullable
	protected Vec3 processRiderPositionHook(Entity passenger, @Nullable Vec3 original) {
		return original;
	}

	@Override
	public Vec3 getPassengerPosition(Entity passenger, float partialTicks) {
		if (passenger != this.getControllingPassenger() || !(this.contraption instanceof AbstractMountedCannonContraption cannon))
			return super.getPassengerPosition(passenger, partialTicks);

		BlockPos seat = cannon.getSeatPos(passenger);
		if (seat == null) return null;
		Vec3 normal = new Vec3(this.getInitialOrientation().step());
		return this.toGlobalVector(Vec3.atCenterOf(seat).add(normal.scale(-0.25f)), partialTicks);
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity entityLiving) {
		ControlPitchContraption controller = this.getController();
		Vec3 superResult = super.getDismountLocationForPassenger(entityLiving); // Call to process other stuff
		return controller != null ? Vec3.atCenterOf(controller.getDismountPositionForContraption(this)) : superResult;
	}

	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
	}

	public BlockPos getSeatPos(Entity passenger) {
		return ((AbstractMountedCannonContraption) this.contraption).getSeatPos(passenger);
	}

	@Nullable
	@Override
	public LivingEntity getControllingPassenger() {
		return this.getFirstPassenger() instanceof Player player ? player : null;
	}

	public boolean canBeTurnedByController(ControlPitchContraption control) {
		return this.contraption instanceof AbstractMountedCannonContraption cannon && cannon.canBeTurnedByController(control);
	}

	public void tryFiringShot() {
		if (this.contraption instanceof AbstractMountedCannonContraption cannon && this.level() instanceof ServerLevel slevel) {
			cannon.fireShot(slevel, this, this.getController());
		}
	}

	@Override
	public boolean handlePlayerInteraction(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand) {
		if (this.contraption instanceof MountedBigCannonContraption cannon && interactionHand == InteractionHand.MAIN_HAND) {
			BlockEntity be = this.contraption.presentBlockEntities.get(localPos);
			StructureBlockInfo info = this.contraption.getBlocks().get(localPos);

			if (info.state().getBlock() instanceof BigCannonBlock cBlock
				&& be instanceof IBigCannonBlockEntity cbe
				&& cBlock.onInteractWhileAssembled(player, localPos, side, interactionHand, this.level(), cannon,
				(BlockEntity & IBigCannonBlockEntity) cbe, info, this)) {
				return true;
			}
		} else if (this.contraption instanceof MountedAutocannonContraption autocannon && interactionHand == InteractionHand.MAIN_HAND) {
			BlockEntity be = this.contraption.presentBlockEntities.get(localPos);
			StructureBlockInfo info = this.contraption.getBlocks().get(localPos);

			if (info.state().getBlock() instanceof AutocannonBlock cBlock
				&& be instanceof IAutocannonBlockEntity cbe
				&& cBlock.onInteractWhileAssembled(player, localPos, side, interactionHand, this.level(), autocannon,
				(BlockEntity & IAutocannonBlockEntity) cbe, info, this)) {
				return true;
			}
		}
		return super.handlePlayerInteraction(player, localPos, side, interactionHand);
	}

}
