package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

public interface BigCannonPropellantBlock extends BigCannonMunitionBlock {

	float getChargePower(StructureBlockInfo data);
	float getChargePower(ItemStack stack);

	float getStressOnCannon(StructureBlockInfo data);
	float getStressOnCannon(ItemStack stack);

	float getSpread(StructureBlockInfo data);

	void consumePropellant(BigCannonBehavior behavior);

	default boolean isCompatibleWith(BigCannonPropellantBlock other, StructureBlockInfo data, Direction dir) { return this == other; }

	default boolean canBeIgnited(StructureBlockInfo data, Direction dir) { return true; }

}
