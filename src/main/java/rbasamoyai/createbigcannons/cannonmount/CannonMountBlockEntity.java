package rbasamoyai.createbigcannons.cannonmount;

import java.util.function.Supplier;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.IDisplayAssemblyExceptions;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonMountBlockEntity extends KineticTileEntity implements IDisplayAssemblyExceptions {

	private static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	private AssemblyException lastException = null;
	private PitchOrientedContraptionEntity mountedContraption;
	private boolean running;
	
	private float cannonYaw;
	private float cannonPitch;
	private float prevYaw;
	private float prevPitch;
	private float clientYawDiff;
	private float clientPitchDiff;
	
	float yawSpeed;
	
	public CannonMountBlockEntity(BlockEntityType<? extends CannonMountBlockEntity> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
		if (CBCBlocks.CANNON_MOUNT.has(state)) {
			this.cannonYaw = state.getValue(HORIZONTAL_FACING).toYRot();
		}
		this.setLazyTickRate(3);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (this.mountedContraption != null) {
			if (!this.mountedContraption.isAlive()) {
				this.mountedContraption = null;
			}
		}
		
		this.prevYaw = this.cannonYaw;
		this.prevPitch = this.cannonPitch;
		if (this.level.isClientSide) {
			this.clientYawDiff *= 0.5f;
			this.clientPitchDiff *= 0.5f;
		}
		
		if (!this.running) return;
		
		if (!(this.mountedContraption != null && this.mountedContraption.isStalled())) {
			float yawSpeed = this.getAngularSpeed(this::getYawSpeed, this.clientYawDiff);
			float pitchSpeed = this.getAngularSpeed(this::getSpeed, this.clientPitchDiff);
			float newYaw = this.cannonYaw + yawSpeed;
			float newPitch = this.cannonPitch + pitchSpeed;
			this.cannonYaw = (float) (newYaw % 360.0f);
			this.cannonPitch = (float) (newPitch % 360.0f);
			
			if (this.mountedContraption != null) {
				Direction.AxisDirection heading = ((MountedCannonContraption) this.mountedContraption.getContraption()).initialOrientation().getAxisDirection();
				if (heading == AxisDirection.POSITIVE) {
					this.cannonPitch = Mth.clamp(this.cannonPitch, -60.0f, 30.0f);
				} else {
					this.cannonPitch = Mth.clamp(this.cannonPitch, -30.0f, 60.0f);
				}
			} else {
				this.cannonPitch = 0.0f;
			}
		}
		
		this.applyRotation();
	}
	
	public boolean isRunning() { return this.running; }
	
	protected void applyRotation() {
		if (this.mountedContraption == null) return;
		this.mountedContraption.prevPitch = this.prevPitch;
		this.mountedContraption.pitch = this.cannonPitch;
		this.mountedContraption.prevYaw = this.prevYaw;
		this.mountedContraption.yaw = this.cannonYaw;
	}
	
	public void onRedstoneUpdate(boolean assemblyPowered, boolean prevAssemblyPowered, boolean firePowered, boolean prevFirePowered) {
		if (assemblyPowered != prevAssemblyPowered) {
			this.level.setBlock(this.worldPosition, this.getBlockState().setValue(CannonMountBlock.ASSEMBLY_POWERED, assemblyPowered), 3);
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
			this.level.setBlock(this.worldPosition, this.getBlockState().setValue(CannonMountBlock.FIRE_POWERED, firePowered), 3);
			if (firePowered && this.running && this.mountedContraption != null && this.level instanceof ServerLevel) {
				((MountedCannonContraption) this.mountedContraption.getContraption()).fireShot((ServerLevel) this.level, this.mountedContraption);
			}
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
		if (!this.running) {
			return this.cannonPitch;
		}
		return this.cannonPitch + convertToAngular(this.getSpeed()) * 0.1f * partialTicks;
	}
	
	public float getYawSpeed() {
		return this.overStressed ? 0 : this.getTheoreticalYawSpeed();
	}
	
	public float getTheoreticalYawSpeed() {
		return this.yawSpeed;
	}
	
	public float getYawOffset(float partialTicks) {
		if (!this.running) {
			return this.cannonYaw;
		}
		return this.cannonYaw + convertToAngular(this.getYawSpeed()) * 0.1f * partialTicks;
	}
	
	public float getAngularSpeed(Supplier<Float> sup, float clientDiff) {
		float speed = convertToAngular(sup.get()) * 0.1f;
		if (sup.get() == 0) {
			speed = 0;
		}
		if (this.level.isClientSide) {
			speed *= ServerSpeedProvider.get();
			speed += clientDiff / 3.0f; 
		}
		return speed;
	}
	
	protected void assemble() throws AssemblyException {
		if (!CBCBlocks.CANNON_MOUNT.has(this.getBlockState())) return;
		BlockPos assemblyPos = this.worldPosition.above(2);
		if (this.level.isOutsideBuildHeight(assemblyPos)) {
			throw cannonBlockOutsideOfWorld(assemblyPos);
		}
		
		MountedCannonContraption mountedCannon = new MountedCannonContraption();
		if (!mountedCannon.assemble(this.level, assemblyPos)) {
			return;
		}
		this.running = true;
		
		mountedCannon.removeBlocksFromWorld(this.level, BlockPos.ZERO);
		PitchOrientedContraptionEntity contraptionEntity = PitchOrientedContraptionEntity.create(this.level, mountedCannon, this.getBlockState().getValue(HORIZONTAL_FACING));
		this.mountedContraption = contraptionEntity;
		this.resetContraptionToOffset();
		this.level.addFreshEntity(contraptionEntity);
		
		this.sendData();
		
		AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(this.level, this.worldPosition);
	}
	
	protected void disassemble() {
		if (!this.running && this.mountedContraption == null) return;
		if (this.mountedContraption != null) {
			this.resetContraptionToOffset();
			this.mountedContraption.disassemble();
			AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.level, this.worldPosition);
		}
		
		this.running = false;
		
		if (this.remove) {
			CBCBlocks.CANNON_MOUNT.get().playerWillDestroy(this.level, this.worldPosition, this.getBlockState(), null);
		}
	}
	
	protected void resetContraptionToOffset() {
		if (this.mountedContraption == null) return;
		this.cannonPitch = 0;
		this.cannonYaw = this.getBlockState().getValue(HORIZONTAL_FACING).toYRot();
		this.applyRotation();
		Vec3 vec = Vec3.atBottomCenterOf((this.worldPosition.above(2)));
		this.mountedContraption.setPos(vec);
	}
	
	@Override
	public float calculateStressApplied() {
		if (this.running && this.mountedContraption != null) {
			MountedCannonContraption contraption = (MountedCannonContraption) this.mountedContraption.getContraption();
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
	}
	
	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		this.running = tag.getBoolean("Running");
		this.cannonYaw = tag.getFloat("CannonYaw");
		this.cannonPitch = tag.getFloat("CannonPitch");
		this.lastException = AssemblyException.read(tag);
		
		if (!clientPacket) return;
		
		if (this.running) {
			if (this.mountedContraption == null || !this.mountedContraption.isStalled()) {
				this.clientYawDiff = AngleHelper.getShortestAngleDiff(this.prevYaw, this.cannonYaw);
				this.clientPitchDiff = AngleHelper.getShortestAngleDiff(this.prevPitch, this.cannonPitch);
				this.prevYaw = this.cannonYaw;
				this.prevPitch = this.cannonPitch;
			}
		} else {
			this.mountedContraption = null;
		}
	}
	
	public void attach(PitchOrientedContraptionEntity contraption) {
		this.mountedContraption = contraption;
		if (!this.level.isClientSide) {
			this.running = true;
			this.sendData();
		}
	}
	
	public boolean isAttachedTo(PitchOrientedContraptionEntity contraption) {
		return this.mountedContraption == contraption;
	}
	
	@Override public AssemblyException getLastAssemblyException() { return this.lastException; }
	
	public static AssemblyException cannonBlockOutsideOfWorld(BlockPos pos) {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonBlockOutsideOfWorld", pos.getX(), pos.getY(), pos.getZ()));
	}
	
}
