package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantProperties;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantPropertiesHandler;

public interface BigCannonPropellantBlock extends BigCannonMunitionBlock {

	default float getChargePower(StructureBlockInfo data) { return this.getPropellantProperties().strength(); }
	default float getChargePower(ItemStack stack) { return this.getPropellantProperties().strength(); }

	default float getStressOnCannon(StructureBlockInfo data) { return this.getPropellantProperties().addedStress(); }
	default float getStressOnCannon(ItemStack stack) { return this.getPropellantProperties().addedStress(); }

	default float getSpread(StructureBlockInfo data) { return this.getPropellantProperties().addedSpread(); }
	default float getRecoil(StructureBlockInfo data) { return this.getPropellantProperties().addedRecoil(); }

	void consumePropellant(BigCannonBehavior behavior);

	default boolean isValidAddition(StructureBlockInfo self, int index, Direction dir) { return true; }

	default boolean canBeIgnited(StructureBlockInfo data, Direction dir) { return true; }
	default BigCannonPropellantProperties getPropellantProperties() { return BigCannonPropellantPropertiesHandler.getProperties((Block) this); }

}
