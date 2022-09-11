package rbasamoyai.createbigcannons.crafting.boring;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public interface TransformableByBoring {

	BlockState getBoredBlockState(BlockState state);
	
	public static boolean wasJustBored(CompoundTag tag) { return tag.contains("JustBored"); }
	
}
