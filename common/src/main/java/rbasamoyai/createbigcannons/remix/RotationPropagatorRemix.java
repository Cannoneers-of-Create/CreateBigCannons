package rbasamoyai.createbigcannons.remix;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import rbasamoyai.createbigcannons.base.multiple_kinetic_interface.HasMultipleKineticInterfaces;

public class RotationPropagatorRemix {

	public static BlockEntity replaceGetBlockEntity(BlockEntity original, BlockPos relativePos) {
		if (!(original instanceof HasMultipleKineticInterfaces hasMultiple))
			return original;
		KineticBlockEntity subBE = hasMultiple.getInterfacingBlockEntity(relativePos);
		return subBE == null ? original : subBE;
	}

}
