package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.simibubi.create.AllBlocks;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;

public interface IBigCannonBlockEntity extends ICannonBlockEntity<BigCannonBehavior> {
	
	default boolean canLoadBlock(StructureBlockInfo blockInfo) {
		if (blockInfo == null) return false;
		BlockState cannonState = ((BlockEntity) this).getBlockState();
		if (!(cannonState.getBlock() instanceof BigCannonBlock cannon)) return false;
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
		if (blockInfo.state.getBlock() instanceof BigCannonPropellantBlock propellant) {
			return propellant.canBeLoaded(blockInfo.state, cannonAxis);
		}
		
		return false;
	}

	default boolean blockCanHandle(StructureBlockInfo data) {
		return data.state.is(CBCTags.BlockCBC.THICK_TUBING);
	}
	
}
