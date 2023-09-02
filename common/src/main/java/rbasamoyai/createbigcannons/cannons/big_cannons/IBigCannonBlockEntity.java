package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.simibubi.create.AllBlocks;

import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

public interface IBigCannonBlockEntity extends ICannonBlockEntity<BigCannonBehavior> {

	default boolean canLoadBlock(StructureBlockInfo blockInfo) {
		if (blockInfo == null) return false;
		BlockState cannonState = ((BlockEntity) this).getBlockState();
		if (!(cannonState.getBlock() instanceof BigCannonBlock cannon)) return false;
		if (!this.cannonBehavior().block().state().isAir()) return false;

		Axis cannonAxis = cannon.getFacing(cannonState).getAxis();
		if (CBCBlocks.RAM_HEAD.has(blockInfo.state())
			|| CBCBlocks.WORM_HEAD.has(blockInfo.state())
			|| AllBlocks.PISTON_EXTENSION_POLE.has(blockInfo.state())) {
			return blockInfo.state().getValue(BlockStateProperties.FACING).getAxis() == cannonAxis;
		}
		if (blockInfo.state().getBlock() instanceof BigCannonMunitionBlock munition) {
			return munition.canBeLoaded(blockInfo.state(), cannonAxis);
		}

		return false;
	}

	default boolean blockCanHandle(StructureBlockInfo data) {
		return data.state().is(CBCTags.BlockCBC.THICK_TUBING);
	}

}
