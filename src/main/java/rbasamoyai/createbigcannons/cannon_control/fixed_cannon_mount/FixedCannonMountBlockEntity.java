package rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount;

import static rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity.cannonBlockOutsideOfWorld;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.CannonContraptionProviderBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class FixedCannonMountBlockEntity extends SmartBlockEntity implements IDisplayAssemblyExceptions, ControlPitchContraption.Block, IHaveGoggleInformation {

	private AssemblyException lastException = null;
	protected PitchOrientedContraptionEntity mountedContraption;
	private boolean running;

	private float cannonYaw;
	private float cannonPitch;

	private FixedCannonMountScrollValueBehaviour pitchSlot;
	private FixedCannonMountScrollValueBehaviour yawSlot;

	public FixedCannonMountBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		if (CBCBlocks.FIXED_CANNON_MOUNT.has(state))
			this.cannonYaw = state.getValue(FixedCannonMountBlock.FACING).toYRot();
		this.setLazyTickRate(3);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(this.pitchSlot = new FixedCannonMountScrollValueBehaviour(this, true));
		behaviours.add(this.yawSlot = new FixedCannonMountScrollValueBehaviour(this, false));
	}

	public void onRedstoneUpdate(boolean assemblyPowered, boolean prevAssemblyPowered, boolean firePowered, boolean prevFirePowered, int firePower) {
		if (assemblyPowered != prevAssemblyPowered) {
			this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(FixedCannonMountBlock.ASSEMBLY_POWERED, assemblyPowered), 3);
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
			this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(FixedCannonMountBlock.FIRE_POWERED, firePowered), 3);
		}
		if (this.running && this.mountedContraption != null && this.getLevel() instanceof ServerLevel slevel) {
			((AbstractMountedCannonContraption) this.mountedContraption.getContraption()).onRedstoneUpdate(slevel, this.mountedContraption, firePowered != prevFirePowered, firePower, this);
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.mountedContraption != null && !this.mountedContraption.isAlive())
			this.mountedContraption = null;
		this.applyRotation();
	}

	protected void applyRotation() {
		if (this.mountedContraption == null)
			return;
		Direction dir = this.mountedContraption.getInitialOrientation();
		boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
		float sgn = flag ? 1 : -1;

		float pitchAdjust = this.pitchSlot == null ? 0 : this.pitchSlot.getValue();
		this.mountedContraption.pitch = (this.cannonPitch + pitchAdjust) * sgn;
		float yawAdjust = this.yawSlot == null ? 0 : this.yawSlot.getValue();
		this.mountedContraption.yaw = this.cannonYaw + yawAdjust;
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		if (this.running && this.mountedContraption != null) {
			this.sendData();
		}
	}

	protected void assemble() throws AssemblyException {
		if (!CBCBlocks.FIXED_CANNON_MOUNT.has(this.getBlockState()))
			return;
		Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
		BlockPos assemblyPos = this.worldPosition.relative(facing);
		if (this.getLevel().isOutsideBuildHeight(assemblyPos))
			throw cannonBlockOutsideOfWorld(assemblyPos);

		AbstractMountedCannonContraption mountedCannon = this.getContraption(assemblyPos);
		if (mountedCannon == null || !mountedCannon.assemble(this.getLevel(), assemblyPos))
			return;
		Direction facing1 = mountedCannon.initialOrientation();
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

	protected void resetContraptionToOffset() {
		if (this.mountedContraption == null)
			return;
		this.cannonPitch = 0;
		this.cannonYaw = this.getContraptionDirection().toYRot();

		this.mountedContraption.pitch = this.cannonPitch;
		this.mountedContraption.yaw = this.cannonYaw;
		this.mountedContraption.prevPitch = this.mountedContraption.pitch;
		this.mountedContraption.prevYaw = this.mountedContraption.yaw;

		this.mountedContraption.setXRot(this.cannonPitch);
		this.mountedContraption.setYRot(this.cannonYaw);
		this.mountedContraption.xRotO = this.mountedContraption.getXRot();
		this.mountedContraption.yRotO = this.mountedContraption.getYRot();

		Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
		Vec3 vec = Vec3.atBottomCenterOf((this.worldPosition.relative(facing)));
		this.mountedContraption.setPos(vec);
	}

	public Direction getContraptionDirection() {
		return this.mountedContraption == null ? Direction.NORTH
			: ((AbstractMountedCannonContraption) this.mountedContraption.getContraption()).initialOrientation();
	}

	@Override
	public void disassemble() {
		if (!this.running && this.mountedContraption == null)
			return;
		if (this.mountedContraption != null) {
			this.resetContraptionToOffset();
			this.mountedContraption.save(new CompoundTag()); // Crude refresh of block data
			this.mountedContraption.disassemble();
			AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.getLevel(), this.worldPosition);
		}

		this.running = false;
	}

	@Override public AssemblyException getLastAssemblyException() { return this.lastException; }

	@Override public boolean isAttachedTo(AbstractContraptionEntity entity) { return this.mountedContraption == entity; }

	@Override
	public void attach(PitchOrientedContraptionEntity contraption) {
		if (!(contraption.getContraption() instanceof AbstractMountedCannonContraption))
			return;
		this.mountedContraption = contraption;
		if (!this.level.isClientSide) {
			this.running = true;
			this.sendData();
		}
	}

	@Override
	public void onStall() {
		if (!this.level.isClientSide)
			this.sendData();
	}

	@Override
	public BlockPos getDismountPositionForContraption(PitchOrientedContraptionEntity poce) {
		Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
		return this.worldPosition.relative(this.mountedContraption.getInitialOrientation().getOpposite()).relative(facing);
	}

	@Override public BlockState getControllerState() { return this.getBlockState(); }

	@Override public BlockPos getControllerBlockPos() { return this.worldPosition; }

	@Override
	public void remove() {
		this.remove = true;
		if (!this.level.isClientSide)
			this.disassemble();
		super.remove();
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.putBoolean("Running", this.running);
		tag.putFloat("CannonYaw", this.cannonYaw);
		tag.putFloat("CannonPitch", this.cannonPitch);
		AssemblyException.write(tag, this.lastException);
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		this.running = tag.getBoolean("Running");
		this.cannonYaw = tag.getFloat("CannonYaw");
		this.cannonPitch = tag.getFloat("CannonPitch");
		this.lastException = AssemblyException.read(tag);

		if (!clientPacket) return;

		if (!this.running)
			this.mountedContraption = null;
	}

	public Vec3 getInteractionLocation() {
		return this.mountedContraption != null && this.mountedContraption.getContraption() instanceof AbstractMountedCannonContraption cannon
			? cannon.getInteractionVec(this.mountedContraption) : Vec3.atCenterOf(this.worldPosition);
	}

	@Nullable public PitchOrientedContraptionEntity getContraption() { return this.mountedContraption; }

	public static class FixedCannonMountScrollValueBehaviour extends ValveHandleBlockEntity.ValveHandleScrollValueBehaviour {
		public static final BehaviourType<FixedCannonMountScrollValueBehaviour> PITCH_TYPE = new BehaviourType<>();
		public static final BehaviourType<FixedCannonMountScrollValueBehaviour> YAW_TYPE = new BehaviourType<>();

		private final boolean pitch;
		private final ValueBoxTransform newSlotPositioning;

		public FixedCannonMountScrollValueBehaviour(SmartBlockEntity be, boolean pitch) {
			super(be);
			String suffix = pitch ? "pitch" : "yaw";
			this.setLabel(Lang.builder(CreateBigCannons.MOD_ID).translate("fixed_cannon_mount.angle_" + suffix).component());
			this.newSlotPositioning = new FixedCannonMountValueBox(pitch);
			this.pitch = pitch;
			this.between(-45, 45);
			this.withFormatter(v -> {
				return String.format("%s%d", v < 0 ? "-" : v > 0 ? "+" : "", Math.abs(v)) + Lang.translateDirect("generic.unit.degrees").getString();
			});
		}

		@Override
		public boolean testHit(Vec3 hit) {
			BlockState state = this.blockEntity.getBlockState();
			Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(this.blockEntity.getBlockPos()));
			return this.newSlotPositioning.testHit(state, localHit);
		}

		@Override
		public MutableComponent formatValue(ValueSettings settings) {
			int sgn = settings.row() == 0 ? -1 : 1;
			return Lang.number(settings.value() * sgn)
				.add(Lang.translateDirect("generic.unit.degrees"))
				.component();
		}

		@Override public void onShortInteract(Player player, InteractionHand hand, Direction side) {}

		@Override
		public void setValueSettings(Player player, ValueSettings valueSetting, boolean ctrlHeld) {
			int value = valueSetting.value();
			if (!valueSetting.equals(this.getValueSettings()))
				this.playFeedbackSound(this);
			this.setValue(valueSetting.row() == 0 ? -value : value);
		}

		@Override
		public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
			ImmutableList<Component> rows = ImmutableList.of(Components.literal("-")
					.withStyle(ChatFormatting.BOLD),
				Components.literal("+")
					.withStyle(ChatFormatting.BOLD));
			return new ValueSettingsBoard(this.label, 45, 15, rows, new ValueSettingsFormatter(this::formatValue));
		}

		@Override public ValueBoxTransform getSlotPositioning() { return this.newSlotPositioning; }

		@Override public BehaviourType<?> getType() { return this.pitch ? PITCH_TYPE : YAW_TYPE; }

		public boolean setsPitch() { return this.pitch; }

		@Override
		public void write(CompoundTag nbt, boolean clientPacket) {
			nbt.putInt(this.pitch ? "PitchAdjustment" : "YawAdjustment", this.value);
		}

		@Override
		public void read(CompoundTag nbt, boolean clientPacket) {
			this.value = nbt.getInt(this.pitch ? "PitchAdjustment" : "YawAdjustment");
		}
	}

	public static class FixedCannonMountValueBox extends CenteredSideValueBoxTransform {
		private final boolean pitch;

		public FixedCannonMountValueBox(boolean pitch) {
			super((state, dir) -> {
				return state.getValue(BlockStateProperties.FACING) != dir;
			});
			this.pitch = pitch;
		}

		@Override
		protected Vec3 getSouthLocation() {
			double xOffset = this.pitch ? -4 : 4;
			return VecHelper.voxelSpace(8 + xOffset, 8, 15.5);
		}
	}

}
