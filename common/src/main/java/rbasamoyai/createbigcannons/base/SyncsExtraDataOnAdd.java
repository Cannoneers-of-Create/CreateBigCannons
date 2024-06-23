package rbasamoyai.createbigcannons.base;

import net.minecraft.nbt.CompoundTag;

public interface SyncsExtraDataOnAdd {

	default CompoundTag addExtraSyncData() { return new CompoundTag(); }
	default void readExtraSyncData(CompoundTag tag) {}

}
