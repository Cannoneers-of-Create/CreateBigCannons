package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import static net.minecraft.ChatFormatting.GRAY;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.multiple_kinetic_interface.HasMultipleKineticInterfaces;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.CannonContraptionProviderBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class CannonMountBlockEntity extends SmartBlockEntity implements IDisplayAssemblyExceptions, ControlPitchContraption.Block,
	ExtendsCannonMount, HasMultipleKineticInterfaces, IHaveGoggleInformation {

	private static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

	private AssemblyException lastException = null;
	protected PitchOrientedContraptionEntity mountedContraption;
	private boolean running;

	private float cannonYaw;
	private float cannonPitch;
	private float prevYaw;
	private float prevPitch;
	private float clientYawDiff;
	private float clientPitchDiff;

	protected final CannonMountInterfaceBlockEntity pitchInterface;
	protected final CannonMountInterfaceBlockEntity yawInterface;


	public CannonMountBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
		if (CBCBlocks.CANNON_MOUNT.has(state)) {
			this.cannonYaw = state.getValue(HORIZONTAL_FACING).toYRot();
		}
		this.setLazyTickRate(3);
		this.pitchInterface = new CannonMountInterfaceBlockEntity.PitchInterface(typeIn, pos, state, this);
		this.yawInterface = new CannonMountInterfaceBlockEntity.YawInterface(typeIn, pos, state, this);
	}

	@Override
	public void setLevel(Level level) {
		super.setLevel(level);
		this.pitchInterface.setLevel(level);
		this.yawInterface.setLevel(level);
	}

	@Override public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

	@Override
	public BlockState getControllerState() {
		return this.getBlockState();
	}

	@Nullable
	@Override
	public ResourceLocation getTypeId() { return CreateBigCannons.resource("cannon_mount"); }

	@Override
	protected AABB createRenderBoundingBox() {
		// TODO: based on state for things like upside down mounts
		return new AABB(this.getBlockPos()).expandTowards(0, 2, 0);
	}

	@Override
	public void tick() {
		super.tick();

		this.pitchInterface.tick();
		this.yawInterface.tick();

		if (this.mountedContraption != null) {
			if (!this.mountedContraption.isAlive()) {
				this.mountedContraption = null;
			}
		}

		this.prevYaw = this.cannonYaw;
		this.prevPitch = this.cannonPitch;

		boolean flag = this.mountedContraption != null && this.mountedContraption.canBeTurnedByController(this);

		if (this.getLevel().isClientSide) {
			this.clientYawDiff = flag ? this.clientYawDiff * 0.5f : 0;
			this.clientPitchDiff = flag ? this.clientPitchDiff * 0.5f : 0;
		}

		if (!this.running && !this.isVirtual()) {
			if (CBCBlocks.CANNON_MOUNT.has(this.getBlockState())) {
				this.cannonYaw = this.getBlockState().getValue(HORIZONTAL_FACING).toYRot();
				this.prevYaw = this.cannonYaw;
				this.cannonPitch = 0;
				this.prevPitch = 0;
			}
			return;
		}

		if (!(this.mountedContraption != null && this.mountedContraption.isStalled()) && flag) {
			float yawSpeed = this.getAngularSpeed(this.yawInterface.getSpeed(), this.clientYawDiff);
			float pitchSpeed = this.getAngularSpeed(this.pitchInterface.getSpeed(), this.clientPitchDiff);

			double yawAngleLimit = this.yawInterface.getSequencedAngleLimit();
			if (yawAngleLimit >= 0) {
				yawSpeed = (float) Mth.clamp(yawSpeed, -yawAngleLimit, yawAngleLimit);
				this.yawInterface.setSequencedAngleLimit(Math.max(0, yawAngleLimit - Math.abs(yawSpeed)));
			}

			double pitchAngleLimit = this.pitchInterface.getSequencedAngleLimit();
			if (pitchAngleLimit >= 0) {
				pitchSpeed = (float) Mth.clamp(pitchSpeed, -pitchAngleLimit, pitchAngleLimit);
				this.pitchInterface.setSequencedAngleLimit(Math.max(0, pitchAngleLimit - Math.abs(pitchSpeed)));
			}

			Direction dir = this.mountedContraption.getInitialOrientation();
			boolean flag1 = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
			float sgn = flag1 ? 1 : -1;

			float newYaw = this.cannonYaw - yawSpeed;
			float newPitch = this.cannonPitch + pitchSpeed * sgn;
			this.cannonYaw = newYaw % 360.0f;
			this.cannonPitch = this.mountedContraption == null ? 0 : Mth.clamp(newPitch % 360.0f, -this.getMaxDepress(), this.getMaxElevate());
		}
		this.applyRotation();
	}

	private float getMaxDepress() {
		return this.mountedContraption.maximumDepression();
	}

	private float getMaxElevate() {
		return this.mountedContraption.maximumElevation();
	}

	public boolean isRunning() {
		return this.running;
	}

	protected void applyRotation() {
		if (this.mountedContraption == null) return;

		Direction dir = this.mountedContraption.getInitialOrientation();
		boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
		float sgn = flag ? 1 : -1;

		if (!this.mountedContraption.canBeTurnedByController(this)) {
			float d = -this.mountedContraption.maximumDepression();
			float e = this.mountedContraption.maximumElevation();
			this.cannonPitch = Mth.clamp(this.mountedContraption.pitch, d, e) * sgn;
			this.cannonYaw = this.mountedContraption.yaw;
		} else {
			this.mountedContraption.pitch = this.cannonPitch * sgn;
			this.mountedContraption.yaw = this.cannonYaw;
		}
	}

	public void onRedstoneUpdate(boolean assemblyPowered, boolean prevAssemblyPowered, boolean firePowered, boolean prevFirePowered, int firePower) {
		if (assemblyPowered != prevAssemblyPowered) {
			this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(CannonMountBlock.ASSEMBLY_POWERED, assemblyPowered), 3);
			if (assemblyPowered) {
				try {
					this.assemble();
					this.lastException = null;
				} catch (AssemblyException e) {
					this.lastException = e;
					this.sendData();
				}
			} else {
				this.disassemble();
				this.sendData();
			}
		}
		if (firePowered != prevFirePowered) {
			this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(CannonMountBlock.FIRE_POWERED, firePowered), 3);
		}
		if (this.running && this.mountedContraption != null && this.getLevel() instanceof ServerLevel slevel) {
			((AbstractMountedCannonContraption) this.mountedContraption.getContraption()).onRedstoneUpdate(slevel, this.mountedContraption, firePowered != prevFirePowered, firePower, this);
		}
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		if (this.running && this.mountedContraption != null) {
			this.sendData();
		}
	}

	public float getPitchOffset(float partialTicks) {
		if (this.isVirtual())
			return Mth.lerp(partialTicks + 0.5f, this.prevPitch, this.cannonPitch);
		if (this.mountedContraption == null || this.mountedContraption.isStalled() || !this.running)
			partialTicks = 0;
		if (this.mountedContraption != null && !this.mountedContraption.canBeTurnedByController(this)) {
			Direction facing = this.getContraptionDirection();
			boolean flag = (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (facing.getAxis() == Direction.Axis.X);
			float sgn = flag ? 1 : -1;
			return this.mountedContraption.getViewXRot(partialTicks) * sgn;
		}
		float aSpeed = this.getAngularSpeed(this.pitchInterface.getSpeed(), this.clientPitchDiff);
		return Mth.lerp(partialTicks, this.cannonPitch, this.cannonPitch + aSpeed);
	}

	public void setPitch(float pitch) {
		this.cannonPitch = pitch;
	}

	public float getPitchSpeed() { return this.pitchInterface.getSpeed(); }
	public float getYawSpeed() { return this.yawInterface.getSpeed(); }

	public KineticBlockEntity getPitchInterface() { return this.pitchInterface; }
	public KineticBlockEntity getYawInterface() { return this.yawInterface; }

	public float getYawOffset(float partialTicks) {
		if (this.isVirtual())
			return Mth.lerp(partialTicks + 0.5f, this.prevYaw, this.cannonYaw);
		if (this.mountedContraption == null || this.mountedContraption.isStalled() || !this.running)
			partialTicks = 0;
		if (this.mountedContraption != null && !this.mountedContraption.canBeTurnedByController(this)) {
			return -this.mountedContraption.getViewYRot(partialTicks);
		}
		float aSpeed = this.getAngularSpeed(this.yawInterface.getSpeed(), this.clientYawDiff);
		return Mth.lerp(partialTicks, this.cannonYaw, this.cannonYaw + aSpeed);
	}

	public float getDisplayPitch() {
//		float ret = this.getPitchOffset(0);
//		if (Math.abs(ret) < 1e-1f) return 0;
//		Direction dir = this.getContraptionDirection();
//		return (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X) ? ret : -ret;
		return Math.abs(this.cannonPitch) < 1e-1f ? 0 : this.cannonPitch;
	}

	public void setYaw(float yaw) {
		this.cannonYaw = yaw;
	}

	public Direction getContraptionDirection() {
		return this.mountedContraption == null ? Direction.NORTH : ((AbstractMountedCannonContraption) this.mountedContraption.getContraption()).initialOrientation();
	}

	public float getAngularSpeed(float value, float clientDiff) {
		float speed = KineticBlockEntity.convertToAngular(value) * 0.125f;
		if (value == 0) {
			speed = 0;
		}
		if (this.getLevel().isClientSide) {
			speed *= ServerSpeedProvider.get();
			speed += clientDiff / 3.0f;
		}
		return speed;
	}

	protected void assemble() throws AssemblyException {
		if (!CBCBlocks.CANNON_MOUNT.has(this.getBlockState())) return;
		BlockPos assemblyPos = this.worldPosition.above(2);
		if (this.getLevel().isOutsideBuildHeight(assemblyPos)) {
			throw cannonBlockOutsideOfWorld(assemblyPos);
		}

		AbstractMountedCannonContraption mountedCannon = this.getContraption(assemblyPos);
		if (mountedCannon == null || !mountedCannon.assemble(this.getLevel(), assemblyPos)) return;
		Direction facing = this.getBlockState().getValue(CannonMountBlock.HORIZONTAL_FACING);
		Direction facing1 = mountedCannon.initialOrientation();
		if (facing.getAxis() != facing1.getAxis() && facing1.getAxis().isHorizontal()) return;
		this.running = true;

		mountedCannon.removeBlocksFromWorld(this.getLevel(), BlockPos.ZERO);
		PitchOrientedContraptionEntity contraptionEntity = PitchOrientedContraptionEntity.create(this.getLevel(), mountedCannon, facing1, this);
		this.mountedContraption = contraptionEntity;
		this.resetContraptionToOffset();
		this.getLevel().addFreshEntity(contraptionEntity);

		this.sendData();

		AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(this.getLevel(), this.worldPosition);
	}

	private AbstractMountedCannonContraption getContraption(BlockPos pos) {
		return this.level.getBlockState(pos).getBlock() instanceof CannonContraptionProviderBlock provBlock ? provBlock.getCannonContraption() : null;
	}

	public void disassemble() {
		if (!this.running && this.mountedContraption == null) return;
		if (this.mountedContraption != null) {
			this.resetContraptionToOffset();
			this.mountedContraption.save(new CompoundTag()); // Crude refresh of block data
			this.mountedContraption.disassemble();
			AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.getLevel(), this.worldPosition);
		}

		this.running = false;
	}

	protected void resetContraptionToOffset() {
		if (this.mountedContraption == null) return;
		this.cannonPitch = 0;
		this.cannonYaw = this.getContraptionDirection().toYRot();
		this.prevPitch = this.cannonPitch;
		this.prevYaw = this.cannonYaw;

		this.mountedContraption.pitch = this.cannonPitch;
		this.mountedContraption.yaw = this.cannonYaw;
		this.mountedContraption.prevPitch = this.mountedContraption.pitch;
		this.mountedContraption.prevYaw = this.mountedContraption.yaw;

		this.mountedContraption.setXRot(this.cannonPitch);
		this.mountedContraption.setYRot(this.cannonYaw);
		this.mountedContraption.xRotO = this.mountedContraption.getXRot();
		this.mountedContraption.yRotO = this.mountedContraption.getYRot();

		Vec3 vec = Vec3.atBottomCenterOf((this.worldPosition.above(2)));
		this.mountedContraption.setPos(vec);
	}

	public float calculateCannonStressApplied() {
		if (this.running && this.mountedContraption != null) {
			AbstractMountedCannonContraption contraption = (AbstractMountedCannonContraption) this.mountedContraption.getContraption();
			return contraption.getWeightForStress();
		}
		return 0.0f;
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.putBoolean("Running", this.running);
		tag.putFloat("CannonYaw", this.cannonYaw);
		tag.putFloat("CannonPitch", this.cannonPitch);
		AssemblyException.write(tag, this.lastException);

		CompoundTag pitchTag = new CompoundTag();
		this.pitchInterface.saveAdditional(pitchTag);
		tag.put("PitchInterface", pitchTag);

		CompoundTag yawTag = new CompoundTag();
		this.yawInterface.saveAdditional(yawTag);
		tag.put("YawInterface", yawTag);
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		boolean oldRunning = this.running;
		this.running = tag.getBoolean("Running");
		this.cannonYaw = tag.getFloat("CannonYaw");
		this.cannonPitch = tag.getFloat("CannonPitch");
		this.lastException = AssemblyException.read(tag);

		this.pitchInterface.load(tag.getCompound("PitchInterface"));
		this.yawInterface.load(tag.getCompound("YawInterface"));

		if (!clientPacket) return;

		if (this.running) {
			if (oldRunning && (this.mountedContraption == null || !this.mountedContraption.isStalled())) {
				this.clientYawDiff = AngleHelper.getShortestAngleDiff(this.prevYaw, this.cannonYaw);
				this.clientPitchDiff = AngleHelper.getShortestAngleDiff(this.prevPitch, this.cannonPitch);
				this.prevYaw = this.cannonYaw;
				this.prevPitch = this.cannonPitch;
			}
		} else {
			this.mountedContraption = null;
		}
	}

	@Override
	public void remove() {
		this.remove = true;
		if (!this.getLevel().isClientSide)
			this.disassemble();
		this.pitchInterface.remove();
		this.yawInterface.remove();
		super.remove();
	}

	@Override
	public boolean isAttachedTo(AbstractContraptionEntity entity) {
		return this.mountedContraption == entity;
	}

	@Override
	public void attach(PitchOrientedContraptionEntity contraption) {
		if (!(contraption.getContraption() instanceof AbstractMountedCannonContraption)) return;
		this.mountedContraption = contraption;
		if (!this.getLevel().isClientSide) {
			this.running = true;
			this.sendData();
		}
	}

	@Override
	public void onStall() {
		if (!this.getLevel().isClientSide) this.sendData();
	}

	@Override
	public BlockPos getControllerBlockPos() {
		return this.worldPosition;
	}

	@Override
	public BlockPos getDismountPositionForContraption(PitchOrientedContraptionEntity poce) {
		return this.worldPosition.relative(this.mountedContraption.getInitialOrientation().getOpposite()).above();
	}

	@Override
	public AssemblyException getLastAssemblyException() {
		return this.lastException;
	}

	public static AssemblyException cannonBlockOutsideOfWorld(BlockPos pos) {
		return new AssemblyException(Components.translatable("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonBlockOutsideOfWorld", pos.getX(), pos.getY(), pos.getZ()));
	}

	public Vec3 getInteractionLocation() {
		return this.mountedContraption != null && this.mountedContraption.getContraption() instanceof AbstractMountedCannonContraption cannon
			? cannon.getInteractionVec(this.mountedContraption) : Vec3.atCenterOf(this.worldPosition);
	}

	@Nullable
	public PitchOrientedContraptionEntity getContraption() {
		return this.mountedContraption;
	}

	@Nullable
	@Override
	public CannonMountBlockEntity getCannonMount() {
		return this;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (IRotate.StressImpact.isEnabled()) {
			float pitchStress = this.pitchInterface.calculateStressApplied();
			float yawStress = this.yawInterface.calculateStressApplied();
			if (!Mth.equal(pitchStress + yawStress, 0)) {
				// Adapted from KineticBlockEntity
				Lang.translate("gui.goggles.kinetic_stats")
					.forGoggles(tooltip);
				Lang.translate("tooltip.stressImpact")
					.style(GRAY)
					.forGoggles(tooltip);

				float stressTotal = pitchStress * Math.abs(this.pitchInterface.getTheoreticalSpeed())
					+ yawStress * Math.abs(this.yawInterface.getTheoreticalSpeed());

				Lang.number(stressTotal)
					.translate("generic.unit.stress")
					.style(ChatFormatting.AQUA)
					.space()
					.add(Lang.translate("gui.goggles.at_current_speed")
						.style(ChatFormatting.DARK_GRAY))
					.forGoggles(tooltip, 1);
			}
		}
		ExtendsCannonMount.addCannonInfoToTooltip(tooltip, this.mountedContraption);
		return true;
	}

	@Nullable
	@Override
	public KineticBlockEntity getInterfacingBlockEntity(BlockPos from) {
		// TODO: upside down cannon mount
		if (from.equals(new BlockPos(0, -1, 0)))
			return this.yawInterface;
		BlockState state = this.getBlockState();
		Direction.Axis axis = ((CannonMountBlock) state.getBlock()).getRotationAxis(state);
		BlockPos test1 = BlockPos.ZERO.relative(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE));
		if (from.equals(test1))
			return this.pitchInterface;
		return from.equals(BlockPos.ZERO.subtract(test1)) ? this.pitchInterface : null;
	}

	@Override
	public List<KineticBlockEntity> getAllKineticBlockEntities() {
		return List.of(this.pitchInterface, this.yawInterface);
	}

	public void tryUpdatingSpeed() {
		this.pitchInterface.tryUpdateSpeed();
		this.yawInterface.tryUpdateSpeed();
	}

	@Override
	public void setBlockState(BlockState blockState) {
		super.setBlockState(blockState);
		this.pitchInterface.setBlockState(blockState);
		this.yawInterface.setBlockState(blockState);
	}

}
