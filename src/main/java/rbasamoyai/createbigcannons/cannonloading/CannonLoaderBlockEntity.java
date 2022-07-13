package rbasamoyai.createbigcannons.cannonloading;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.DirectionalExtenderScrollOptionSlot;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.LinearActuatorTileEntity;
import com.simibubi.create.foundation.tileEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;

public class CannonLoaderBlockEntity extends LinearActuatorTileEntity {
	
	protected int extensionLength;
	
	public CannonLoaderBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}
	
	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		this.extensionLength = compound.getInt("ExtensionLength");
		super.read(compound, clientPacket);
	}
	
	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		compound.putInt("ExtensionLength", this.extensionLength);
		super.write(compound, clientPacket);
	}

	@Override
	protected void assemble() throws AssemblyException {
		if (!(this.level.getBlockState(this.worldPosition).getBlock() instanceof CannonLoaderBlock)) return;
		
		Direction facing = this.getBlockState().getValue(CannonLoaderBlock.FACING);
		CannonLoadingContraption contraption = new CannonLoadingContraption(facing, this.getMovementSpeed() < 0);
		if (!contraption.assemble(this.level, this.worldPosition)) {
			return;
		}
		
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, facing.getAxis());
		Direction movementDirection = (this.getSpeed() > 0) ^ facing.getAxis() != Direction.Axis.Z ? positive : positive.getOpposite();
		BlockPos anchor = contraption.anchor.relative(facing, contraption.initialExtensionProgress);
		if (CannonLoaderCollider.isCollidingWithWorld(this.level, contraption, anchor.relative(movementDirection), movementDirection)) {
			return;
		}
		
		this.extensionLength = contraption.extensionLength;
		float resultingOffset = contraption.initialExtensionProgress + Math.signum(this.getMovementSpeed()) * 0.5f;
		if (resultingOffset <= 0 || resultingOffset >= this.extensionLength) {
			return;
		}
		
		this.running = true;
		this.offset = contraption.initialExtensionProgress;
		this.sendData();
		this.clientOffsetDiff = 0;
		
		BlockPos startPos = BlockPos.ZERO.relative(facing, contraption.initialExtensionProgress);
		contraption.removeBlocksFromWorld(this.level, startPos);
		this.movedContraption = ControlledContraptionEntity.create(this.getLevel(), this, contraption);
		this.resetContraptionToOffset();
		this.forceMove = true;
		this.level.addFreshEntity(this.movedContraption);
		
		AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(this.level, this.worldPosition);
	}

	@Override
	public void disassemble() {
		if (!this.running && this.movedContraption == null) return;
		if (!this.remove) {
			this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(CannonLoaderBlock.MOVING, false), 3 | 16);
		}
		if (this.movedContraption != null) {
			this.resetContraptionToOffset();
			this.movedContraption.disassemble();
			AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.level, this.worldPosition);
		}
		this.running = false;
		this.movedContraption = null;
		this.sendData();
		
		if (this.remove) {
			CBCBlocks.CANNON_LOADER.get().playerWillDestroy(this.level, this.worldPosition, this.getBlockState(), null);
		}
	}
	
	@Override
	protected boolean moveAndCollideContraption() {
		if (this.movedContraption == null) {
			return false;
		}
		if (this.movedContraption.isStalled()) {
			this.movedContraption.setDeltaMovement(Vec3.ZERO);			
			return false;
		}
		Vec3 motion = this.getMotionVector();
		this.movedContraption.setContraptionMotion(motion);
		this.movedContraption.move(motion.x, motion.y, motion.z);
		return CannonLoaderCollider.collideBlocks(this.movedContraption);
	}
	
	@Override
	protected void collided() {
		super.collided();
		if (!this.running && this.getMovementSpeed() > 0) {
			this.assembleNextTick = true;
		}
	}
	
	@Override
	public float getMovementSpeed() {
		float movementSpeed = Mth.clamp(convertToLinear(this.getSpeed()), -0.49f, 0.49f);
		if (this.level.isClientSide) {
			movementSpeed *= ServerSpeedProvider.get();
		}
		Direction facing = this.getBlockState().getValue(CannonLoaderBlock.FACING);
		int movementModifier = facing.getAxisDirection().getStep() * (facing.getAxis() == Direction.Axis.Z ? -1 : 1);
		movementSpeed = movementSpeed * -movementModifier + this.clientOffsetDiff * 0.5f;
		
		movementSpeed = Mth.clamp(movementSpeed, 0 - this.offset, this.extensionLength - this.offset);
		return movementSpeed;
	}

	@Override protected int getExtensionRange() { return this.extensionLength; }

	@Override
	protected int getInitialOffset() {
		return this.movedContraption == null ? 0 : ((CannonLoadingContraption) this.movedContraption.getContraption()).initialExtensionProgress;
	}

	@Override
	protected ValueBoxTransform getMovementModeSlot() {
		return new DirectionalExtenderScrollOptionSlot((state, d) -> false);
	}

	@Override
	protected Vec3 toMotionVector(float speed) {
		Direction facing = this.getBlockState().getValue(CannonLoaderBlock.FACING);
		return Vec3.atLowerCornerOf(facing.getNormal()).scale(speed);
	}

	@Override
	protected Vec3 toPosition(float offset) {
		Vec3 position = Vec3.atLowerCornerOf(this.getBlockState().getValue(CannonLoaderBlock.FACING).getNormal()).scale(offset);
		return position.add(Vec3.atLowerCornerOf(this.movedContraption.getContraption().anchor));
	}

}
