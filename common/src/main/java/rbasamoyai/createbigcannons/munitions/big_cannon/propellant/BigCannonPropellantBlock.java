package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

import java.util.List;

public interface BigCannonPropellantBlock extends BigCannonMunitionBlock {

	float getChargePower(StructureBlockInfo data);
	float getChargePower(ItemStack stack);

	float getStressOnCannon(StructureBlockInfo data);
	float getStressOnCannon(ItemStack stack);

	float getSpread(StructureBlockInfo data);

	void consumePropellant(BigCannonBehavior behavior);

	boolean isCompatibleWith(List<StructureBlockInfo> total, StructureBlockInfo self, int index, Direction dir);

	default boolean canBeIgnited(StructureBlockInfo data, Direction dir) { return true; }

}
