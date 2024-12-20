package rbasamoyai.createbigcannons.base.multiple_kinetic_interface;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;

public interface HasMultipleKineticInterfaces {

	/**
	 * Returns the kinetic interface associated with the block position
	 *
	 * @param from relative block position, centered on this block entity
	 * @return the inner block entity
	 */
	@Nullable KineticBlockEntity getInterfacingBlockEntity(BlockPos from);

	List<KineticBlockEntity> getAllKineticBlockEntities();

}
