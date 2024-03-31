package rbasamoyai.createbigcannons.cannons.big_cannons;

import javax.annotation.Nullable;

import com.simibubi.create.AllBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.Level;
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
		BlockState cannonState = ((BlockEntity) this).getBlockState();
		if (!(cannonState.getBlock() instanceof BigCannonBlock cannon) || !this.cannonBehavior().block().state.isAir()) return false;
		Axis cannonAxis = cannon.getFacing(cannonState).getAxis();
		return isValidMunitionState(cannonAxis, blockInfo);
	}

	default boolean canPushBlock(StructureBlockInfo blockInfo) {
		StructureBlockInfo loadedInfo = this.cannonBehavior().block();
		this.cannonBehavior().removeBlock();
		boolean flag = this.canLoadBlock(blockInfo);
		this.cannonBehavior().loadBlock(loadedInfo);
		return flag;
	}

	static boolean isValidMunitionState(@Nullable Axis cannonAxis, StructureBlockInfo blockInfo) {
		if (blockInfo == null) return false;
		if (isValidLoader(cannonAxis, blockInfo)) return true;
		if (blockInfo.state.getBlock() instanceof BigCannonMunitionBlock munition) {
			return cannonAxis == null || munition.canBeLoaded(blockInfo.state, cannonAxis);
		}
		return false;
	}

	static boolean isValidMunitionState(@Nullable Axis cannonAxis, BlockState state) {
		return isValidMunitionState(cannonAxis, new StructureBlockInfo(BlockPos.ZERO, state, null));
	}

	static boolean isValidLoader(@Nullable Axis cannonAxis, StructureBlockInfo blockInfo) {
		if (blockInfo == null) return false;
		if (CBCBlocks.RAM_HEAD.has(blockInfo.state)
			|| CBCBlocks.WORM_HEAD.has(blockInfo.state)
			|| AllBlocks.PISTON_EXTENSION_POLE.has(blockInfo.state)) {
			return cannonAxis == null || blockInfo.state.getValue(BlockStateProperties.FACING).getAxis() == cannonAxis;
		}
		if (AllBlocks.ROPE.has(blockInfo.state) || AllBlocks.PULLEY_MAGNET.has(blockInfo.state)) {
			return cannonAxis == null || cannonAxis.isVertical();
		}
		return false;
	}

	default boolean blockCanHandle(StructureBlockInfo data) {
		return data.state.is(CBCTags.CBCBlockTags.THICK_TUBING);
	}

	static BlockState getInnerCannonBlockState(Level level, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof BigCannonBlock && level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe) {
			return cbe.cannonBehavior().block().state;
		}
		return state;
	}

}
