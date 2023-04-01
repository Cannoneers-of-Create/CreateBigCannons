package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;

public interface BigCannonPropellantBlock {

	float getChargePower(StructureBlockInfo data);

	float getStressOnCannon(StructureBlockInfo data);

	float getSpread(StructureBlockInfo data);

	boolean canBeLoaded(BlockState state, Direction.Axis facing);

	void consumePropellant(BigCannonBehavior behavior);

	default boolean isCompatibleWith(BigCannonPropellantBlock other) { return this == other; }

}
