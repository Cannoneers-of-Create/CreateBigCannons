package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionBlock;

public interface BigCannonPropellantBlock<T extends BigCannonPropellantProperties> extends BigCannonMunitionBlock, PropertiesMunitionBlock<T> {

	default float getChargePower(StructureBlockInfo data) {
		T properties = this.getProperties();
		return properties == null ? 2 : properties.strength();
	}
	default float getChargePower(ItemStack stack) {
		T properties = this.getProperties();
		return properties == null ? 2 : properties.strength();
	}

	default float getStressOnCannon(StructureBlockInfo data) {
		T properties = this.getProperties();
		return properties == null ? 1 : properties.addedStress();
	}

	default float getStressOnCannon(ItemStack stack) {
		T properties = this.getProperties();
		return properties == null ? 1 : properties.addedStress();
	}

	default float getSpread(StructureBlockInfo data) {
		T properties = this.getProperties();
		return properties == null ? 2 : properties.addedSpread();
	}

	default float getRecoil(StructureBlockInfo data) {
		T properties = this.getProperties();
		return properties == null ? 2 : properties.addedRecoil();
	}

	void consumePropellant(BigCannonBehavior behavior);

	default boolean isValidAddition(StructureBlockInfo self, int index, Direction dir) { return true; }

	default boolean canBeIgnited(StructureBlockInfo data, Direction dir) { return true; }

}
