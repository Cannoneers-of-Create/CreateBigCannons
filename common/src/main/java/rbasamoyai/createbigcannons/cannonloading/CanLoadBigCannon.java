package rbasamoyai.createbigcannons.cannonloading;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.config.CBCConfigs;

import javax.annotation.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface CanLoadBigCannon {

	void createbigcannons$setBrokenDisassembly(boolean flag);
	boolean createbigcannons$isBrokenDisassembly();

	@Nullable Direction createbigcannons$getAssemblyMovementDirection(Level level);

	BlockPos createbigcannons$toLocalPos(BlockPos globalPos);

	Set<BlockPos> createbigcannons$getFragileBlockPositions();

	Set<BlockPos> createbigcannons$getCannonLoadingColliders();

	static boolean intersectionLoadingEnabled() { return CBCConfigs.SERVER.kinetics.enableIntersectionLoading.get(); }

	static boolean checkForIntersectingBlocks(Level level, AbstractContraptionEntity movedContraption, Map<BlockPos, BlockState> encounteredBlocks) {
		if (!(movedContraption.getContraption() instanceof CanLoadBigCannon loader)) return false;
		BlockPos anchor = new BlockPos(movedContraption.getAnchorVec());
		Set<BlockPos> fragileBlocks = loader.createbigcannons$getFragileBlockPositions();
		Set<BlockPos> encountered = new HashSet<>();
		for (BlockPos lpos : fragileBlocks) {
			BlockPos gpos = anchor.offset(lpos);
			encountered.add(gpos);
			if (encounteredBlocks.containsKey(gpos)) {
				BlockState newState = level.getBlockState(gpos);
				if (!encounteredBlocks.get(gpos).equals(newState) && canBreakLoader(newState)) {
					return true;
				}
			}
			encounteredBlocks.put(gpos, level.getBlockState(gpos));
		}
		encounteredBlocks.entrySet().removeIf(e -> !encountered.contains(e.getKey()));
		return false;
	}

	static boolean canBreakLoader(BlockState state) {
		return !state.getMaterial().isReplaceable() || state.getMaterial().isSolid();
	}

}
