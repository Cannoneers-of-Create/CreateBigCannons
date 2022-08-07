package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.AllBlocks;

import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.ProjectileBlock;

public interface ICannonBlockEntity {
	
	CannonBehavior cannonBehavior();
	
	default boolean canLoadBlock(StructureBlockInfo blockInfo) {
		if (blockInfo == null) return false;
		BlockState cannonState = ((BlockEntity) this).getBlockState();
		if (!(cannonState.getBlock() instanceof CannonBlock cannon)) return false;
		Axis cannonAxis = cannon.getFacing(cannonState).getAxis();
		
		if (!this.cannonBehavior().block().state.isAir()) {
			return false;
		}
		
		if (CBCBlocks.RAM_HEAD.has(blockInfo.state)
		|| CBCBlocks.WORM_HEAD.has(blockInfo.state)
		|| AllBlocks.PISTON_EXTENSION_POLE.has(blockInfo.state)
		|| blockInfo.state.getBlock() instanceof ProjectileBlock) {
			
			return blockInfo.state.getValue(BlockStateProperties.FACING).getAxis() == cannonAxis;
		}
		if (CBCBlocks.POWDER_CHARGE.has(blockInfo.state)) {
			return blockInfo.state.getValue(BlockStateProperties.AXIS) == cannonAxis;
		}
		
		return false;
	}
	
}
