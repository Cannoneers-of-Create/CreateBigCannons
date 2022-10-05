package rbasamoyai.createbigcannons.crafting.boring;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionType;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock.PistonState;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.PistonExtensionPoleBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCContraptionTypes;
import rbasamoyai.createbigcannons.base.PoleContraption;

public class CannonDrillingContraption extends PoleContraption {
	
	public CannonDrillingContraption() {}
	
	public CannonDrillingContraption(Direction orientation, boolean retract) {
		super(orientation, retract);
	}
	
	private static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	@Override
	protected boolean collectExtensions(Level level, BlockPos pos, Direction direction) throws AssemblyException {
		if (!CBCBlocks.CANNON_DRILL.has(level.getBlockState(pos))) return false;
		
		List<StructureBlockInfo> poles = new ArrayList<>();
		BlockPos start = pos;
		BlockState nextBlock = level.getBlockState(start.relative(direction));
		int extensionsInFront = 0;
		Direction.Axis blockAxis = direction.getAxis();
		int maxPoles = CannonDrillBlock.maxAllowedDrillLength();
		
		PistonExtensionPoleBlock.PlacementHelper matcher = PistonExtensionPoleBlock.PlacementHelper.get();
		if (level.getBlockState(pos).getValue(CannonDrillBlock.STATE) == PistonState.EXTENDED) {
			while (matcher.matchesAxis(nextBlock, blockAxis) || isDrillBit(nextBlock) && nextBlock.getValue(FACING) == direction) {
				start = start.relative(direction);
				poles.add(new StructureBlockInfo(start, nextBlock.setValue(FACING, direction), null));
				
				extensionsInFront++;
				if (isDrillBit(nextBlock)) break;
				
				nextBlock = level.getBlockState(start.relative(direction));
				
				if (extensionsInFront > maxPoles) {
					throw new AssemblyException("tooManyPistonPoles", maxPoles);
				}
			}
		}
		
		if (extensionsInFront == 0) {
			poles.add(new StructureBlockInfo(pos, CBCBlocks.CANNON_DRILL_BIT.getDefaultState().setValue(FACING, direction), null));
		} else {
			poles.add(new StructureBlockInfo(pos, AllBlocks.PISTON_EXTENSION_POLE.getDefaultState().setValue(FACING, direction), null));
		}
		
		BlockPos end = pos;
		int extensionsInBack = 0;
		Direction opposite = direction.getOpposite();
		nextBlock = level.getBlockState(end.relative(opposite));
		
		while (matcher.matchesAxis(nextBlock, blockAxis)) {
			end = end.relative(opposite);
			poles.add(new StructureBlockInfo(end, nextBlock.setValue(FACING, direction), null));
			extensionsInBack++;
			nextBlock = level.getBlockState(end.relative(opposite));
			
			if (extensionsInFront + extensionsInBack > maxPoles) {
				throw new AssemblyException("tooManyPistonPoles", maxPoles);
			}
		}
		
		this.extensionLength = extensionsInFront + extensionsInBack;
		
		if (this.extensionLength == 0) {
			throw AssemblyException.noPistonPoles();
		}
		
		this.anchor = pos.relative(direction, this.initialExtensionProgress + 1);
		this.initialExtensionProgress = extensionsInFront;
		this.pistonContraptionHitbox = new AABB(
				BlockPos.ZERO,
				BlockPos.ZERO.relative(direction, -this.extensionLength - 1))
				.expandTowards(1, 1, 1);
		
		this.bounds = new AABB(0, 0, 0, 0, 0, 0);
		
		for (StructureBlockInfo pole : poles) {
			BlockPos relPos = pole.pos.relative(direction, -extensionsInFront);
			BlockPos localPos = relPos.subtract(this.anchor);
			this.getBlocks().put(localPos, new StructureBlockInfo(localPos, pole.state, null));
		}
		
		return true;
	}
	
	private static boolean isDrillBit(BlockState state) {
		return CBCBlocks.CANNON_DRILL_BIT.has(state);
	}
	
	@Override
	protected boolean addToInitialFrontier(Level world, BlockPos pos, Direction forcedDirection, Queue<BlockPos> frontier) throws AssemblyException {
		frontier.clear();
		return true;
	}
	
	@Override
	protected boolean customBlockPlacement(LevelAccessor level, BlockPos pos, BlockState state) {
		BlockPos levelPos = this.anchor.relative(this.orientation, -1);
		BlockState drillState = level.getBlockState(levelPos);
		BlockEntity blockEntity = level.getBlockEntity(levelPos);
		if (!(blockEntity instanceof CannonDrillBlockEntity drill) || blockEntity.isRemoved()) return true;
		
		if (pos.equals(levelPos)) {
			if (!AllBlocks.PISTON_EXTENSION_POLE.has(state) && CBCBlocks.CANNON_DRILL.has(drillState)) {
				level.setBlock(levelPos, drillState.setValue(CannonDrillBlock.STATE, PistonState.RETRACTED), 3 | 16);
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean customBlockRemoval(LevelAccessor level, BlockPos pos, BlockState state) {
		BlockPos drillPos = this.anchor.relative(this.orientation, -1);
		BlockState drillState = level.getBlockState(drillPos);
		if (pos.equals(drillPos) && CBCBlocks.CANNON_DRILL.has(drillState)) {
			level.setBlock(drillPos, drillState.setValue(CannonDrillBlock.STATE, PistonState.MOVING), 66 | 16);
			return true;
		}
		return false;
	}

	@Override protected ContraptionType getType() { return CBCContraptionTypes.CANNON_DRILL; }

}
