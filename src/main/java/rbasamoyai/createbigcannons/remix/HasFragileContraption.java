package rbasamoyai.createbigcannons.remix;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_loading.CanLoadBigCannon;

public interface HasFragileContraption {

	Map<BlockPos, BlockState> createbigcannons$getEncounteredBlocks();
	Set<BlockPos> createbigcannons$getFragileBlockPositions();

	void createbigcannons$setBrokenDisassembly(boolean flag);
	boolean createbigcannons$isBrokenDisassembly();

	boolean createbigcannons$blockBreaksDisassembly(Level level, BlockPos pos, BlockState newState);

	static boolean defaultBlockBreaksAssembly(Level level, BlockPos pos, BlockState newState, HasFragileContraption contraption) {
		return !contraption.createbigcannons$getEncounteredBlocks().get(pos).equals(newState) && CanLoadBigCannon.canBreakLoader(newState);
	}

	boolean createbigcannons$shouldCheckFragility();

	static boolean defaultShouldCheck() { return !CanLoadBigCannon.intersectionLoadingEnabled(); }

	void createbigcannons$fragileDisassemble();

	static boolean checkForIntersectingBlocks(Level level, AbstractContraptionEntity movedContraption, HasFragileContraption fragile) {
		if (!fragile.createbigcannons$shouldCheckFragility())
			return false;
		BlockPos anchor = BlockPos.containing(movedContraption.getAnchorVec());
		Map<BlockPos, BlockState> encounteredBlocks = fragile.createbigcannons$getEncounteredBlocks();
		Set<BlockPos> fragileBlocks = fragile.createbigcannons$getFragileBlockPositions();

		Set<BlockPos> encountered = new HashSet<>();
		for (BlockPos lpos : fragileBlocks) {
			BlockPos gpos = anchor.offset(lpos);
			encountered.add(gpos);
			if (encounteredBlocks.containsKey(gpos)) {
				BlockState newState = level.getBlockState(gpos);
				if (fragile.createbigcannons$blockBreaksDisassembly(level, gpos, newState))
					return true;
			}
			encounteredBlocks.put(gpos, level.getBlockState(gpos));
		}
		encounteredBlocks.entrySet().removeIf(e -> !encountered.contains(e.getKey()));
		return false;
	}

}
