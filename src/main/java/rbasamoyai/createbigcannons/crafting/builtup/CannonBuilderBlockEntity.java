package rbasamoyai.createbigcannons.crafting.builtup;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.base.PoleContraption;
import rbasamoyai.createbigcannons.base.PoleMoverBlockEntity;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock.BuilderState;

public class CannonBuilderBlockEntity extends PoleMoverBlockEntity {

	public CannonBuilderBlockEntity(BlockEntityType<? extends CannonBuilderBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	protected PoleContraption innerAssemble() throws AssemblyException {
		if (!(this.level.getBlockState(this.worldPosition).getBlock() instanceof CannonBuilderBlock)) return null;
		
		Direction facing = this.getBlockState().getValue(CannonBuilderBlock.FACING);
		CannonBuildingContraption contraption = new CannonBuildingContraption(facing, this.getMovementSpeed() < 0);
		if (!contraption.assemble(this.level, this.worldPosition)) return null;
		
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, facing.getAxis());
		Direction movementDirection = (this.getSpeed() > 0) ^ facing.getAxis() != Direction.Axis.Z ? positive : positive.getOpposite();
		BlockPos anchor = contraption.anchor.relative(facing, contraption.initialExtensionProgress());
		return CannonBuilderCollider.isCollidingWithWorld(this.level, contraption, anchor.relative(movementDirection), movementDirection) ? null : contraption;
	}
	
	@Override
	public void disassemble() {
		if (!this.running && this.movedContraption == null) return;
		if (!this.remove) {
			this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(CannonBuilderBlock.STATE, BuilderState.EXTENDED), 3 | 16);
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
		return CannonBuilderCollider.collideBlocks(this.movedContraption);
	}
	
	public BlockState updateBlockstatesOnPowered(BlockState state) {
		if (!state.hasProperty(CannonBuilderBlock.STATE)) return state;
		
		BuilderState bState = state.getValue(CannonBuilderBlock.STATE);
		if (bState == BuilderState.ACTIVATED || bState == BuilderState.UNACTIVATED) {
			return state.setValue(CannonBuilderBlock.STATE, bState == BuilderState.ACTIVATED ? BuilderState.UNACTIVATED : BuilderState.ACTIVATED);
		}
		if (this.running || this.movedContraption != null) return state;
		
		Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
		for (int offs = 1; offs < CannonBuilderBlock.maxAllowedBuilderLength(); ++offs) {
			BlockPos currentPos = this.worldPosition.relative(facing, offs);
			BlockState currentState = this.level.getBlockState(currentPos);
			if (AllBlocks.PISTON_EXTENSION_POLE.has(currentState)) {
				if (currentState.getValue(BlockStateProperties.FACING).getAxis() != facing.getAxis()) break;
				continue;
			}
			if (CBCBlocks.CANNON_BUILDER_HEAD.has(currentState)) {
				this.level.setBlock(currentPos, currentState.cycle(CannonBuilderHeadBlock.ATTACHED), 2);
			}
			break;
		}
		return state;
	}

}
