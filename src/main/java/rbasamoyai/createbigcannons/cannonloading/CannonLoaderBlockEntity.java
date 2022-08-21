package rbasamoyai.createbigcannons.cannonloading;

import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.base.PoleContraption;
import rbasamoyai.createbigcannons.base.PoleMoverBlockEntity;

public class CannonLoaderBlockEntity extends PoleMoverBlockEntity {
	
	protected int extensionLength;
	
	public CannonLoaderBlockEntity(BlockEntityType<? extends CannonLoaderBlockEntity> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@Override
	protected PoleContraption innerAssemble() throws AssemblyException {
		if (!(this.level.getBlockState(this.worldPosition).getBlock() instanceof CannonLoaderBlock)) return null;
		
		Direction facing = this.getBlockState().getValue(CannonLoaderBlock.FACING);
		CannonLoadingContraption contraption = new CannonLoadingContraption(facing, this.getMovementSpeed() < 0);
		if (!contraption.assemble(this.level, this.worldPosition)) return null;
		
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, facing.getAxis());
		Direction movementDirection = (this.getSpeed() > 0) ^ facing.getAxis() != Direction.Axis.Z ? positive : positive.getOpposite();
		BlockPos anchor = contraption.anchor.relative(facing, contraption.initialExtensionProgress());
		return CannonLoaderCollider.isCollidingWithWorld(this.level, contraption, anchor.relative(movementDirection), movementDirection) ? null : contraption;
	}
	
	@Override
	public void disassemble() {
		if (!this.running && this.movedContraption == null) return;
		if (!this.remove) {
			this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(CannonLoaderBlock.MOVING, false), 3 | 16);
		}
		super.disassemble();
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

}
