package rbasamoyai.createbigcannons.cannons;

import java.util.List;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.CBCBlocks;

public class CannonBlockEntity extends SmartTileEntity {

	private CannonBehavior cannonBehavior;
	
	public CannonBlockEntity(BlockEntityType<? extends CannonBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviors) {
		behaviors.add(this.cannonBehavior = new CannonBehavior(this, this::canLoadBlock));
	}
	
	private static final DirectionProperty FACING = BlockStateProperties.FACING;
	private static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
	
	private boolean canLoadBlock(StructureBlockInfo blockInfo) {
		if (blockInfo == null) return false;
		BlockState cannonState = this.getBlockState();
		if (!(cannonState.getBlock() instanceof CannonTubeBlock)) return false;
		Axis cannonAxis = cannonState.getValue(AXIS);
		
		if (!this.cannonBehavior.block().state.isAir()) {
			return false;
		}
		
		if (CBCBlocks.RAM_HEAD.has(blockInfo.state) || AllBlocks.PISTON_EXTENSION_POLE.has(blockInfo.state)) {
			return blockInfo.state.getValue(FACING).getAxis() == cannonAxis;
		}
		if (CBCBlocks.POWDER_CHARGE.has(blockInfo.state)) {
			return blockInfo.state.getValue(AXIS) == cannonAxis;
		}
		
		return false;
	}
	
}
