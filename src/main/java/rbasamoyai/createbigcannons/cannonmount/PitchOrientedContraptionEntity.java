package rbasamoyai.createbigcannons.cannonmount;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.OrientedContraptionEntity;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.CBCEntityTypes;

public class PitchOrientedContraptionEntity extends OrientedContraptionEntity {

	private BlockPos controllerPos;
	private boolean updatesOwnRotation = true;
	private LazyOptional<IItemHandler> itemOptional;

	public PitchOrientedContraptionEntity(EntityType<? extends PitchOrientedContraptionEntity> type, Level level) {
		super(type, level);
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (this.itemOptional == null) this.itemOptional = this.contraption instanceof AbstractMountedCannonContraption cannon ? cannon.getItemOptional() : LazyOptional.empty();
			return this.itemOptional.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if (this.itemOptional != null) this.itemOptional.invalidate();
	}

	public static PitchOrientedContraptionEntity create(Level level, Contraption contraption, Direction initialOrientation, boolean updatesOwnRotation) {
		PitchOrientedContraptionEntity entity = new PitchOrientedContraptionEntity(CBCEntityTypes.PITCH_ORIENTED_CONTRAPTION.get(), level);

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
		if (compound.contains("ControllerRelative")) this.controllerPos = NbtUtils.readBlockPos(compound.getCompound("ControllerRelative")).offset(this.blockPosition());
		else if (this.level.getBlockEntity(this.blockPosition().below(2)) instanceof ControlPitchContraption.Block controller && !this.isPassenger()) {
			// Legacy, cannon mount
			this.controllerPos = controller.getControllerBlockPos();
		}
		this.updatesOwnRotation = compound.getBoolean("UpdatesOwnRotation");
	}

	@Override
	protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
		super.writeAdditional(compound, spawnPacket);
		if (this.controllerPos != null) compound.put("ControllerRelative", NbtUtils.writeBlockPos(controllerPos.subtract(blockPosition())));
		compound.putBoolean("UpdatesOwnRotation", this.updatesOwnRotation);
	}

	protected ControlPitchContraption getController() {
		if (this.controllerPos != null) {
			if (!this.level.isLoaded(this.controllerPos)) return null;
			return this.level.getBlockEntity(this.controllerPos) instanceof ControlPitchContraption controller ? controller : null;
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
		if (this.contraption instanceof AbstractMountedCannonContraption mounted) mounted.tick(this.level, this);

		ControlPitchContraption controller = this.getController();
		if (controller == null) {
			if (!this.level.isClientSide) this.disassemble();
			return;
		}
		if (!controller.isAttachedTo(this)) {
			controller.attach(this);
			if (this.level.isClientSide) this.setPos(this.getX(), this.getY(), this.getZ());
		}

		if (!this.level.isClientSide && !this.canBeTurnedByController(this.getController())) {
			this.yaw = this.getYRot();
			this.pitch = this.getXRot();
		}
	}

	public void handleAnimation() {
		if (this.contraption instanceof AbstractMountedCannonContraption mounted) mounted.animate();
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
	public void applyLocalTransforms(PoseStack stack, float partialTicks) {
		float initialYaw = this.getInitialYaw();
		float pitch = this.getViewXRot(partialTicks);
		float yaw = this.getViewYRot(partialTicks) + initialYaw;
		
		stack.translate(-0.5f, 0.0f, -0.5f);
		
		TransformStack tstack = TransformStack.cast(stack)
				.nudge(this.getId())
				.centre()
				.rotateY(yaw);
		
		if (this.getInitialOrientation().getAxis() == Direction.Axis.X) {
			tstack.rotateZ(pitch);
		} else {
			tstack.rotateX(pitch);
		}
		tstack.unCentre();
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
		return 0.75f;
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
		}
	}

	@Override
	public void addSittingPassenger(Entity passenger, int seatIndex) {
		if (passenger instanceof Mob mob && mob.getLeashHolder() instanceof Player player) {
			super.addSittingPassenger(player, seatIndex);
		} else {
			super.addSittingPassenger(passenger, seatIndex);
		}
	}

	@Override
	public Vec3 getPassengerPosition(Entity passenger, float partialTicks) {
		if (passenger != this.getControllingPassenger() || !(this.contraption instanceof AbstractMountedCannonContraption cannon))
			return super.getPassengerPosition(passenger, partialTicks);

		BlockPos seat = cannon.getSeatPos(passenger);
		if (seat == null) return null;
		AABB bb = passenger.getBoundingBox();
		double ySize = bb.getYsize();
		return this.toGlobalVector(Vec3.atLowerCornerOf(seat)
				.add(.5, passenger.getMyRidingOffset() + ySize - .65f, .5), partialTicks)
				.add(VecHelper.getCenterOf(BlockPos.ZERO))
				.subtract(0.5, ySize, 0.5);
	}

	@Nullable
	@Override
	public Entity getControllingPassenger() {
		return this.getFirstPassenger() instanceof Player player ? player : null;
	}

	public boolean canBeTurnedByController(ControlPitchContraption control) {
		return this.contraption instanceof AbstractMountedCannonContraption cannon && cannon.canBeTurnedByController(control);
	}

	public static float getRotationCap() { return 0.1f; }

}
